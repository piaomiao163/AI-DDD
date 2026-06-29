<template>
  <div class="bid-project-detail">
    <el-card shadow="never" v-loading="loading">
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span class="card-title">项目详情</span>
          <el-button @click="router.back()">返回</el-button>
        </div>
      </template>

      <template v-if="project">
        <!-- 基本信息 -->
        <el-descriptions title="基本信息" :column="2" border>
          <el-descriptions-item label="项目名称">{{ project.projectName }}</el-descriptions-item>
          <el-descriptions-item label="采购类型">{{ purchaseTypeMap[project.purchaseType] || '-' }}</el-descriptions-item>
          <el-descriptions-item label="招标方式">{{ bidMethodMap[project.bidMethod] || '-' }}</el-descriptions-item>
          <el-descriptions-item label="预算金额">{{ project.budgetAmount?.toLocaleString() }} 万元</el-descriptions-item>
          <el-descriptions-item label="资金来源">{{ project.fundingSource || '-' }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="(projectStatusTagMap[project.status] as any)" effect="dark" size="small">
              {{ projectStatusMap[project.status] || '未知' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="评标方法">{{ evalMethodMap[project.evalMethod] || '-' }}</el-descriptions-item>
          <el-descriptions-item label="专家人数">{{ project.expertCount || '-' }}</el-descriptions-item>
          <el-descriptions-item label="保证金">{{ project.depositAmount?.toLocaleString() || '-' }} 万元</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ project.createTime }}</el-descriptions-item>
          <el-descriptions-item label="项目描述" :span="2">{{ project.projectDesc || '-' }}</el-descriptions-item>
        </el-descriptions>

        <el-descriptions title="时间安排" :column="2" border style="margin-top:20px">
          <el-descriptions-item label="公告开始时间">{{ project.announcementStartTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="报名截止时间">{{ project.registrationDeadline || '-' }}</el-descriptions-item>
          <el-descriptions-item label="答疑截止时间">{{ project.clarificationDeadline || '-' }}</el-descriptions-item>
          <el-descriptions-item label="投标截止时间">{{ project.biddingDeadline || '-' }}</el-descriptions-item>
          <el-descriptions-item label="评标完成时间">{{ project.evaluationDeadline || '-' }}</el-descriptions-item>
        </el-descriptions>

        <!-- 招标公告 -->
        <div v-if="project.announcementContent" style="margin-top:20px">
          <div style="font-weight:bold;margin-bottom:8px;font-size:14px">招标公告</div>
          <div style="background:#f5f7fa;padding:16px;border-radius:4px;white-space:pre-wrap;font-size:14px;line-height:1.8">
            {{ project.announcementContent }}
          </div>
        </div>

        <!-- 状态流转操作 -->
        <div style="margin-top:20px;display:flex;gap:8px;flex-wrap:wrap">
          <el-button type="warning" v-if="project.status === 0" @click="handleSubmit">提交审核</el-button>
          <el-button type="success" v-if="project.status === 1" @click="handlePublish">发布</el-button>
          <el-button type="primary" v-if="project.status === 3" @click="handleOpen">触发开标</el-button>
          <el-button type="primary" v-if="project.status === 5" @click="handleEvaluate">开始评标</el-button>
          <el-button type="primary" v-if="project.status === 6" @click="publicizeDialogVisible = true">发起公示</el-button>
          <el-button type="success" v-if="project.status === 7" @click="awardDialogVisible = true">定标</el-button>
        </div>

        <!-- 报名列表 -->
        <div style="margin-top:24px">
          <div style="font-weight:bold;margin-bottom:12px;font-size:15px">报名列表</div>
          <el-table :data="registrations" border stripe size="small">
            <el-table-column prop="vendorName" label="供应商名称" min-width="150" />
            <el-table-column prop="contactName" label="联系人" width="100" />
            <el-table-column prop="contactPhone" label="联系电话" width="130" />
            <el-table-column prop="status" label="状态" width="90" align="center">
              <template #default="{ row }">
                <el-tag :type="row.status === 1 ? 'success' : row.status === 2 ? 'danger' : 'warning'" size="small">
                  {{ registrationStatusMap[row.status] || '-' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="报名时间" width="170" />
            <el-table-column label="操作" width="160" align="center">
              <template #default="{ row }">
                <template v-if="row.status === 0">
                  <el-button size="small" type="success" @click="handleApprove(row.id)">通过</el-button>
                  <el-button size="small" type="danger" @click="handleReject(row.id)">拒绝</el-button>
                </template>
                <span v-else>-</span>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 投标书列表（开标后展示） -->
        <div v-if="project.status >= 5" style="margin-top:24px">
          <div style="font-weight:bold;margin-bottom:12px;font-size:15px">投标书列表</div>
          <el-table :data="tenders" border stripe size="small">
            <el-table-column prop="id" label="投标书ID" width="90" />
            <el-table-column prop="vendorName" label="投标人" min-width="150" />
            <el-table-column prop="totalPrice" label="报价(万元)" width="120" align="right">
              <template #default="{ row }">{{ row.totalPrice?.toLocaleString() || '-' }}</template>
            </el-table-column>
            <el-table-column prop="includeTax" label="是否含税" width="90" align="center">
              <template #default="{ row }">{{ row.includeTax ? '是' : '否' }}</template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="110" align="center">
              <template #default="{ row }">
                <el-tag :type="row.status === 4 ? 'success' : row.status === 5 ? 'danger' : ''" size="small">
                  {{ tenderStatusMap[row.status] || '-' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 评分汇总（评标后展示） -->
        <div v-if="project.status >= 6 && evaluations.length" style="margin-top:24px">
          <div style="font-weight:bold;margin-bottom:12px;font-size:15px">评分汇总</div>
          <el-table :data="evaluations" border stripe size="small">
            <el-table-column prop="expertName" label="专家" width="100" />
            <el-table-column prop="tenderVendorName" label="投标人" min-width="150" />
            <el-table-column prop="totalScore" label="总分" width="80" align="center" />
            <el-table-column prop="comment" label="综合评价" min-width="200" show-overflow-tooltip />
            <el-table-column prop="submitted" label="状态" width="90" align="center">
              <template #default="{ row }">
                <el-tag :type="row.submitted ? 'success' : 'warning'" size="small">{{ row.submitted ? '已提交' : '草稿' }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </template>
    </el-card>

    <!-- 拒绝原因对话框 -->
    <el-dialog v-model="rejectDialog.visible" title="拒绝原因" width="400px">
      <el-input v-model="rejectDialog.reason" type="textarea" :rows="3" placeholder="请输入拒绝原因" />
      <template #footer>
        <el-button @click="rejectDialog.visible = false">取消</el-button>
        <el-button type="danger" :loading="rejectDialog.loading" @click="confirmReject">确认拒绝</el-button>
      </template>
    </el-dialog>

    <!-- 公示天数 -->
    <el-dialog v-model="publicizeDialogVisible" title="发起公示" width="360px">
      <el-form label-width="90px">
        <el-form-item label="公示天数">
          <el-input-number v-model="publicizeDays" :min="1" :max="30" style="width:100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="publicizeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handlePublicize">确认</el-button>
      </template>
    </el-dialog>

    <!-- 定标 -->
    <el-dialog v-model="awardDialogVisible" title="定标" width="360px">
      <el-form label-width="120px">
        <el-form-item label="中标投标书ID">
          <el-input-number v-model="awardTenderId" :min="1" style="width:100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="awardDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAward">确认定标</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getBidProjectById, getRegistrationByProject, getTendersByProject, getEvaluationByProject,
  submitBidProject, publishBidProject, openBidding, startEvaluation, publicizeBidProject, awardBid,
  approveRegistration, rejectRegistration,
  purchaseTypeMap, bidMethodMap, projectStatusMap, projectStatusTagMap, evalMethodMap,
  registrationStatusMap, tenderStatusMap
} from '@/api/bid'

const router = useRouter()
const route = useRoute()
const projectId = Number(route.params.id)
const loading = ref(false)
const project = ref<any>(null)
const registrations = ref<any[]>([])
const tenders = ref<any[]>([])
const evaluations = ref<any[]>([])

const rejectDialog = reactive({ visible: false, id: 0, reason: '', loading: false })
const publicizeDialogVisible = ref(false)
const publicizeDays = ref(7)
const awardDialogVisible = ref(false)
const awardTenderId = ref(0)

import { reactive } from 'vue'

const loadAll = async () => {
  loading.value = true
  try {
    const res = await getBidProjectById(projectId) as any
    project.value = res.data
    const regRes = await getRegistrationByProject(projectId) as any
    registrations.value = regRes.data || []
    if (project.value?.status >= 5) {
      const tRes = await getTendersByProject(projectId) as any
      tenders.value = tRes.data || []
    }
    if (project.value?.status >= 6) {
      const eRes = await getEvaluationByProject(projectId) as any
      evaluations.value = eRes.data || []
    }
  } catch {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

onMounted(loadAll)

const handleSubmit = async () => {
  await ElMessageBox.confirm('确认提交审核？', '提示', { type: 'warning' })
  try { await submitBidProject(projectId); ElMessage.success('提交成功'); loadAll() } catch {}
}
const handlePublish = async () => {
  await ElMessageBox.confirm('确认发布？', '提示', { type: 'warning' })
  try { await publishBidProject(projectId); ElMessage.success('发布成功'); loadAll() } catch {}
}
const handleOpen = async () => {
  await ElMessageBox.confirm('确认触发开标？', '提示', { type: 'warning' })
  try { await openBidding(projectId); ElMessage.success('开标成功'); loadAll() } catch {}
}
const handleEvaluate = async () => {
  await ElMessageBox.confirm('确认开始评标？', '提示', { type: 'warning' })
  try { await startEvaluation(projectId); ElMessage.success('评标已开始'); loadAll() } catch {}
}
const handlePublicize = async () => {
  try { await publicizeBidProject(projectId, publicizeDays.value); ElMessage.success('公示已发起'); publicizeDialogVisible.value = false; loadAll() } catch {}
}
const handleAward = async () => {
  if (!awardTenderId.value) { ElMessage.warning('请输入中标投标书ID'); return }
  try { await awardBid(projectId, awardTenderId.value); ElMessage.success('定标成功'); awardDialogVisible.value = false; loadAll() } catch {}
}
const handleApprove = async (id: number) => {
  await ElMessageBox.confirm('确认通过该报名？', '提示', { type: 'warning' })
  try { await approveRegistration(id); ElMessage.success('已通过'); loadAll() } catch {}
}
const handleReject = (id: number) => {
  rejectDialog.id = id; rejectDialog.reason = ''; rejectDialog.visible = true
}
const confirmReject = async () => {
  if (!rejectDialog.reason) { ElMessage.warning('请输入拒绝原因'); return }
  rejectDialog.loading = true
  try { await rejectRegistration(rejectDialog.id, rejectDialog.reason); ElMessage.success('已拒绝'); rejectDialog.visible = false; loadAll() }
  catch {} finally { rejectDialog.loading = false }
}
</script>
