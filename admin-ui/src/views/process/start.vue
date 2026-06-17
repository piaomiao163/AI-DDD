<template>
  <div class="process-start">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>发起流程</span>
        </div>
      </template>
      <el-table :data="processList" v-loading="loading" stripe border>
        <el-table-column prop="name" label="流程名称" min-width="150" />
        <el-table-column prop="processKey" label="流程标识" min-width="120">
          <template #default="scope">
            <el-tag type="info" size="small">{{ scope.row.processKey }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="分类" width="100" />
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
        <el-table-column label="操作" width="120" align="center" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="openStartDialog(scope.row)">
              <el-icon><Position /></el-icon>
              发起
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 发起对话框 -->
    <el-dialog v-model="dialogVisible" :title="`发起流程 - ${currentProcess.name}`" width="500px">
      <el-form :model="startForm" label-width="80px">
        <el-form-item label="审批标题" required>
          <el-input v-model="startForm.title" placeholder="如：张三的请假申请" clearable />
        </el-form-item>
        <el-form-item label="业务类型">
          <el-select v-model="startForm.businessType" placeholder="可选" clearable style="width: 100%">
            <el-option label="请假" value="leave" />
            <el-option label="报销" value="expense" />
            <el-option label="采购" value="purchase" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="startForm.priority" placeholder="普通" style="width: 100%">
            <el-option label="普通" :value="0" />
            <el-option label="紧急" :value="1" />
            <el-option label="特急" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="业务Key">
          <el-input v-model="startForm.businessKey" placeholder="可选，如：ORDER-001" clearable />
        </el-form-item>
        <el-form-item label="备注">
          <el-input type="textarea" v-model="startForm.reason" placeholder="请输入发起原因" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleStart" :loading="starting">确定发起</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { Position } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import * as processApi from '@/api/process'
import * as instanceApi from '@/api/processInstance'
import type { ProcessDefinition } from '@/api/process'
import '@/styles/css/process/start.css'

const processList = ref<ProcessDefinition[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const starting = ref(false)

const currentProcess = reactive<Partial<ProcessDefinition>>({})
const startForm = reactive({
  title: '',
  businessType: '',
  priority: 0,
  businessKey: '',
  reason: ''
})

onMounted(() => {
  fetchPublishedProcesses()
})

const fetchPublishedProcesses = async () => {
  loading.value = true
  try {
    const res = await processApi.getAllProcesses()
    processList.value = (res.data || []).filter((p: ProcessDefinition) => p.status === 1)
  } catch (error) {
    ElMessage.error('获取流程列表失败')
  } finally {
    loading.value = false
  }
}

const openStartDialog = (process: ProcessDefinition) => {
  Object.assign(currentProcess, process)
  startForm.title = ''
  startForm.businessType = ''
  startForm.priority = 0
  startForm.businessKey = ''
  startForm.reason = ''
  dialogVisible.value = true
}

const handleStart = async () => {
  if (!startForm.title) {
    ElMessage.warning('请输入审批标题')
    return
  }
  starting.value = true
  try {
    const variables: Record<string, any> = {}
    if (startForm.reason) variables.reason = startForm.reason
    await instanceApi.startProcess({
      processDefinitionKey: currentProcess.processKey!,
      title: startForm.title,
      businessKey: startForm.businessKey || undefined,
      businessType: startForm.businessType || undefined,
      priority: startForm.priority || undefined,
      variables
    })
    ElMessage.success('流程发起成功')
    dialogVisible.value = false
  } catch (error) {
    ElMessage.error('流程发起失败')
  } finally {
    starting.value = false
  }
}
</script>
