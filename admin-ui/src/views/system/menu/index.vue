<template>
  <div class="menu-container">
    <el-card>
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="菜单名称">
          <el-input v-model="queryParams.name" placeholder="请输入菜单名称" clearable />
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
          <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
          <el-button type="info" icon="Sort" @click="toggleExpandAll">展开/折叠</el-button>
        </el-col>
      </el-row>

      <el-table
        v-loading="loading"
        :data="menuList"
        border
        stripe
        row-key="id"
        :default-expand-all="isExpandAll"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
        <el-table-column prop="name" label="菜单名称" width="200" />
        <el-table-column prop="icon" label="图标" width="80" align="center">
          <template #default="scope">
            <el-icon v-if="scope.row.icon" size="18">
              <component :is="getIconComponent(scope.row.icon)" />
            </el-icon>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路由路径" width="200" show-overflow-tooltip />
        <el-table-column prop="component" label="组件路径" min-width="200" show-overflow-tooltip />
        <el-table-column prop="sort" label="排序" width="80" align="center" />
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
            <el-button type="primary" size="small" link icon="Edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="primary" size="small" link icon="Plus" @click="handleAddChild(scope.row)">新增</el-button>
            <el-button type="danger" size="small" link icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px" append-to-body>
      <el-form ref="menuFormRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="上级菜单">
              <el-tree-select
                v-model="form.parentId"
                :data="menuTreeOptions"
                :props="{ label: 'name', value: 'id', children: 'children' }"
                placeholder="选择上级菜单"
                clearable
                check-strictly
                :render-after-expand="false"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单类型" prop="type">
              <el-radio-group v-model="form.type">
                <el-radio label="MENU">菜单</el-radio>
                <el-radio label="BUTTON">按钮</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入菜单名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="sort">
              <el-input-number v-model="form.sort" :min="0" controls-position="right" class="full-width" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单图标" prop="icon">
              <el-select v-model="form.icon" placeholder="请选择图标" clearable class="full-width">
                <el-option
                  v-for="item in iconOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                  <el-icon size="16" class="mr-8">
                    <component :is="item.component" />
                  </el-icon>
                  {{ item.label }}
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.type === 'MENU'">
            <el-form-item label="路由地址" prop="path">
              <el-input v-model="form.path" placeholder="请输入路由地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.type === 'MENU'">
            <el-form-item label="组件路径" prop="component">
              <el-input v-model="form.component" placeholder="请输入组件路径" />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.type === 'BUTTON'">
            <el-form-item label="权限标识" prop="permission">
              <el-input v-model="form.permission" placeholder="请输入权限标识" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="0">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.type === 'MENU'">
            <el-form-item label="是否显示" prop="visible">
              <el-radio-group v-model="form.visible">
                <el-radio :label="1">显示</el-radio>
                <el-radio :label="0">隐藏</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
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
import { menuApi } from '@/api/menu'
import { buildTree } from '@/utils/tree'
import '@/styles/css/system/common.css'
import '@/styles/css/system/menu.css'
import {
  House,
  User,
  UserFilled,
  Menu,
  Setting,
  Lock,
  OfficeBuilding,
  Monitor,
  Document,
  Folder,
  Key,
  CircleCheck,
  Star,
  Bell,
  Message,
  Calendar,
  Search,
  Edit,
  Delete,
  Plus,
  View,
  Upload,
  Download,
  Link
} from '@element-plus/icons-vue'

const loading = ref(false)

const queryParams = reactive({
  name: '',
  status: undefined as number | undefined
})

const menuList = ref<any[]>([])

const isExpandAll = ref(true)

const dialogVisible = ref(false)
const dialogTitle = ref('')
const menuFormRef = ref<FormInstance>()

const menuTreeOptions = ref<any[]>([])

const form = reactive({
  id: undefined as number | undefined,
  parentId: undefined as number | undefined,
  name: '',
  type: 'MENU',
  icon: '',
  path: '',
  component: '',
  permission: '',
  sort: 0,
  status: 1,
  visible: 1,
  remark: ''
})

const rules: FormRules = {
  name: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择菜单类型', trigger: 'change' }],
  sort: [{ required: true, message: '请输入显示排序', trigger: 'blur' }],
  status: [{ required: true, message: '请选择菜单状态', trigger: 'change' }]
}

const iconOptions = [
  { label: '首页', value: 'House', component: House },
  { label: '用户', value: 'User', component: User },
  { label: '用户组', value: 'UserFilled', component: UserFilled },
  { label: '菜单', value: 'Menu', component: Menu },
  { label: '设置', value: 'Setting', component: Setting },
  { label: '锁', value: 'Lock', component: Lock },
  { label: '建筑', value: 'OfficeBuilding', component: OfficeBuilding },
  { label: '监控', value: 'Monitor', component: Monitor },
  { label: '文档', value: 'Document', component: Document },
  { label: '文件夹', value: 'Folder', component: Folder },
  { label: '钥匙', value: 'Key', component: Key },
  { label: '勾选', value: 'CircleCheck', component: CircleCheck },
  { label: '星星', value: 'Star', component: Star },
  { label: '消息', value: 'Bell', component: Bell },
  { label: '邮件', value: 'Message', component: Message },
  { label: '日历', value: 'Calendar', component: Calendar },
  { label: '搜索', value: 'Search', component: Search },
  { label: '编辑', value: 'Edit', component: Edit },
  { label: '删除', value: 'Delete', component: Delete },
  { label: '新增', value: 'Plus', component: Plus },
  { label: '查看', value: 'View', component: View },
  { label: '上传', value: 'Upload', component: Upload },
  { label: '下载', value: 'Download', component: Download },
  { label: '链接', value: 'Link', component: Link }
]

const iconComponentMap: Record<string, any> = {
  House, User, UserFilled, Menu, Setting, Lock, OfficeBuilding, Monitor,
  Document, Folder, Key, CircleCheck, Star, Bell, Message, Calendar,
  Search, Edit, Delete, Plus, View, Upload, Download, Link
}

const getIconComponent = (iconName: string) => {
  return iconComponentMap[iconName] || Menu
}

const getList = async () => {
  loading.value = true
  try {
    const res: any = await menuApi.getList(queryParams)
    menuList.value = buildTree(res.data || [])
  } catch (error: any) {
    ElMessage.error(error.message || '获取菜单列表失败')
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
  dialogTitle.value = '新增菜单'
  resetForm()
  getMenuTreeOptions()
  dialogVisible.value = true
}

const handleAddChild = (row: any) => {
  dialogTitle.value = '新增菜单'
  resetForm()
  form.parentId = row.id
  getMenuTreeOptions()
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑菜单'
  resetForm()
  getMenuTreeOptions()
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm(`是否确认删除菜单 "${row.name}"？`, '系统提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const res: any = await menuApi.delete(row.id)
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
  if (!menuFormRef.value) return
  await menuFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const api = form.id ? menuApi.update : menuApi.add
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

const resetForm = () => {
  form.id = undefined
  form.parentId = undefined
  form.name = ''
  form.type = 'MENU'
  form.icon = ''
  form.path = ''
  form.component = ''
  form.permission = ''
  form.sort = 0
  form.status = 1
  form.visible = 1
  form.remark = ''
  nextTick(() => {
    menuFormRef.value?.resetFields()
  })
}

const getMenuTreeOptions = () => {
  menuTreeOptions.value = [
    { id: 0, name: '根目录', children: menuList.value }
  ]
}

onMounted(() => {
  getList()
})
</script>

