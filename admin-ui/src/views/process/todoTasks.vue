<template>
  <div class="todo-tasks">
    <el-card shadow="never">
      <template #header>
        <span class="card-title">任务管理</span>
      </template>
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="待办任务" name="todo" />
        <el-tab-pane label="可认领" name="claimable" />
        <el-tab-pane label="已办任务" name="done" />
      </el-tabs>

      <el-table :data="taskList" v-loading="loading" stripe border>
        <el-table-column prop="taskName" label="任务名称" min-width="130" />
        <el-table-column prop="processName" label="流程名称" min-width="130" />
        <el-table-column prop="assignee" label="处理人" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column prop="priority" label="优先级" width="80" align="center">
        <template #default="scope">
            <el-tag size="small" :type="scope.row.priority > 5 ? 'danger' : scope.row.priority > 3 ? 'warning' : 'info'">
              {{ scope.row.priority }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="260" align="center" fixed="right">
          <template #default="scope">
            <template v-if="activeTab === 'todo'">
              <el-button type="success" size="small" @click="handleComplete(scope.row)">审批</el-button>
              <el-button type="danger" size="small" @click="handleReject(scope.row)">驳回</el-button>
              <el-button size="small" @click="handleDelegate(scope.row)">委派</el-button>
              <el-button v-if="scope.row.businessType === 'leave'" size="small" @click="openLeaveDrawer(scope.row)">详情</el-button>
            </template>
            <template v-else-if="activeTab === 'claimable'">
              <el-button type="primary" size="small" @click="handleClaim(scope.row.taskId)">认领</el-button>
            </template>
            <template v-else>
              <el-tag type="info" size="small">已处理</el-tag>
            </template>
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

    <!-- 请假审批抽屉 -->
    <LeaveApprovalDrawer
      v-model:visible="leaveDrawerVisible"
      :task-id="leaveDrawerTaskId"
      @approved="onLeaveDrawerAction"
      @rejected="onLeaveDrawerAction"
    />

    <!-- 审批对话框 -->
    <el-dialog v-model="completeDialogVisible" title="审批确认" width="400px">
      <p>确认审批通过任务: <strong>{{ currentTask.taskName }}</strong> ?</p>
      <el-input v-model="completeComment" type="textarea" :rows="2" placeholder="审批意见（可选）" />
      <template #footer>
        <el-button @click="completeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="doComplete" :loading="submitting">确认通过</el-button>
      </template>
    </el-dialog>

    <!-- 驳回对话框 -->
    <el-dialog v-model="rejectDialogVisible" title="驳回任务" width="400px">
      <el-form :model="rejectForm" label-width="80px">
        <el-form-item label="驳回原因" required>
          <el-input v-model="rejectForm.reason" type="textarea" :rows="3" placeholder="请输入驳回原因" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rejectDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="doReject" :loading="submitting">确认驳回</el-button>
      </template>
    </el-dialog>

    <!-- 委派对话框 -->
    <el-dialog v-model="delegateDialogVisible" title="委派任务" width="400px">
      <el-form label-width="80px">
        <el-form-item label="委派用户">
          <el-input v-model="delegateUserId" placeholder="请输入目标用户名" clearable />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="delegateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="doDelegate" :loading="submitting">确认委派</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import * as taskApi from '@/api/task'
import type { Task } from '@/api/task'
import LeaveApprovalDrawer from '@/components/LeaveApprovalDrawer.vue'
import '@/styles/css/process/todoTasks.css'

const activeTab = ref('todo')
const taskList = ref<Task[]>([])
const total = ref(0)
const loading = ref(false)
const pagination = reactive({ pageNum: 1, pageSize: 10 })

// 请假审批抽屉
const leaveDrawerVisible = ref(false)
const leaveDrawerTaskId = ref('')

// 对话框状态
const completeDialogVisible = ref(false)
const rejectDialogVisible = ref(false)
const delegateDialogVisible = ref(false)
const submitting = ref(false)

const currentTask = reactive<Partial<Task>>({})
const completeComment = ref('')
const rejectForm = reactive({ reason: '' })
const delegateUserId = ref('')

onMounted(() => { fetchData() })

const fetchData = async () => {
  loading.value = true
  try {
    const data = { pageNum: pagination.pageNum, pageSize: pagination.pageSize }
    let res: any
    if (activeTab.value === 'todo') {
      res = await taskApi.getTodoTasks(data)
    } else if (activeTab.value === 'claimable') {
      res = await taskApi.getClaimableTasks(data)
    } else {
      res = await taskApi.getDoneTasks(data)
    }
    taskList.value = res.data?.list || []
    total.value = res.data?.total || 0
  } catch (error) {
    ElMessage.error('获取任务列表失败')
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

const handleComplete = (task: Task) => {
  // 请假类型任务使用抽屉展示详情
  if (task.businessType === 'leave') {
    openLeaveDrawer(task)
    return
  }
  Object.assign(currentTask, task)
  completeComment.value = ''
  completeDialogVisible.value = true
}

const doComplete = async () => {
  submitting.value = true
  try {
    const variables: Record<string, any> = { outcome: 'approved' }
    if (completeComment.value) variables.comment = completeComment.value
    await taskApi.completeTask(currentTask.taskId!, variables)
    ElMessage.success('审批成功')
    completeDialogVisible.value = false
    fetchData()
  } catch (error) {
    ElMessage.error('审批失败')
  } finally {
    submitting.value = false
  }
}

const handleReject = (task: Task) => {
  // 请假类型任务使用抽屉展示详情
  if (task.businessType === 'leave') {
    openLeaveDrawer(task)
    return
  }
  Object.assign(currentTask, task)
  rejectForm.reason = ''
  rejectDialogVisible.value = true
}

const doReject = async () => {
  if (!rejectForm.reason) {
    ElMessage.warning('请输入驳回原因')
    return
  }
  submitting.value = true
  try {
    await taskApi.rejectTask(currentTask.taskId!, currentTask.processInstanceId!, rejectForm.reason)
    ElMessage.success('任务已驳回')
    rejectDialogVisible.value = false
    fetchData()
  } catch (error) {
    ElMessage.error('驳回失败')
  } finally {
    submitting.value = false
  }
}

const handleDelegate = (task: Task) => {
  Object.assign(currentTask, task)
  delegateUserId.value = ''
  delegateDialogVisible.value = true
}

const doDelegate = async () => {
  if (!delegateUserId.value) {
    ElMessage.warning('请输入委派用户')
    return
  }
  submitting.value = true
  try {
    await taskApi.delegateTask(currentTask.taskId!, delegateUserId.value)
    ElMessage.success('委派成功')
    delegateDialogVisible.value = false
    fetchData()
  } catch (error) {
    ElMessage.error('委派失败')
  } finally {
    submitting.value = false
  }
}

const handleClaim = async (taskId: string) => {
  try {
    await taskApi.claimTask(taskId)
    ElMessage.success('认领成功')
    fetchData()
  } catch (error) {
    ElMessage.error('认领失败')
  }
}

const openLeaveDrawer = (task: Task) => {
  leaveDrawerTaskId.value = task.taskId
  leaveDrawerVisible.value = true
}

const onLeaveDrawerAction = () => {
  fetchData()
}
</script>

