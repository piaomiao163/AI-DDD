<template>
  <div class="bid-score">
    <el-card shadow="never" v-loading="pageLoading">
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span class="card-title">评分 — {{ project?.projectName || '' }}</span>
          <el-button @click="router.back()">返回</el-button>
        </div>
      </template>

      <div style="display:flex;gap:20px;min-height:500px">
        <!-- 左侧：投标人列表 -->
        <div style="width:220px;flex-shrink:0;border-right:1px solid #e4e7ed;padding-right:16px">
          <div style="font-weight:bold;margin-bottom:12px;font-size:14px;color:#606266">投标人列表</div>
          <div v-for="t in tenders" :key="t.id"
            :style="`padding:10px 12px;border-radius:4px;cursor:pointer;margin-bottom:8px;border:1px solid ${selectedTender?.id === t.id ? '#409eff' : '#e4e7ed'};background:${selectedTender?.id === t.id ? '#ecf5ff' : '#fff'}`"
            @click="selectTender(t)">
            <div style="font-size:13px;font-weight:bold;color:#303133">{{ t.vendorName || `投标人${t.id}` }}</div>
            <div style="font-size:12px;color:#909399;margin-top:4px">报价：{{ t.totalPrice?.toLocaleString() || '-' }} 万元</div>
            <el-tag v-if="scoredMap[t.id]" type="success" size="small" style="margin-top:4px">已评分</el-tag>
          </div>
          <el-empty v-if="tenders.length === 0" description="暂无投标人" :image-size="60" />
        </div>

        <!-- 右侧：评分表 -->
        <div style="flex:1">
          <template v-if="selectedTender">
            <div style="font-weight:bold;font-size:15px;margin-bottom:16px">
              正在评分：{{ selectedTender.vendorName || `投标人${selectedTender.id}` }}
            </div>

            <el-alert v-if="currentEval && currentEval.submitted"
              title="您已提交该投标人的评分，无法修改" type="success" show-icon :closable="false"
              style="margin-bottom:16px"
            />

            <el-form :disabled="!!(currentEval && currentEval.submitted)">
              <!-- 评分项 -->
              <el-table :data="scoreItems" border style="margin-bottom:16px">
                <el-table-column prop="itemName" label="评分项" min-width="150" />
                <el-table-column prop="maxScore" label="满分" width="80" align="center" />
                <el-table-column label="得分" width="160" align="center">
                  <template #default="{ row }">
                    <el-input-number
                      v-model="row.score"
                      :min="0"
                      :max="row.maxScore"
                      :precision="1"
                      size="small"
                      style="width:120px"
                      :disabled="!!(currentEval && currentEval.submitted)"
                    />
                  </template>
                </el-table-column>
              </el-table>

              <div style="margin-bottom:8px;font-weight:bold;font-size:14px">综合评价意见</div>
              <el-input
                v-model="comment"
                type="textarea"
                :rows="4"
                placeholder="请输入综合评价意见"
                :disabled="!!(currentEval && currentEval.submitted)"
                style="margin-bottom:16px"
              />

              <div v-if="!(currentEval && currentEval.submitted)" style="display:flex;gap:8px">
                <el-button :loading="saving" @click="handleSave">保存草稿</el-button>
                <el-button type="primary" :loading="submitting" @click="handleSubmit">提交评分</el-button>
              </div>
            </el-form>
          </template>
          <div v-else style="text-align:center;padding:80px;color:#909399">请从左侧选择投标人进行评分</div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getBidProjectById, getTendersByProject, getMyEvaluation, saveEvaluation, submitEvaluation
} from '@/api/bid'

const router = useRouter()
const route = useRoute()
const projectId = Number(route.params.projectId)

const pageLoading = ref(false)
const saving = ref(false)
const submitting = ref(false)
const project = ref<any>(null)
const tenders = ref<any[]>([])
const selectedTender = ref<any>(null)
const currentEval = ref<any>(null)
const scoredMap = reactive<Record<number, boolean>>({})
const comment = ref('')

// 默认评分项模板（实际应从后端加载模板）
const defaultItems = [
  { itemName: '技术方案', maxScore: 30, score: 0 },
  { itemName: '商务条件', maxScore: 20, score: 0 },
  { itemName: '价格', maxScore: 40, score: 0 },
  { itemName: '业绩与信誉', maxScore: 10, score: 0 }
]
const scoreItems = ref<any[]>([])

// 当前专家ID（实际从 store/user 获取，这里用占位符）
const expertId = ref(0)

onMounted(async () => {
  pageLoading.value = true
  try {
    const [projRes, tendersRes] = await Promise.all([
      getBidProjectById(projectId) as any,
      getTendersByProject(projectId) as any
    ])
    project.value = projRes.data
    tenders.value = tendersRes.data || []
  } catch {
    ElMessage.error('加载数据失败')
  } finally {
    pageLoading.value = false
  }
})

const selectTender = async (tender: any) => {
  selectedTender.value = tender
  comment.value = ''
  scoreItems.value = defaultItems.map(i => ({ ...i }))
  try {
    const res = await getMyEvaluation(tender.id, expertId.value) as any
    currentEval.value = res.data
    if (res.data) {
      comment.value = res.data.comment || ''
      scoredMap[tender.id] = true
      if (res.data.items?.length) {
        scoreItems.value = res.data.items.map((item: any) => ({ ...item }))
      }
    } else {
      currentEval.value = null
      scoredMap[tender.id] = false
    }
  } catch {
    currentEval.value = null
  }
}

const buildData = () => ({
  id: currentEval.value?.id || null,
  projectId,
  tenderId: selectedTender.value.id,
  expertId: expertId.value,
  totalScore: scoreItems.value.reduce((s, i) => s + (i.score || 0), 0),
  comment: comment.value,
  items: scoreItems.value
})

const handleSave = async () => {
  if (!selectedTender.value) return
  saving.value = true
  try {
    const res = await saveEvaluation(buildData()) as any
    if (!currentEval.value) currentEval.value = res.data
    else currentEval.value.id = res.data?.id || currentEval.value.id
    ElMessage.success('保存成功')
  } catch {} finally {
    saving.value = false
  }
}

const handleSubmit = async () => {
  if (!selectedTender.value) return
  await ElMessageBox.confirm('提交后不可修改，确认提交评分？', '警告', { type: 'warning' })
  submitting.value = true
  try {
    let id = currentEval.value?.id
    if (!id) {
      const res = await saveEvaluation(buildData()) as any
      id = res.data?.id || res.data
    }
    if (id) {
      await submitEvaluation(id)
      ElMessage.success('评分已提交')
      scoredMap[selectedTender.value.id] = true
      await selectTender(selectedTender.value)
    }
  } catch {} finally {
    submitting.value = false
  }
}
</script>
