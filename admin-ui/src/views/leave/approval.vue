<template>
  <div class="leave-approval">
    <el-card shadow="never">
      <template #header>
        <span class="card-title">请假审批</span>
      </template>
      <el-table :data="list" v-loading="loading" stripe border>
        <el-table-column prop="title" label="请假标题" min-width="160" show-overflow-tooltip />
        <el-table-column prop="applicantName" label="申请人" width="100" />
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
        <el-table-column prop="reason" label="原因" min-width="120" show-overflow-tooltip />
        <el-table-column prop="taskName" label="当前节点" width="110" align="center" />
        <el-table-column prop="leaveCreateTime" label="提交时间" width="170" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template #default="scope">
            <el-button type="success" size="small" @click="openDrawer(scope.row)">审批</el-button>
            <el-button type="danger" size="small" @click="openDrawer(scope.row)">驳回</el-button>
            <el-button size="small" @click="openDrawer(scope.row)">详情</el-button>
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

    <LeaveApprovalDrawer
      v-model:visible="drawerVisible"
      :task-id="currentTaskId"
      @approved="onActionCompleted"
      @rejected="onActionCompleted"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import LeaveApprovalDrawer from '@/components/LeaveApprovalDrawer.vue'
import { getLeaveApprovalList, leaveTypeMap, type LeaveApprovalItem, type PageResult } from '@/api/leave'
import '@/styles/css/leave/approval.css'

const list = ref<LeaveApprovalItem[]>([])
const total = ref(0)
const loading = ref(false)
const pagination = reactive({ pageNum: 1, pageSize: 10 })
const drawerVisible = ref(false)
const currentTaskId = ref('')

onMounted(() => { fetchData() })

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getLeaveApprovalList({ pageNum: pagination.pageNum, pageSize: pagination.pageSize }) as { data: PageResult<LeaveApprovalItem> }
    list.value = res.data?.list || []
    total.value = res.data?.total || 0
  } catch {
    // 错误提示由 request 拦截器统一处理
  } finally {
    loading.value = false
  }
}

const handleSizeChange = () => {
  pagination.pageNum = 1
  fetchData()
}

const openDrawer = (row: LeaveApprovalItem) => {
  currentTaskId.value = row.taskId
  drawerVisible.value = true
}

const onActionCompleted = () => {
  fetchData()
}
</script>
