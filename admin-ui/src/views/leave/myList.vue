<template>
  <div class="leave-my-list">
    <el-card shadow="never">
      <template #header>
        <span class="card-title">我的请假</span>
      </template>
      <el-table :data="list" v-loading="loading" stripe border>
        <el-table-column prop="title" label="标题" min-width="160" show-overflow-tooltip />
        <el-table-column prop="leaveType" label="类型" width="90" align="center">
          <template #default="scope">
            {{ leaveTypeMap[scope.row.leaveType] || '未知' }}
          </template>
        </el-table-column>
        <el-table-column label="日期范围" min-width="180">
          <template #default="scope">
            {{ scope.row.startDate }} ~ {{ scope.row.endDate }}
          </template>
        </el-table-column>
        <el-table-column prop="days" label="天数" width="80" align="center" />
        <el-table-column prop="status" label="状态" width="90" align="center">
          <template #default="scope">
            <el-tag :type="leaveStatusTagType[scope.row.status]" effect="dark" size="small">
              {{ leaveStatusMap[scope.row.status] || '未知' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="currentNodeName" label="当前节点" width="110" align="center">
          <template #default="scope">
            {{ scope.row.currentNodeName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交时间" width="170" />
        <el-table-column prop="approveComment" label="审批意见" min-width="120" show-overflow-tooltip>
          <template #default="scope">
            {{ scope.row.approveComment || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template #default="scope">
            <el-button v-if="scope.row.processInstanceId" size="small" @click="viewDiagram(scope.row.processInstanceId)">
              <el-icon><View /></el-icon>
              流程图
            </el-button>
            <el-button
              v-if="scope.row.status === 0 || scope.row.status === 1"
              type="warning"
              size="small"
              @click="handleWithdraw(scope.row.id)"
            >
              撤回
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
import { getMyLeaveList, withdrawLeave, leaveTypeMap, leaveStatusMap, leaveStatusTagType, type Leave, type PageResult } from '@/api/leave'
import '@/styles/css/leave/myList.css'

const router = useRouter()
const list = ref<Leave[]>([])
const total = ref(0)
const loading = ref(false)
const pagination = reactive({ pageNum: 1, pageSize: 10 })

onMounted(() => { fetchData() })

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getMyLeaveList({ pageNum: pagination.pageNum, pageSize: pagination.pageSize }) as { data: PageResult<Leave> }
    list.value = res.data?.list || []
    total.value = res.data?.total || 0
  } catch (error: unknown) {
    console.error('获取请假列表失败', error)
  } finally {
    loading.value = false
  }
}

const handleSizeChange = () => {
  pagination.pageNum = 1
  fetchData()
}

const viewDiagram = (processInstanceId: number) => {
  router.push(`/process/diagram/${processInstanceId}`)
}

const handleWithdraw = async (id: number) => {
  try {
    await ElMessageBox.prompt('请输入撤回原因', '撤回请假', {
      confirmButtonText: '确定撤回',
      cancelButtonText: '取消',
      type: 'warning',
      inputPlaceholder: '请输入原因'
    }).then(async ({ value }) => {
      await withdrawLeave(id, value)
      ElMessage.success('已撤回')
      fetchData()
    })
  } catch { /* cancelled */ }
}
</script>
