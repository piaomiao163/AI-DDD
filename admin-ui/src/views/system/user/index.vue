<template>
  <div class="user-container">
    <el-card>
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="用户名">
          <el-input v-model="queryParams.username" placeholder="请输入用户名" clearable />
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
      
      <el-row class="table-toolbar" :gutter="10">
        <el-col>
          <el-button v-if="permissionStore.hasPermission('system:user:add')" type="primary" icon="Plus" @click="handleAdd">新增</el-button>
          <el-button v-if="permissionStore.hasPermission('system:user:delete')" type="danger" icon="Delete" :disabled="multiple" @click="handleDeleteBatch">删除</el-button>
        </el-col>
      </el-row>
      
      <el-table
        v-loading="loading"
        :data="userList"
        border
        stripe
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="id" label="用户ID" width="80" align="center" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="nickname" label="昵称" width="120" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="department.name" label="部门" width="120" />
        <el-table-column label="角色" width="180">
          <template #default="scope">
            <el-tag v-for="role in scope.row.roles" :key="role.id" size="small" class="mr-4">
              {{ role.name }}
            </el-tag>
            <span v-if="!scope.row.roles || scope.row.roles.length === 0">无</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" sortable="custom" />
        <el-table-column label="操作" width="250" fixed="right" align="center">
          <template #default="scope">
            <el-button v-if="permissionStore.hasPermission('system:user:edit')" type="primary" size="small" link icon="Edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button v-if="permissionStore.hasPermission('system:user:edit')" type="primary" size="small" link icon="UserFilled" @click="handleAssignRole(scope.row)">分配角色</el-button>
            <el-button v-if="permissionStore.hasPermission('system:user:delete')" type="danger" size="small" link icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
      />
    </el-card>
    
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form ref="userFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!form.id">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="部门" prop="departmentId">
          <el-tree-select
            v-model="form.departmentId"
            :data="deptOptions"
            :props="{ value: 'id', label: 'name', children: 'children' }"
            value-key="id"
            placeholder="请选择部门"
            clearable
            check-strictly
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="角色">
          <el-checkbox-group v-model="form.roleIds">
            <el-checkbox v-for="role in roleOptions" :key="role.id" :label="role.id">
              {{ role.name }}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="roleDialogVisible" title="分配角色" width="500px" append-to-body>
      <el-checkbox-group v-model="selectedRoles">
        <el-checkbox v-for="role in roleOptions" :key="role.id" :label="role.id">
          {{ role.name }}
        </el-checkbox>
      </el-checkbox-group>
      <template #footer>
        <el-button @click="roleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAssignRole">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { userApi } from '@/api/user'
import { roleApi } from '@/api/role'
import { deptApi } from '@/api/dept'
import { usePermissionStore } from '@/store/permission'
import '@/styles/css/system/common.css'
import '@/styles/css/system/user.css'

const loading = ref(false)

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  username: '',
  status: undefined as number | undefined,
  sortField: '',
  sortOrder: ''
})

const userList = ref<any[]>([])
const total = ref(0)

const multiple = ref(true)
const selectedIds = ref<number[]>([])

const dialogVisible = ref(false)
const dialogTitle = ref('')
const userFormRef = ref<FormInstance>()

const form = reactive({
  id: undefined as number | undefined,
  username: '',
  nickname: '',
  password: '',
  email: '',
  phone: '',
  departmentId: undefined as number | undefined,
  status: 1,
  roleIds: [] as number[]
})

const rules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }]
}

const roleDialogVisible = ref(false)
const roleOptions = ref<any[]>([])
const selectedRoles = ref<number[]>([])
const currentUserId = ref<number>()

const deptOptions = ref<any[]>([])
const permissionStore = usePermissionStore()

const getDeptList = async () => {
  try {
    const res: any = await deptApi.getTree()
    deptOptions.value = res.data || []
  } catch (error: any) {
    ElMessage.error(error.message || '获取部门列表失败')
  }
}

const getList = async () => {
  loading.value = true
  try {
    const res: any = await userApi.getList(queryParams)
    // 后端分页
    userList.value = res.data?.list || []
    total.value = res.data?.total || 0
  } catch (error: any) {
    ElMessage.error(error.message || '获取用户列表失败')
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

const handlePageChange = (page: number) => {
  queryParams.pageNum = page
  getList()
}

const handleSizeChange = (size: number) => {
  queryParams.pageSize = size
  queryParams.pageNum = 1
  getList()
}

const handleSortChange = (sort: any) => {
  if (sort.prop === 'createTime') {
    queryParams.sortField = 'create_time'
    queryParams.sortOrder = sort.order === 'ascending' ? 'asc' : 'desc'
    getList()
  }
}

const resetQuery = () => {
  queryParams.username = ''
  queryParams.status = undefined
  queryParams.sortField = ''
  queryParams.sortOrder = ''
  handleQuery()
}

const handleSelectionChange = (selection: any[]) => {
  selectedIds.value = selection.map(item => item.id)
  multiple.value = !selection.length
}

const handleAdd = async () => {
  dialogTitle.value = '新增用户'
  resetForm()
  // 加载角色列表
  try {
    const res: any = await roleApi.getList()
    roleOptions.value = res.data || []
  } catch (error: any) {
    ElMessage.error(error.message || '获取角色列表失败')
  }
  dialogVisible.value = true
}

const handleEdit = async (row: any) => {
  dialogTitle.value = '编辑用户'
  resetForm()
  Object.assign(form, row)
  // 加载角色列表
  try {
    const res: any = await roleApi.getList()
    roleOptions.value = res.data || []
    // 设置已选角色
    form.roleIds = row.roles?.map((r: any) => r.id) || []
  } catch (error: any) {
    ElMessage.error(error.message || '获取角色列表失败')
  }
  dialogVisible.value = true
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm(`是否确认删除用户 "${row.username}"？`, '系统提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const res: any = await userApi.delete(row.id)
      if (res.success !== false) {
        ElMessage.success('删除成功')
        getList()
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    } catch (error: any) {
      ElMessage.error(error.message || '删除失败')
    }
  })
}

const handleDeleteBatch = () => {
  if (!selectedIds.value.length) {
    ElMessage.warning('请选择要删除的用户')
    return
  }
  
  ElMessageBox.confirm('是否确认删除选中的用户？', '系统提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const res: any = await userApi.deleteBatch(selectedIds.value)
      if (res.success !== false) {
        ElMessage.success('删除成功')
        getList()
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    } catch (error: any) {
      ElMessage.error(error.message || '删除失败')
    }
  })
}

const submitForm = async () => {
  if (!userFormRef.value) return
  
  try {
    await userFormRef.value.validate()
    
    const res: any = await (form.id ? userApi.update(form) : userApi.add(form))
    if (res.success !== false) {
      const userId = form.id || res.data
      // 分配角色
      if (form.roleIds && form.roleIds.length > 0) {
        try {
          await userApi.assignUserRoles(userId, form.roleIds)
        } catch (error: any) {
          ElMessage.warning('用户保存成功，但角色分配失败：' + (error.message || '未知错误'))
        }
      }
      ElMessage.success(form.id ? '修改成功' : '新增成功')
      dialogVisible.value = false
      getList()
    } else {
      ElMessage.error(res.message || (form.id ? '修改失败' : '新增失败'))
    }
  } catch (error: any) {
    ElMessage.error(error.message || (form.id ? '修改失败' : '新增失败'))
  }
}

const resetForm = () => {
  form.id = undefined
  form.username = ''
  form.nickname = ''
  form.password = ''
  form.email = ''
  form.phone = ''
  form.departmentId = undefined
  form.status = 1
  form.roleIds = []
  userFormRef.value?.clearValidate()
}

const handleAssignRole = async (row: any) => {
  currentUserId.value = row.id
  roleDialogVisible.value = true
  
  try {
    const res: any = await roleApi.getList()
    roleOptions.value = res.data || []
    
    const userRoleRes: any = await userApi.getUserRoles(row.id)
    selectedRoles.value = userRoleRes.data?.map((r: any) => r.id) || []
  } catch (error: any) {
    ElMessage.error(error.message || '获取角色数据失败')
  }
}

const submitAssignRole = async () => {
  if (!currentUserId.value) return
  try {
    const res: any = await userApi.assignUserRoles(currentUserId.value, selectedRoles.value)
    if (res.success !== false) {
      ElMessage.success('角色分配成功')
      roleDialogVisible.value = false
      getList() // 重新获取用户列表，以显示更新后的角色信息
    } else {
      ElMessage.error(res.message || '角色分配失败')
    }
  } catch (error: any) {
    ElMessage.error(error.message || '角色分配失败')
  }
}

onMounted(() => {
  getList()
  getDeptList()
})
</script>

