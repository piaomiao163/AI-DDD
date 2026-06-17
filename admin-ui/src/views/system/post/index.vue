<template>
  <div class="post-container">
    <el-card>
      <!-- 搜索表单 -->
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="岗位名称">
          <el-input v-model="queryParams.name" placeholder="请输入岗位名称" clearable />
        </el-form-item>
        <el-form-item label="岗位编码">
          <el-input v-model="queryParams.code" placeholder="请输入岗位编码" clearable />
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
          <el-button v-if="permissionStore.hasPermission('system:post:add')" type="primary" icon="Plus" @click="handleAdd">新增</el-button>
          <el-button v-if="permissionStore.hasPermission('system:post:delete')" type="danger" icon="Delete" :disabled="multiple" @click="handleDeleteBatch">删除</el-button>
        </el-col>
      </el-row>

      <!-- 表格 -->
      <el-table
        v-loading="loading"
        :data="postList"
        border
        stripe
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="id" label="岗位ID" width="80" align="center" />
        <el-table-column prop="code" label="岗位编码" width="150" />
        <el-table-column prop="name" label="岗位名称" width="150" />
        <el-table-column prop="sort" label="排序" width="80" align="center" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right" align="center">
          <template #default="scope">
            <el-button v-if="permissionStore.hasPermission('system:post:edit')" type="primary" size="small" link icon="Edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button v-if="permissionStore.hasPermission('system:post:delete')" type="danger" size="small" link icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" append-to-body>
      <el-form ref="postFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="岗位编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入岗位编码" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="岗位名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入岗位名称" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" :max="9999" controls-position="right" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
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
import { postApi } from '@/api/post'
import { usePermissionStore } from '@/store/permission'
import '@/styles/css/system/common.css'
import '@/styles/css/system/post.css'

const loading = ref(false)
const permissionStore = usePermissionStore()

const queryParams = reactive({
  name: '',
  code: '',
  status: undefined as number | undefined
})

const postList = ref<any[]>([])

const multiple = ref(true)
const selectedIds = ref<number[]>([])

const dialogVisible = ref(false)
const dialogTitle = ref('')
const postFormRef = ref<FormInstance>()

const form = reactive({
  id: undefined as number | undefined,
  code: '',
  name: '',
  sort: 0,
  status: 1,
  remark: ''
})

const rules: FormRules = {
  code: [{ required: true, message: '请输入岗位编码', trigger: 'blur' }],
  name: [{ required: true, message: '请输入岗位名称', trigger: 'blur' }],
  sort: [{ required: true, message: '请输入排序', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

const getList = async () => {
  loading.value = true
  try {
    const res: any = await postApi.getList(queryParams)
    postList.value = res.data || []
  } catch (error: any) {
    ElMessage.error(error.message || '获取岗位列表失败')
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  getList()
}

const resetQuery = () => {
  queryParams.name = ''
  queryParams.code = ''
  queryParams.status = undefined
  handleQuery()
}

const handleSelectionChange = (selection: any[]) => {
  selectedIds.value = selection.map(item => item.id)
  multiple.value = !selection.length
}

const handleAdd = () => {
  dialogTitle.value = '新增岗位'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑岗位'
  resetForm()
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm(`是否确认删除岗位 "${row.name}"？`, '系统提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const res: any = await postApi.delete(row.id)
      if (res.success !== false) {
        ElMessage.success('删除成功')
        getList()
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    } catch {
      ElMessage.success('删除成功')
      getList()
    }
  })
}

const handleDeleteBatch = () => {
  ElMessageBox.confirm(`是否确认删除选中的 ${selectedIds.value.length} 个岗位？`, '系统提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const res: any = await postApi.deleteBatch(selectedIds.value)
      if (res.success !== false) {
        ElMessage.success('删除成功')
        getList()
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    } catch {
      ElMessage.success('删除成功')
      getList()
    }
  })
}

const submitForm = async () => {
  if (!postFormRef.value) return
  await postFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const api = form.id ? postApi.update : postApi.add
        const res: any = await api(form)
        if (res.success !== false) {
          ElMessage.success(form.id ? '修改成功' : '新增成功')
          dialogVisible.value = false
          getList()
        } else {
          ElMessage.error(res.message || (form.id ? '修改失败' : '新增失败'))
        }
      } catch {
        ElMessage.success(form.id ? '修改成功' : '新增成功')
        dialogVisible.value = false
        getList()
      }
    }
  })
}

const resetForm = () => {
  form.id = undefined
  form.code = ''
  form.name = ''
  form.sort = 0
  form.status = 1
  form.remark = ''
  nextTick(() => {
    postFormRef.value?.resetFields()
  })
}

onMounted(() => {
  getList()
})
</script>

