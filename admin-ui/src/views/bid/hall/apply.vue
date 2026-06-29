<template>
  <div class="bid-apply">
    <el-card shadow="never" v-loading="pageLoading">
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span class="card-title">项目报名</span>
          <el-button @click="router.back()">返回</el-button>
        </div>
      </template>

      <!-- 项目信息 -->
      <el-descriptions v-if="project" title="项目信息" :column="2" border style="margin-bottom:24px">
        <el-descriptions-item label="项目名称">{{ project.projectName }}</el-descriptions-item>
        <el-descriptions-item label="采购类型">{{ purchaseTypeMap[project.purchaseType] || '-' }}</el-descriptions-item>
        <el-descriptions-item label="招标方式">{{ bidMethodMap[project.bidMethod] || '-' }}</el-descriptions-item>
        <el-descriptions-item label="预算金额">{{ project.budgetAmount?.toLocaleString() }} 万元</el-descriptions-item>
        <el-descriptions-item label="报名截止">{{ project.registrationDeadline || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="(projectStatusTagMap[project.status] as any)" size="small" effect="dark">
            {{ projectStatusMap[project.status] || '-' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="项目描述" :span="2">{{ project.projectDesc || '-' }}</el-descriptions-item>
      </el-descriptions>

      <!-- 已有报名状态 -->
      <el-alert v-if="myRegistration" style="margin-bottom:20px"
        :title="`您已报名此项目，状态：${registrationStatusMap[myRegistration.status]}`"
        :type="myRegistration.status === 1 ? 'success' : myRegistration.status === 2 ? 'error' : 'warning'"
        show-icon :closable="false"
      />

      <!-- 报名表单（未报名时展示） -->
      <template v-if="!myRegistration">
        <div style="font-weight:bold;margin-bottom:16px;font-size:15px">填写报名信息</div>
        <el-form ref="formRef" :model="form" :rules="rules" label-width="120px" style="max-width:600px">
          <el-form-item label="供应商名称" prop="vendorName">
            <el-input v-model="form.vendorName" placeholder="请输入供应商名称" clearable />
          </el-form-item>
          <el-form-item label="联系人" prop="contactName">
            <el-input v-model="form.contactName" placeholder="请输入联系人姓名" clearable />
          </el-form-item>
          <el-form-item label="联系电话" prop="contactPhone">
            <el-input v-model="form.contactPhone" placeholder="请输入联系电话" clearable />
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="form.email" placeholder="请输入邮箱地址" clearable />
          </el-form-item>
          <el-form-item label="资质材料">
            <div v-for="(_, idx) in form.qualificationFiles" :key="idx" style="display:flex;gap:8px;margin-bottom:8px">
              <el-input v-model="form.qualificationFiles[idx]" placeholder="输入文件路径" />
              <el-button @click="form.qualificationFiles.splice(idx, 1)" type="danger" :icon="Delete" circle />
            </div>
            <el-button @click="form.qualificationFiles.push('')" size="small">+ 添加文件</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="submitting" @click="handleSubmit">提交报名</el-button>
          </el-form-item>
        </el-form>
      </template>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Delete } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'
import {
  getBidProjectById, getMyRegistration, saveRegistration,
  purchaseTypeMap, bidMethodMap, projectStatusMap, projectStatusTagMap, registrationStatusMap
} from '@/api/bid'

const router = useRouter()
const route = useRoute()
const projectId = Number(route.params.projectId)
const pageLoading = ref(false)
const submitting = ref(false)
const project = ref<any>(null)
const myRegistration = ref<any>(null)
const formRef = ref<FormInstance>()

const form = reactive({
  vendorName: '',
  contactName: '',
  contactPhone: '',
  email: '',
  qualificationFiles: [] as string[]
})

const rules: FormRules = {
  vendorName: [{ required: true, message: '请输入供应商名称', trigger: 'blur' }],
  contactName: [{ required: true, message: '请输入联系人', trigger: 'blur' }],
  contactPhone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }]
}

onMounted(async () => {
  pageLoading.value = true
  try {
    const [projRes, regRes] = await Promise.all([
      getBidProjectById(projectId) as any,
      getMyRegistration(projectId) as any
    ])
    project.value = projRes.data
    myRegistration.value = regRes.data
  } catch {} finally {
    pageLoading.value = false
  }
})

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate()
  submitting.value = true
  try {
    await saveRegistration({ ...form, projectId, qualificationFiles: form.qualificationFiles.filter(f => f) })
    ElMessage.success('报名成功，请等待审核')
    const regRes = await getMyRegistration(projectId) as any
    myRegistration.value = regRes.data
  } catch {} finally {
    submitting.value = false
  }
}
</script>
