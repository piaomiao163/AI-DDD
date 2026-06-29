<template>
  <div class="bid-project-form">
    <el-card shadow="never">
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span class="card-title">{{ isEdit ? '编辑项目' : '新建项目' }}</span>
          <el-button @click="router.back()">返回</el-button>
        </div>
      </template>

      <el-steps :active="currentStep" finish-status="success" style="margin-bottom:32px">
        <el-step title="基本信息" />
        <el-step title="时间安排" />
        <el-step title="评标设置" />
        <el-step title="公告内容" />
      </el-steps>

      <!-- Step 1: 基本信息 -->
      <el-form v-show="currentStep === 0" ref="form1Ref" :model="form" :rules="rules1" label-width="120px">
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model="form.projectName" placeholder="请输入项目名称" clearable />
        </el-form-item>
        <el-form-item label="采购类型" prop="purchaseType">
          <el-select v-model="form.purchaseType" placeholder="请选择" style="width:100%">
            <el-option v-for="(label, value) in purchaseTypeMap" :key="value" :label="label" :value="Number(value)" />
          </el-select>
        </el-form-item>
        <el-form-item label="招标方式" prop="bidMethod">
          <el-select v-model="form.bidMethod" placeholder="请选择" style="width:100%">
            <el-option v-for="(label, value) in bidMethodMap" :key="value" :label="label" :value="Number(value)" />
          </el-select>
        </el-form-item>
        <el-form-item label="预算金额(万元)" prop="budgetAmount">
          <el-input-number v-model="form.budgetAmount" :min="0" :precision="2" style="width:100%" />
        </el-form-item>
        <el-form-item label="资金来源" prop="fundingSource">
          <el-input v-model="form.fundingSource" placeholder="如：财政拨款、自有资金等" />
        </el-form-item>
        <el-form-item label="项目描述" prop="projectDesc">
          <el-input v-model="form.projectDesc" type="textarea" :rows="4" placeholder="请输入项目描述" />
        </el-form-item>
      </el-form>

      <!-- Step 2: 时间安排 -->
      <el-form v-show="currentStep === 1" ref="form2Ref" :model="form" :rules="rules2" label-width="160px">
        <el-form-item label="公告开始时间" prop="announcementStartTime">
          <el-date-picker v-model="form.announcementStartTime" type="datetime" placeholder="选择时间" value-format="YYYY-MM-DD HH:mm:ss" style="width:100%" />
        </el-form-item>
        <el-form-item label="报名截止时间" prop="registrationDeadline">
          <el-date-picker v-model="form.registrationDeadline" type="datetime" placeholder="选择时间" value-format="YYYY-MM-DD HH:mm:ss" style="width:100%" />
        </el-form-item>
        <el-form-item label="答疑截止时间" prop="clarificationDeadline">
          <el-date-picker v-model="form.clarificationDeadline" type="datetime" placeholder="选择时间" value-format="YYYY-MM-DD HH:mm:ss" style="width:100%" />
        </el-form-item>
        <el-form-item label="投标截止时间" prop="biddingDeadline">
          <el-date-picker v-model="form.biddingDeadline" type="datetime" placeholder="选择时间" value-format="YYYY-MM-DD HH:mm:ss" style="width:100%" />
        </el-form-item>
        <el-form-item label="评标预计完成时间" prop="evaluationDeadline">
          <el-date-picker v-model="form.evaluationDeadline" type="datetime" placeholder="选择时间" value-format="YYYY-MM-DD HH:mm:ss" style="width:100%" />
        </el-form-item>
      </el-form>

      <!-- Step 3: 评标设置 -->
      <el-form v-show="currentStep === 2" ref="form3Ref" :model="form" :rules="rules3" label-width="160px">
        <el-form-item label="评标方法" prop="evalMethod">
          <el-select v-model="form.evalMethod" placeholder="请选择" style="width:100%">
            <el-option v-for="(label, value) in evalMethodMap" :key="value" :label="label" :value="Number(value)" />
          </el-select>
        </el-form-item>
        <el-form-item label="专家人数" prop="expertCount">
          <el-input-number v-model="form.expertCount" :min="1" :max="99" style="width:100%" />
        </el-form-item>
        <el-form-item label="专家抽取方式" prop="expertSelectMethod">
          <el-select v-model="form.expertSelectMethod" placeholder="请选择" style="width:100%">
            <el-option label="随机抽取" :value="1" />
            <el-option label="定向邀请" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="保证金金额(万元)" prop="depositAmount">
          <el-input-number v-model="form.depositAmount" :min="0" :precision="2" style="width:100%" />
        </el-form-item>
      </el-form>

      <!-- Step 4: 公告内容 -->
      <el-form v-show="currentStep === 3" ref="form4Ref" :model="form" :rules="rules4" label-width="120px">
        <el-form-item label="招标公告" prop="announcementContent">
          <el-input v-model="form.announcementContent" type="textarea" :rows="16" placeholder="请输入招标公告内容" />
        </el-form-item>
      </el-form>

      <div style="display:flex;justify-content:space-between;margin-top:24px">
        <el-button v-if="currentStep > 0" @click="currentStep--">上一步</el-button>
        <span v-else />
        <div>
          <el-button :loading="saving" @click="handleSave">保存草稿</el-button>
          <el-button v-if="currentStep < 3" type="primary" @click="handleNext">下一步</el-button>
          <el-button v-else type="primary" :loading="submitting" @click="handleSubmit">提交</el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { getBidProjectById, saveBidProject, updateBidProject, submitBidProject, purchaseTypeMap, bidMethodMap, evalMethodMap } from '@/api/bid'

const router = useRouter()
const route = useRoute()
const projectId = route.params.id ? Number(route.params.id) : null
const isEdit = !!projectId

const currentStep = ref(0)
const saving = ref(false)
const submitting = ref(false)
const form1Ref = ref<FormInstance>()
const form2Ref = ref<FormInstance>()
const form3Ref = ref<FormInstance>()
const form4Ref = ref<FormInstance>()

const form = reactive({
  id: null as number | null,
  projectName: '',
  purchaseType: null as number | null,
  bidMethod: null as number | null,
  budgetAmount: 0,
  fundingSource: '',
  projectDesc: '',
  announcementStartTime: '',
  registrationDeadline: '',
  clarificationDeadline: '',
  biddingDeadline: '',
  evaluationDeadline: '',
  evalMethod: null as number | null,
  expertCount: 3,
  expertSelectMethod: null as number | null,
  depositAmount: 0,
  announcementContent: ''
})

const rules1: FormRules = {
  projectName: [{ required: true, message: '请输入项目名称', trigger: 'blur' }],
  purchaseType: [{ required: true, message: '请选择采购类型', trigger: 'change' }],
  bidMethod: [{ required: true, message: '请选择招标方式', trigger: 'change' }],
  budgetAmount: [{ required: true, message: '请输入预算金额', trigger: 'blur' }]
}
const rules2: FormRules = {
  registrationDeadline: [{ required: true, message: '请选择报名截止时间', trigger: 'change' }],
  biddingDeadline: [{ required: true, message: '请选择投标截止时间', trigger: 'change' }]
}
const rules3: FormRules = {
  evalMethod: [{ required: true, message: '请选择评标方法', trigger: 'change' }],
  expertCount: [{ required: true, message: '请输入专家人数', trigger: 'blur' }]
}
const rules4: FormRules = {
  announcementContent: [{ required: true, message: '请输入招标公告内容', trigger: 'blur' }]
}

onMounted(async () => {
  if (isEdit && projectId) {
    try {
      const res = await getBidProjectById(projectId) as any
      const d = res.data
      Object.assign(form, d)
    } catch {
      ElMessage.error('加载项目信息失败')
    }
  }
})

const getCurrentFormRef = () => [form1Ref, form2Ref, form3Ref, form4Ref][currentStep.value]

const handleNext = async () => {
  const ref = getCurrentFormRef()
  if (!ref.value) return
  try {
    await ref.value.validate()
    currentStep.value++
  } catch {}
}

const buildData = () => ({ ...form })

const handleSave = async () => {
  saving.value = true
  try {
    if (form.id) {
      await updateBidProject(buildData())
    } else {
      const res = await saveBidProject(buildData()) as any
      form.id = res.data?.id || res.data
    }
    ElMessage.success('保存草稿成功')
  } catch {} finally {
    saving.value = false
  }
}

const handleSubmit = async () => {
  const ref = getCurrentFormRef()
  if (ref.value) {
    try { await ref.value.validate() } catch { return }
  }
  submitting.value = true
  try {
    let id = form.id
    if (id) {
      await updateBidProject(buildData())
    } else {
      const res = await saveBidProject(buildData()) as any
      id = res.data?.id || res.data
    }
    if (id) {
      await submitBidProject(id)
      ElMessage.success('提交审核成功')
      router.push('/bid/project')
    }
  } catch {} finally {
    submitting.value = false
  }
}
</script>
