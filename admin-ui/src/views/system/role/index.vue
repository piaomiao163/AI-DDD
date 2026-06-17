<template>
  <div class="role-container">
    <el-card>
      <!-- 搜索表单 -->
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="角色名称">
          <el-input v-model="queryParams.name" placeholder="请输入角色名称" clearable />
        </el-form-item>
        <el-form-item label="角色编码">
          <el-input v-model="queryParams.code" placeholder="请输入角色编码" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable class="filter-input">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">搜索</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 操作按钮 -->
      <el-row class="table-toolbar" :gutter="10">
        <el-col>
          <el-button v-if="permissionStore.hasPermission('system:role:add')" type="primary" icon="Plus" @click="handleAdd">新增</el-button>
          <el-button v-if="permissionStore.hasPermission('system:role:delete')" type="danger" icon="Delete" :disabled="multiple" @click="handleDeleteBatch">删除</el-button>
        </el-col>
      </el-row>

      <!-- 表格 -->
      <el-table
        v-loading="loading"
        :data="roleList"
        border
        stripe
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="id" label="角色ID" width="80" align="center" />
        <el-table-column prop="name" label="角色名称" width="150" />
        <el-table-column prop="code" label="角色编码" width="150" />
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="250" fixed="right" align="center">
          <template #default="scope">
            <el-button v-if="permissionStore.hasPermission('system:role:edit')" type="primary" size="small" link icon="Edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button 
              v-if="permissionStore.hasPermission('system:role:assignPermission') && scope.row.code !== 'admin'" 
              type="primary" 
              size="small" 
              link 
              icon="Key" 
              @click="handleAssignPermission(scope.row)"
            >分配权限</el-button>
            <el-button 
              v-if="permissionStore.hasPermission('system:role:delete') && scope.row.code !== 'admin'" 
              type="danger" 
              size="small" 
              link 
              icon="Delete" 
              @click="handleDelete(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleQuery"
        @current-change="handleQuery"
      />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" append-to-body>
      <el-form ref="roleFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入角色编码" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 分配权限对话框 -->
    <el-dialog v-model="permissionDialogVisible" title="分配权限" width="550px" append-to-body>
      <div class="permission-tree-wrapper">
        <el-tree
          ref="permissionTreeRef"
          :data="permissionTreeData"
          show-checkbox
          node-key="id"
          :props="{ label: 'name', children: 'children' }"
          default-expand-all
          :check-strictly="false"
          highlight-current
        />
      </div>
      <template #footer>
        <el-button @click="permissionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitPermission">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { roleApi } from '@/api/role'
import { permissionApi } from '@/api/permission'
import { usePermissionStore } from '@/store/permission'
import { buildTree } from '@/utils/tree'
import '@/styles/css/system/common.css'
import '@/styles/css/system/role.css'

// 加载状态
const loading = ref(false)
const permissionStore = usePermissionStore()

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: '',
  code: '',
  status: undefined as number | undefined
})

// 角色列表
const roleList = ref<any[]>([])
const total = ref(0)

// 多选
const multiple = ref(true)
const selectedIds = ref<number[]>([])

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('')
const roleFormRef = ref<FormInstance>()

// 表单
const form = reactive({
  id: undefined as number | undefined,
  name: '',
  code: '',
  description: '',
  status: 1
})

// 表单校验规则
const rules: FormRules = {
  name: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入角色编码', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

// 权限分配对话框
const permissionDialogVisible = ref(false)
const permissionTreeRef = ref<any>()
const permissionTreeData = ref<any[]>([])
const currentRoleId = ref<number>()

// 获取角色列表
const getList = async () => {
  loading.value = true
  try {
    const res: any = await roleApi.getList(queryParams)
    roleList.value = res.data || []
    total.value = res.data?.length || 0
  } catch (error: any) {
    ElMessage.error(error.message || '获取角色列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

// 重置
const resetQuery = () => {
  queryParams.name = ''
  queryParams.code = ''
  queryParams.status = undefined
  handleQuery()
}

// 多选框选中数据
const handleSelectionChange = (selection: any[]) => {
  selectedIds.value = selection.map(item => item.id)
  multiple.value = !selection.length
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增角色'
  resetForm()
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row: any) => {
  dialogTitle.value = '编辑角色'
  form.id = row.id
  form.name = row.name
  form.code = row.code
  form.description = row.description || ''
  form.status = row.status
  dialogVisible.value = true
  nextTick(() => {
    roleFormRef.value?.clearValidate()
  })
}

// 删除
const handleDelete = (row: any) => {
  ElMessageBox.confirm(`是否确认删除角色 "${row.name}"？`, '系统提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const res: any = await roleApi.delete(row.id)
      if (res.success) {
        ElMessage.success('删除成功')
        getList()
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

// 批量删除
const handleDeleteBatch = () => {
  ElMessageBox.confirm(`是否确认删除选中的 ${selectedIds.value.length} 个角色？`, '系统提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const res: any = await roleApi.deleteBatch(selectedIds.value)
      if (res.success) {
        ElMessage.success('删除成功')
        getList()
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

// 提交表单
const submitForm = async () => {
  if (!roleFormRef.value) return
  await roleFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const api = form.id ? roleApi.update : roleApi.add
        const res: any = await api(form)
        if (res.success) {
          ElMessage.success(form.id ? '修改成功' : '新增成功')
          dialogVisible.value = false
          getList()
        } else {
          ElMessage.error(res.message || (form.id ? '修改失败' : '新增失败'))
        }
      } catch (error) {
        ElMessage.error(form.id ? '修改失败' : '新增失败')
      }
    }
  })
}

// 重置表单
const resetForm = () => {
  form.id = undefined
  form.name = ''
  form.code = ''
  form.description = ''
  form.status = 1
  nextTick(() => {
    roleFormRef.value?.resetFields()
  })
}

// 分配权限
const handleAssignPermission = async (row: any) => {
  currentRoleId.value = row.id
  permissionDialogVisible.value = true
  
  // 先清空树数据
  permissionTreeData.value = []
  
  // 获取权限树
  try {
    const res: any = await permissionApi.getList()
    permissionTreeData.value = buildPermissionTree(res.data || [])

    // 获取角色已有权限
    const roleRes: any = await roleApi.getRolePermissions(row.id)
    const permissionIds = (roleRes.data || []).map((p: any) => p.id)
    
    // 等待DOM更新后再设置选中状态
    nextTick(() => {
      nextTick(() => {
        permissionTreeRef.value?.setCheckedKeys([])
        permissionTreeRef.value?.setCheckedKeys(permissionIds)
      })
    })
  } catch (error: any) {
    ElMessage.error(error.message || '获取权限数据失败')
  }
}

// 提交权限分配
const submitPermission = async () => {
  if (!currentRoleId.value) return
  const permissionIds = permissionTreeRef.value?.getCheckedKeys() || []
  
  try {
    const res: any = await roleApi.assignRolePermissions(currentRoleId.value, permissionIds)
    // request拦截器已经返回了response.data，所以直接检查res.success
    if (res.success !== false) {
      ElMessage.success('权限分配成功')
      permissionDialogVisible.value = false
    } else {
      ElMessage.error(res.message || '权限分配失败')
    }
  } catch (error: any) {
    ElMessage.error(error.message || '权限分配失败')
  }
}

// 构建权限树
const buildPermissionTree = (data: any[]) => {
  const tree = buildTree(data, { sortByKey: null })
  // 如果顶层只有一个 system:manage 根节点，将其子节点提升为根节点
  if (tree.length === 1 && tree[0].code === 'system:manage') {
    return tree[0].children || []
  }
  return tree
}

onMounted(() => {
  getList()
})
</script>

