<template>
  <div class="dept-container">
    <el-card>
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="部门名称">
          <el-input v-model="queryParams.name" placeholder="请输入部门名称" clearable />
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
        :data="deptList"
        border
        stripe
        row-key="id"
        :default-expand-all="isExpandAll"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
        <el-table-column prop="name" label="部门名称" width="200" />
        <el-table-column prop="code" label="部门编码" width="150" />
        <el-table-column prop="leader" label="负责人" width="120" />
        <el-table-column prop="phone" label="联系电话" width="150" />
        <el-table-column prop="email" label="邮箱" min-width="180" show-overflow-tooltip />
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" append-to-body>
      <el-form ref="deptFormRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="上级部门">
          <el-tree-select
            v-model="form.parentId"
            :data="deptTreeOptions"
            :props="{ label: 'name', value: 'id', children: 'children' }"
            placeholder="选择上级部门"
            clearable
            check-strictly
            :render-after-expand="false"
          />
        </el-form-item>
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="部门编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入部门编码" :disabled="!!form.id" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="显示排序" prop="sort">
              <el-input-number v-model="form.sort" :min="0" controls-position="right" class="full-width" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="0">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="负责人" prop="leader">
              <el-input v-model="form.leader" placeholder="请输入负责人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
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
import { deptApi } from '@/api/dept'
import { buildTree } from '@/utils/tree'
import '@/styles/css/system/common.css'
import '@/styles/css/system/dept.css'

const loading = ref(false)

const queryParams = reactive({
  name: '',
  status: undefined as number | undefined
})

const deptList = ref<any[]>([])

const isExpandAll = ref(true)

const dialogVisible = ref(false)
const dialogTitle = ref('')
const deptFormRef = ref<FormInstance>()

const deptTreeOptions = ref<any[]>([])

const form = reactive({
  id: undefined as number | undefined,
  parentId: undefined as number | undefined,
  name: '',
  code: '',
  leader: '',
  phone: '',
  email: '',
  sort: 0,
  status: 1,
  remark: ''
})

const rules: FormRules = {
  name: [{ required: true, message: '请输入部门名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入部门编码', trigger: 'blur' }],
  sort: [{ required: true, message: '请输入显示排序', trigger: 'blur' }],
  status: [{ required: true, message: '请选择部门状态', trigger: 'change' }],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

const getList = async () => {
  loading.value = true
  try {
    const res: any = await deptApi.getList(queryParams)
    deptList.value = buildTree(res.data || [])
  } catch (error: any) {
    ElMessage.error(error.message || '获取部门列表失败')
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
  dialogTitle.value = '新增部门'
  resetForm()
  getDeptTreeOptions()
  dialogVisible.value = true
}

const handleAddChild = (row: any) => {
  dialogTitle.value = '新增部门'
  resetForm()
  form.parentId = row.id
  getDeptTreeOptions()
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑部门'
  resetForm()
  getDeptTreeOptions()
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm(`是否确认删除部门 "${row.name}"？`, '系统提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const res: any = await deptApi.delete(row.id)
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
  if (!deptFormRef.value) return
  await deptFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const api = form.id ? deptApi.update : deptApi.add
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
  form.code = ''
  form.leader = ''
  form.phone = ''
  form.email = ''
  form.sort = 0
  form.status = 1
  form.remark = ''
  nextTick(() => {
    deptFormRef.value?.resetFields()
  })
}

const getDeptTreeOptions = () => {
  deptTreeOptions.value = [
    { id: 0, name: '根目录', children: deptList.value }
  ]
}

onMounted(() => {
  getList()
})
</script>

