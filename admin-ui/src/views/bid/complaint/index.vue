<template>
  <div class="bid-complaint">
    <el-card shadow="never">
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span class="card-title">质疑投诉管理</span>
          <el-button type="primary" @click="submitDialog.visible = true">提交质疑</el-button>
        </div>
      </template>

      <el-table :data="list" v-loading="loading" stripe border>
        <el-table-column prop="projectName" label="项目" min-width="180" show-overflow-tooltip />
        <el-table-column prop="type" label="类型" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="row.type === 2 ? 'danger' : 'warning'" size="small">{{ row.type === 2 ? '投诉' : '质疑' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="150" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 2 ? 'success' : row.status === 3 ? 'info' : 'warning'" size="small">
              {{ complaintStatusMap[row.status] || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="submitterName" label="提交人" width="100" />
        <el-table-column prop="createTime" label="提交时间" width="170" />
        <el-table-column prop="replyContent" label="回复" min-width="150" show-overflow-tooltip>
          <template #default="{ row }">{{ row.replyContent || '-' }}</template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" v-if="row.status === 0 || row.status === 1" @click="openReplyDialog(row)">回复</el-button>
            <el-button size="small" type="info" v-if="row.status !== 3" @click="handleClose(row.id)">关闭</el-button>
            <el-button size="small" type="warning" v-if="row.type === 1 && row.status === 1 && row.isMine" @click="handleEscalate(row.id)">升级投诉</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="display:flex;justify-content:flex-end;margin-top:16px">
        <el-pagination
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          :total="total"
          @size-change="() => { queryParams.pageNum = 1; fetchData() }"
          @current-change="fetchData"
          background
        />
      </div>
    </el-card>

    <!-- 提交质疑对话框 -->
    <el-dialog v-model="submitDialog.visible" title="提交质疑" width="500px" @close="resetSubmitForm">
      <el-form ref="submitFormRef" :model="submitDialog.form" :rules="submitRules" label-width="100px">
        <el-form-item label="项目ID" prop="projectId">
          <el-input-number v-model="submitDialog.form.projectId" :min="1" style="width:100%" />
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="submitDialog.form.title" clearable />
        </el-form-item>
        <el-form-item label="质疑内容" prop="content">
          <el-input v-model="submitDialog.form.content" type="textarea" :rows="5" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="submitDialog.visible = false">取消</el-button>
        <el-button type="primary" :loading="submitDialog.loading" @click="handleSubmitComplaint">提交</el-button>
      </template>
    </el-dialog>

    <!-- 回复对话框 -->
    <el-dialog v-model="replyDialog.visible" title="回复质疑" width="480px">
      <el-input v-model="replyDialog.content" type="textarea" :rows="5" placeholder="请输入回复内容" />
      <template #footer>
        <el-button @click="replyDialog.visible = false">取消</el-button>
        <el-button type="primary" :loading="replyDialog.loading" @click="handleReply">提交回复</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { getComplaintPage, saveComplaint, replyComplaint, closeComplaint, escalateComplaint } from '@/api/bid'

const complaintStatusMap: Record<number, string> = { 0: '待处理', 1: '处理中', 2: '已回复', 3: '已关闭', 4: '已升级' }

const list = ref<any[]>([])
const total = ref(0)
const loading = ref(false)
const queryParams = reactive({ pageNum: 1, pageSize: 10 })

const submitFormRef = ref<FormInstance>()
const submitDialog = reactive({
  visible: false, loading: false,
  form: { projectId: 0, title: '', content: '' }
})
const submitRules: FormRules = {
  projectId: [{ required: true, type: 'number', min: 1, message: '请输入项目ID', trigger: 'blur' }],
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入质疑内容', trigger: 'blur' }]
}

const replyDialog = reactive({ visible: false, id: 0, content: '', loading: false })

onMounted(fetchData)

async function fetchData() {
  loading.value = true
  try {
    const res = await getComplaintPage({ ...queryParams }) as any
    list.value = res.data?.list || []
    total.value = res.data?.total || 0
  } catch {} finally {
    loading.value = false
  }
}

const resetSubmitForm = () => {
  submitDialog.form.projectId = 0; submitDialog.form.title = ''; submitDialog.form.content = ''
  submitFormRef.value?.clearValidate()
}

const handleSubmitComplaint = async () => {
  if (!submitFormRef.value) return
  try { await submitFormRef.value.validate() } catch { return }
  submitDialog.loading = true
  try {
    await saveComplaint({ ...submitDialog.form, type: 1 })
    ElMessage.success('质疑已提交')
    submitDialog.visible = false
    fetchData()
  } catch {} finally { submitDialog.loading = false }
}

const openReplyDialog = (row: any) => {
  replyDialog.id = row.id; replyDialog.content = ''; replyDialog.visible = true
}
const handleReply = async () => {
  if (!replyDialog.content) { ElMessage.warning('请输入回复内容'); return }
  replyDialog.loading = true
  try {
    await replyComplaint(replyDialog.id, replyDialog.content)
    ElMessage.success('回复成功')
    replyDialog.visible = false
    fetchData()
  } catch {} finally { replyDialog.loading = false }
}

const handleClose = async (id: number) => {
  await ElMessageBox.confirm('确认关闭该质疑/投诉？', '提示', { type: 'warning' })
  try { await closeComplaint(id); ElMessage.success('已关闭'); fetchData() } catch {}
}
const handleEscalate = async (id: number) => {
  await ElMessageBox.confirm('确认将质疑升级为投诉？', '提示', { type: 'warning' })
  try { await escalateComplaint(id); ElMessage.success('已升级为投诉'); fetchData() } catch {}
}
</script>
