<template>
  <div class="permission-container">
    <el-card>
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="权限名称">
          <el-input v-model="queryParams.name" placeholder="请输入权限名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">搜索</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-row class="table-toolbar" :gutter="10">
        <el-col>
          <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
          <el-button type="info" icon="Sort" @click="toggleExpandAll">{{ isExpandAll ? '折叠' : '展开' }}</el-button>
        </el-col>
      </el-row>

      <el-table
        v-loading="loading"
        :data="permissionList"
        border
        stripe
        row-key="id"
        :default-expand-all="isExpandAll"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
        <el-table-column prop="name" label="权限名称" width="200" />
        <el-table-column prop="code" label="权限编码" width="220" show-overflow-tooltip />
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="type" label="类型" width="100" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.type === 1 ? 'primary' : 'success'">
              {{ scope.row.type === 1 ? '菜单权限' : '按钮权限' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="关联菜单" width="150" align="center">
          <template #default="scope">
            {{ scope.row.menuId ? menuMap.get(scope.row.menuId) || '-' : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right" align="center">
          <template #default="scope">
            <el-button type="primary" size="small" link icon="Edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="primary" size="small" link icon="Plus" @click="handleAddChild(scope.row)">新增</el-button>
            <el-button type="danger" size="small" link icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" append-to-body>
      <el-form ref="permissionFormRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="上级权限" prop="parentId">
          <el-tree-select
            v-model="form.parentId"
            :data="permissionTreeOptions"
            :props="{ value: 'id', label: 'name', children: 'children' }"
            value-key="id"
            placeholder="请选择上级权限"
            check-strictly
            clearable
            class="full-width"
          />
        </el-form-item>
        <el-form-item label="权限名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入权限名称" />
        </el-form-item>
        <el-form-item label="权限编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入权限编码" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="权限类型" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio :label="1">菜单权限</el-radio>
            <el-radio :label="2">按钮权限</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="关联菜单" prop="menuId">
          <el-tree-select
            v-model="form.menuId"
            :data="menuTreeOptions"
            :props="{ value: 'id', label: 'name', children: 'children' }"
            value-key="id"
            placeholder="请选择关联菜单"
            check-strictly
            clearable
            class="full-width"
          />
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
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { permissionApi } from '@/api/permission'
import { buildTree } from '@/utils/tree'
import '@/styles/css/system/common.css'
import '@/styles/css/system/permission.css'
import { menuApi } from '@/api/menu'

const loading = ref(false)
const menuMap = ref<Map<number, string>>(new Map())

const queryParams = reactive({
  name: '',
  status: undefined as number | undefined
})

const permissionList = ref<any[]>([])

const isExpandAll = ref(true)

const dialogVisible = ref(false)
const dialogTitle = ref('')
const permissionFormRef = ref<FormInstance>()

const permissionTreeOptions = ref<any[]>([])
const menuTreeOptions = ref<any[]>([])

const form = reactive({
  id: undefined as number | undefined,
  parentId: undefined as number | undefined,
  name: '',
  code: '',
  description: '',
  type: 1,
  status: 1,
  menuId: undefined as number | undefined
})

const rules: FormRules = {
  name: [{ required: true, message: '请输入权限名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入权限编码', trigger: 'blur' }],
  type: [{ required: true, message: '请选择权限类型', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

const buildMenuMap = (menus: any[]) => {
  const map = new Map<number, string>()
  const traverse = (menuList: any[]) => {
    menuList.forEach(menu => {
      map.set(menu.id, menu.name)
      if (menu.children && menu.children.length > 0) {
        traverse(menu.children)
      }
    })
  }
  traverse(menus)
  return map
}

const getList = async () => {
  loading.value = true
  try {
    const [permissionRes, menuRes] = await Promise.all([
      permissionApi.getList(queryParams),
      menuApi.getTree()
    ])
    permissionList.value = buildTree(permissionRes.data || [])
    menuMap.value = buildMenuMap(menuRes.data || [])
  } catch (error: any) {
    ElMessage.error(error.message || '获取权限列表失败')
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  getList()
}

const resetQuery = () => {
  queryParams.name = ''
  queryParams.status = undefined
  handleQuery()
}

const toggleExpandAll = () => {
  isExpandAll.value = !isExpandAll.value
  getList()
}

const handleAdd = () => {
  dialogTitle.value = '新增权限'
  resetForm()
  getPermissionTreeOptions()
  getMenuTreeOptions()
  dialogVisible.value = true
}

const handleAddChild = (row: any) => {
  dialogTitle.value = '新增权限'
  resetForm()
  form.parentId = row.id
  getPermissionTreeOptions()
  getMenuTreeOptions()
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑权限'
  resetForm(true)
  Object.assign(form, row)
  getPermissionTreeOptions()
  getMenuTreeOptions()
  dialogVisible.value = true
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm(`是否确认删除权限 "${row.name}"？`, '系统提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const res: any = await permissionApi.delete(row.id)
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
  if (!permissionFormRef.value) return
  await permissionFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const api = form.id ? permissionApi.update : permissionApi.add
        const res: any = await api(form)
        if (res.success !== false) {
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
  })
}

const resetForm = (isEdit: boolean = false) => {
  form.id = undefined
  form.parentId = undefined
  form.name = ''
  form.code = ''
  form.description = ''
  form.type = 1
  form.status = 1
  form.menuId = undefined
  if (!isEdit) {
    nextTick(() => {
      permissionFormRef.value?.resetFields()
    })
  }
}

const getPermissionTreeOptions = async () => {
  try {
    const res: any = await permissionApi.getList()
    const treeData = buildTree(res.data || [])
    permissionTreeOptions.value = [{ id: 0, name: '顶级权限', children: treeData }]
  } catch (error: any) {
    console.error('获取权限树选项失败:', error)
  }
}

const getMenuTreeOptions = async () => {
  try {
    const res: any = await menuApi.getTree()
    menuTreeOptions.value = res.data || []
  } catch (error: any) {
    console.error('获取菜单树选项失败:', error)
  }
}

onMounted(() => {
  getList()
})
</script>

