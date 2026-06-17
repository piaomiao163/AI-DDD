<template>
  <el-drawer v-model="drawerVisible" title="请假审批详情" size="520px" direction="rtl" :before-close="handleClose">
    <div class="leave-approval-drawer" v-loading="loading">
      <template v-if="detail">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="标题">{{ detail.title }}</el-descriptions-item>
          <el-descriptions-item label="请假类型">{{ leaveTypeMap[detail.leaveType] || '未知' }}</el-descriptions-item>
          <el-descriptions-item label="开始日期">{{ detail.startDate }}</el-descriptions-item>
          <el-descriptions-item label="结束日期">{{ detail.endDate }}</el-descriptions-item>
          <el-descriptions-item label="请假天数">{{ detail.days }}天</el-descriptions-item>
          <el-descriptions-item label="请假原因">{{ detail.reason }}</el-descriptions-item>
          <el-descriptions-item label="申请人">{{ detail.applicantName }}</el-descriptions-item>
          <el-descriptions-item label="所属部门">{{ detail.deptName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="当前状态">
            <el-tag :type="leaveStatusTagType[detail.status]" effect="dark" size="small">
              {{ leaveStatusMap[detail.status] || '未知' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="当前节点">{{ detail.currentNodeName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="提交时间">{{ detail.leaveCreateTime }}</el-descriptions-item>
        </el-descriptions>

        <div class="diagram-section" v-if="detail.processInstanceId">
          <h4>流程图</h4>
          <div class="diagram-loading" v-if="diagramLoading">
            <el-icon class="is-loading" :size="24"><Loading /></el-icon>
            <span>加载中...</span>
          </div>
          <img v-else-if="diagramUrl" :src="diagramUrl" alt="流程图" class="diagram-image" />
        </div>

        <div class="approval-actions" v-if="detail.taskId && detail.status === 1">
          <el-divider />
          <el-input
            v-model="approveComment"
            type="textarea"
            :rows="3"
            placeholder="审批意见（可选）"
            maxlength="200"
            show-word-limit
          />
          <div class="action-buttons">
            <el-button type="success" @click="handleApprove" :loading="submitting">审批通过</el-button>
            <el-button type="danger" @click="rejectDialogVisible = true">驳回</el-button>
          </div>
        </div>
      </template>

      <el-empty v-else-if="!loading" description="无法加载请假详情" />
    </div>

    <el-dialog v-model="rejectDialogVisible" title="驳回原因" width="400px" append-to-body>
      <el-input
        v-model="rejectReason"
        type="textarea"
        :rows="3"
        placeholder="请输入驳回原因（必填）"
        maxlength="200"
        show-word-limit
      />
      <template #footer>
        <el-button @click="rejectDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="doReject" :loading="submitting" :disabled="!rejectReason.trim()">确认驳回</el-button>
      </template>
    </el-dialog>
  </el-drawer>
</template>

<script setup lang="ts">
import { ref, watch, computed, onBeforeUnmount } from 'vue'
import { Loading } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'
import { getLeaveApprovalDetailByTaskId, leaveTypeMap, leaveStatusMap, leaveStatusTagType } from '@/api/leave'
import { completeTask, rejectTask } from '@/api/task'
import type { LeaveApprovalDetail } from '@/api/leave'
import '@/styles/css/components/leaveApprovalDrawer.css'

const props = defineProps<{
  visible: boolean
  taskId: string
}>()

const emit = defineEmits<{
  (e: 'update:visible', val: boolean): void
  (e: 'approved'): void
  (e: 'rejected'): void
}>()

const drawerVisible = computed({
  get: () => props.visible,
  set: (val: boolean) => emit('update:visible', val)
})

const detail = ref<LeaveApprovalDetail | null>(null)
const loading = ref(false)
const approveComment = ref('')
const rejectReason = ref('')
const rejectDialogVisible = ref(false)
const submitting = ref(false)
const diagramUrl = ref('')
const diagramLoading = ref(false)

watch(() => props.taskId, (newVal) => {
  if (newVal && props.visible) {
    loadDetail(newVal)
  }
})

watch(() => props.visible, (newVal) => {
  if (newVal && props.taskId) {
    loadDetail(props.taskId)
  }
  if (!newVal) {
    detail.value = null
    approveComment.value = ''
    rejectReason.value = ''
  }
})

const loadDetail = async (taskId: string) => {
  loading.value = true
  try {
    const res: any = await getLeaveApprovalDetailByTaskId(taskId)
    detail.value = res.data || res
    if (detail.value?.processInstanceId) {
      loadDiagram(detail.value.processInstanceId)
    }
  } catch (e: any) {
    ElMessage.error('加载请假详情失败')
  } finally {
    loading.value = false
  }
}

const loadDiagram = async (processInstanceId: number) => {
  diagramLoading.value = true
  try {
    const response = await request.get(`/process-diagram/by-business-id/${processInstanceId}`, {
      responseType: 'blob'
    })
    const blob = new Blob([response as any], { type: 'image/svg+xml' })
    diagramUrl.value = URL.createObjectURL(blob)
  } catch {
    // 流程图加载失败不影响主流程
  } finally {
    diagramLoading.value = false
  }
}

const handleApprove = async () => {
  if (!detail.value?.taskId) return
  submitting.value = true
  try {
    const variables: Record<string, string> = { outcome: 'approved' }
    if (approveComment.value.trim()) {
      variables.comment = approveComment.value.trim()
    }
    await completeTask(detail.value.taskId, { variables })
    ElMessage.success('审批通过')
    emit('approved')
    drawerVisible.value = false
  } catch {
    ElMessage.error('审批失败')
  } finally {
    submitting.value = false
  }
}

const doReject = async () => {
  if (!detail.value?.taskId || !rejectReason.value.trim()) return
  submitting.value = true
  try {
    await rejectTask(detail.value.taskId, detail.value.actProcessInstanceId, rejectReason.value.trim())
    ElMessage.success('已驳回')
    emit('rejected')
    rejectDialogVisible.value = false
    drawerVisible.value = false
  } catch {
    ElMessage.error('驳回失败')
  } finally {
    submitting.value = false
  }
}

const handleClose = () => {
  drawerVisible.value = false
}

onBeforeUnmount(() => {
  if (diagramUrl.value) {
    URL.revokeObjectURL(diagramUrl.value)
  }
})
</script>
