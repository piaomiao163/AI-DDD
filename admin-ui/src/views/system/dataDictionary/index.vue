<template>
  <div class="data-dictionary-container">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span>数据字典管理</span>
          <el-button type="primary" @click="handleAdd" :disabled="!hasPermission('system:dataDictionary:add')">
            <el-icon><Plus /></el-icon>
            新增
          </el-button>
        </div>
      </template>
      
      <el-table v-loading="loading" :data="dataDictionaryList" class="full-width">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="字典名称" />
        <el-table-column prop="code" label="字典编码" />
        <el-table-column prop="description" label="字典描述" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="300" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)" :disabled="!hasPermission('system:dataDictionary:update')">
              编辑
            </el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row.id)" :disabled="!hasPermission('system:dataDictionary:delete')">
              删除
            </el-button>
            <el-button size="small" type="primary" @click="handleItemManage(scope.row)" :disabled="!hasPermission('system:dataDictionary:item:list')">
              管理字典项
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
    >
      <el-form
        ref="dataDictionaryFormRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="字典名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入字典名称" />
        </el-form-item>
        <el-form-item label="字典编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入字典编码" />
        </el-form-item>
        <el-form-item label="字典描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            placeholder="请输入字典描述"
            :rows="3"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="1">启用</el-radio>
            <el-radio label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 字典项管理对话框 -->
    <el-dialog
      v-model="itemDialogVisible"
      :title="`字典项管理 - ${currentDictionary?.name || ''}`"
      width="700px"
    >
      <div class="item-header">
        <el-button type="primary" @click="handleAddItem" :disabled="!hasPermission('system:dataDictionary:item:add')">
          <el-icon><Plus /></el-icon>
          新增字典项
        </el-button>
      </div>
      <el-table v-loading="itemLoading" :data="itemList" class="full-width">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="字典项名称" />
        <el-table-column prop="value" label="字典项值" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="handleEditItem(scope.row)" :disabled="!hasPermission('system:dataDictionary:item:update')">
              编辑
            </el-button>
            <el-button size="small" type="danger" @click="handleDeleteItem(scope.row.id)" :disabled="!hasPermission('system:dataDictionary:item:delete')">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 字典项编辑对话框 -->
      <el-dialog
        v-model="itemEditDialogVisible"
        :title="itemEditDialogTitle"
        width="500px"
      >
        <el-form
          ref="itemFormRef"
          :model="itemForm"
          :rules="itemRules"
          label-width="100px"
        >
          <el-form-item label="字典项名称" prop="name">
            <el-input v-model="itemForm.name" placeholder="请输入字典项名称" />
          </el-form-item>
          <el-form-item label="字典项值" prop="value">
            <el-input v-model="itemForm.value" placeholder="请输入字典项值" />
          </el-form-item>
          <el-form-item label="排序" prop="sort">
            <el-input-number v-model="itemForm.sort" :min="0" placeholder="请输入排序" />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="itemForm.status">
              <el-radio label="1">启用</el-radio>
              <el-radio label="0">禁用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="itemEditDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleItemSubmit">确定</el-button>
          </span>
        </template>
      </el-dialog>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import dataDictionaryApi from '@/api/dataDictionary'
import { usePermissionStore } from '@/store/permission'
import '@/styles/css/system/common.css'
import '@/styles/css/system/dataDictionary.css'

const permissionStore = usePermissionStore()

// 数据字典列表
const dataDictionaryList = ref<any[]>([])
const loading = ref(false)

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('新增数据字典')
const dataDictionaryFormRef = ref()

// 表单数据
const form = reactive({
  id: undefined,
  name: '',
  code: '',
  description: '',
  status: 1
})

// 表单验证规则
const rules = reactive({
  name: [{ required: true, message: '请输入字典名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入字典编码', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
})

// 字典项管理
const itemDialogVisible = ref(false)
const currentDictionary = ref<any>(null)
const itemList = ref<any[]>([])
const itemLoading = ref(false)

// 字典项编辑
const itemEditDialogVisible = ref(false)
const itemEditDialogTitle = ref('新增字典项')
const itemFormRef = ref()
const itemForm = reactive({
  id: undefined,
  dictionaryId: undefined,
  name: '',
  value: '',
  sort: 0,
  status: 1
})

// 字典项验证规则
const itemRules = reactive({
  name: [{ required: true, message: '请输入字典项名称', trigger: 'blur' }],
  value: [{ required: true, message: '请输入字典项值', trigger: 'blur' }],
  sort: [{ required: true, message: '请输入排序', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
})

// 权限检查
const hasPermission = (permission: string) => {
  return permissionStore.hasPermission(permission)
}

// 获取数据字典列表
const loadDataDictionaryList = async () => {
  loading.value = true
  try {
    const response = await dataDictionaryApi.list()
    dataDictionaryList.value = response.data
  } catch (error) {
    ElMessage.error('获取数据字典列表失败')
  } finally {
    loading.value = false
  }
}

// 新增数据字典
const handleAdd = () => {
  dialogTitle.value = '新增数据字典'
  form.id = undefined
  form.name = ''
  form.code = ''
  form.description = ''
  form.status = 1
  dialogVisible.value = true
}

// 编辑数据字典
const handleEdit = (row: any) => {
  dialogTitle.value = '编辑数据字典'
  form.id = row.id
  form.name = row.name
  form.code = row.code
  form.description = row.description
  form.status = row.status
  dialogVisible.value = true
}

// 删除数据字典
const handleDelete = async (id: number) => {
  try {
    await ElMessageBox.confirm('确定要删除该数据字典吗？删除后将无法恢复', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await dataDictionaryApi.delete(id)
    ElMessage.success('删除成功')
    loadDataDictionaryList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!dataDictionaryFormRef.value) return
  
  try {
    await dataDictionaryFormRef.value.validate()
    
    if (form.id) {
      await dataDictionaryApi.update(form)
      ElMessage.success('更新成功')
    } else {
      await dataDictionaryApi.create(form)
      ElMessage.success('创建成功')
    }
    
    dialogVisible.value = false
    loadDataDictionaryList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

// 管理字典项
const handleItemManage = async (dictionary: any) => {
  currentDictionary.value = dictionary
  itemDialogVisible.value = true
  await loadItemList(dictionary.id)
}

// 获取字典项列表
const loadItemList = async (dictionaryId: number) => {
  itemLoading.value = true
  try {
    const response = await dataDictionaryApi.getItemList(dictionaryId)
    itemList.value = response.data
  } catch (error) {
    ElMessage.error('获取字典项列表失败')
  } finally {
    itemLoading.value = false
  }
}

// 新增字典项
const handleAddItem = () => {
  itemEditDialogTitle.value = '新增字典项'
  itemForm.id = undefined
  itemForm.dictionaryId = currentDictionary.value.id
  itemForm.name = ''
  itemForm.value = ''
  itemForm.sort = 0
  itemForm.status = 1
  itemEditDialogVisible.value = true
}

// 编辑字典项
const handleEditItem = (row: any) => {
  itemEditDialogTitle.value = '编辑字典项'
  itemForm.id = row.id
  itemForm.dictionaryId = row.dictionaryId
  itemForm.name = row.name
  itemForm.value = row.value
  itemForm.sort = row.sort
  itemForm.status = row.status
  itemEditDialogVisible.value = true
}

// 删除字典项
const handleDeleteItem = async (id: number) => {
  try {
    await ElMessageBox.confirm('确定要删除该字典项吗？删除后将无法恢复', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await dataDictionaryApi.deleteItem(id)
    ElMessage.success('删除成功')
    loadItemList(currentDictionary.value.id)
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 提交字典项
const handleItemSubmit = async () => {
  if (!itemFormRef.value) return
  
  try {
    await itemFormRef.value.validate()
    
    if (itemForm.id) {
      await dataDictionaryApi.updateItem(itemForm)
      ElMessage.success('更新成功')
    } else {
      await dataDictionaryApi.createItem(itemForm)
      ElMessage.success('创建成功')
    }
    
    itemEditDialogVisible.value = false
    loadItemList(currentDictionary.value.id)
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

// 生命周期
onMounted(() => {
  loadDataDictionaryList()
})
</script>

