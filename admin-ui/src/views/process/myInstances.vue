<template>
  <div class="my-instances">
    <el-card shadow="never">
      <template #header>
        <span class="card-title">我的流程</span>
      </template>
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="运行中" name="running" />
        <el-tab-pane label="已完成" name="history" />
      </el-tabs>

      <el-table :data="instanceList" v-loading="loading" stripe border>
        <el-table-column prop="title" label="审批标题" min-width="180" show-overflow-tooltip />
        <el-table-column prop="processName" label="流程名称" min-width="120" />
        <el-table-column prop="currentNodeName" label="当前节点" width="120">
          <template #default="scope">
            <span v-if="scope.row.currentNodeName">{{ scope.row.currentNodeName }}</span>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="80" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.priority === 2" type="danger" size="small" effect="dark">特急</el-tag>
            <el-tag v-else-if="scope.row.priority === 1" type="warning" size="small" effect="dark">紧急</el-tag>
            <el-tag v-else type="info" size="small">普通</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startUserName" label="发起人" width="100">
          <template #default="scope">
            {{ scope.row.startUserName || scope.row.startUserId }}
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="启动时间" width="170" />
        <el-table-column prop="status" label="状态" width="90" align="center">
          <template #default="scope">
            <el-tag :type="statusTagType(scope.row.status)" effect="dark" size="small">
              {{ statusLabel(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="viewDiagram(scope.row.id)">
              <el-icon><View /></el-icon>
              流程图
            </el-button>
            <el-button
              v-if="scope.row.status === 0"
              type="warning"
              size="small"
              @click="handleWithdraw(scope.row.id)"
            >
              撤回
            </el-button>
            <el-button
              v-if="scope.row.status === 0"
              type="danger"
              size="small"
              @click="handleTerminate(scope.row.id)"
            >
              终止
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.pageNum"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="fetchData"
          background
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { View } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import * as instanceApi from '@/api/processInstance'
import '@/styles/css/process/myInstances.css'

const router = useRouter()
const activeTab = ref('running')
const instanceList = ref<any[]>([])
const total = ref(0)
const loading = ref(false)
const pagination = reactive({ pageNum: 1, pageSize: 10 })

onMounted(() => { fetchData() })

const fetchData = async () => {
  loading.value = true
  try {
    const data = { pageNum: pagination.pageNum, pageSize: pagination.pageSize }
    const res = activeTab.value === 'running'
      ? await instanceApi.getRunningInstances(data)
      : await instanceApi.getHistoryInstances(data)
    instanceList.value = res.data?.list || []
    total.value = res.data?.total || 0
  } catch (error) {
    ElMessage.error('获取流程实例失败')
  } finally {
    loading.value = false
  }
}

const handleTabChange = () => {
  pagination.pageNum = 1
  fetchData()
}

const handleSizeChange = () => {
  pagination.pageNum = 1
  fetchData()
}

const statusLabel = (status: number) => {
  const map: Record<number, string> = { 0: '运行中', 1: '已完成', 2: '已终止', 3: '已撤回' }
  return map[status] || '未知'
}

const statusTagType = (status: number) => {
  const map: Record<number, string> = { 0: 'primary', 1: 'success', 2: 'danger', 3: 'warning' }
  return map[status] || 'info'
}

const viewDiagram = (id: number) => {
  router.push(`/process/diagram/${id}`)
}

const handleWithdraw = async (id: number) => {
  try {
    await ElMessageBox.prompt('请输入撤回原因', '撤回流程', {
      confirmButtonText: '确定撤回',
      cancelButtonText: '取消',
      type: 'warning',
      inputPlaceholder: '请输入原因'
    }).then(async ({ value }) => {
      await instanceApi.withdrawInstance(id, value)
      ElMessage.success('流程已撤回')
      fetchData()
    })
  } catch { /* cancelled */ }
}

const handleTerminate = async (id: number) => {
  try {
    await ElMessageBox.prompt('请输入终止原因', '终止流程', {
      confirmButtonText: '确定终止',
      cancelButtonText: '取消',
      type: 'warning',
      inputPlaceholder: '请输入原因'
    }).then(async ({ value }) => {
      await instanceApi.terminateInstance(id, value)
      ElMessage.success('流程已终止')
      fetchData()
    })
  } catch { /* cancelled */ }
}
</script>
