<template>
  <div class="bid-tender-form">
    <el-card shadow="never" v-loading="pageLoading">
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span class="card-title">投标文件</span>
          <el-button @click="router.back()">返回</el-button>
        </div>
      </template>

      <!-- 项目信息 -->
      <el-descriptions v-if="project" title="项目信息" :column="2" border style="margin-bottom:24px">
        <el-descriptions-item label="项目名称">{{ project.projectName }}</el-descriptions-item>
        <el-descriptions-item label="预算金额">{{ project.budgetAmount?.toLocaleString() }} 万元</el-descriptions-item>
        <el-descriptions-item label="投标截止时间">{{ project.biddingDeadline || '-' }}</el-descriptions-item>
        <el-descriptions-item label="评标方法">{{ evalMethodMap[project.evalMethod] || '-' }}</el-descriptions-item>
      </el-descriptions>

      <!-- 已提交提示 -->
      <el-alert v-if="tender && tender.status === 1"
        title="投标文件已提交，无法修改" type="success" show-icon :closable="false"
        style="margin-bottom:20px"
      />

      <el-form ref="formRef" :model="form" :rules="rules" label-width="140px" style="max-width:700px"
        :disabled="tender && tender.status === 1">
        <el-form-item label="总报价(万元)" prop="totalPrice">
          <el-input-number v-model="form.totalPrice" :min="0" :precision="2" style="width:100%" />
        </el-form-item>
        <el-form-item label="是否含税" prop="includeTax">
          <el-radio-group v-model="form.includeTax">
            <el-radio :label="true">含税</el-radio>
            <el-radio :label="false">不含税</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="技术标文件路径" prop="techFilePath">
          <el-input v-model="form.techFilePath" placeholder="请输入技术标文件路径" clearable />
        </el-form-item>
        <el-form-item label="商务标文件路径" prop="commercialFilePath">
          <el-input v-model="form.commercialFilePath" placeholder="请输入商务标文件路径" clearable />
        </el-form-item>
        <el-form-item label="报价文件路径" prop="priceFilePath">
          <el-input v-model="form.priceFilePath" placeholder="请输入报价文件路径" clearable />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="选填" />
        </el-form-item>
        <el-form-item v-if="!(tender && tender.status === 1)">
          <el-button :loading="saving" @click="handleSave">保存草稿</el-button>
          <el-button type="primary" :loading="submitting" @click="handleSubmit">提交投标</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { getBidProjectById, getMyTender, saveTender, submitTender, evalMethodMap } from '@/api/bid'

const router = useRouter()
const route = useRoute()
const projectId = Number(route.params.projectId)
const pageLoading = ref(false)
const saving = ref(false)
const submitting = ref(false)
const formRef = ref<FormInstance>()
const project = ref<any>(null)
const tender = ref<any>(null)

const form = reactive({
  id: null as number | null,
  totalPrice: 0,
  includeTax: true,
  techFilePath: '',
  commercialFilePath: '',
  priceFilePath: '',
  remark: ''
})

const rules: FormRules = {
  totalPrice: [{ required: true, message: '请输入总报价', trigger: 'blur' }],
  includeTax: [{ required: true, message: '请选择是否含税', trigger: 'change' }]
}

onMounted(async () => {
  pageLoading.value = true
  try {
    const [projRes, tenderRes] = await Promise.all([
      getBidProjectById(projectId) as any,
      getMyTender(projectId) as any
    ])
    project.value = projRes.data
    if (tenderRes.data) {
      tender.value = tenderRes.data
      Object.assign(form, {
        id: tenderRes.data.id,
        totalPrice: tenderRes.data.totalPrice,
        includeTax: tenderRes.data.includeTax,
        techFilePath: tenderRes.data.techFilePath || '',
        commercialFilePath: tenderRes.data.commercialFilePath || '',
        priceFilePath: tenderRes.data.priceFilePath || '',
        remark: tenderRes.data.remark || ''
      })
    }
  } catch {} finally {
    pageLoading.value = false
  }
})

const handleSave = async () => {
  saving.value = true
  try {
    const data = { ...form, projectId }
    if (form.id) {
      await saveTender(data)
    } else {
      const res = await saveTender(data) as any
      form.id = res.data?.id || res.data
    }
    ElMessage.success('保存草稿成功')
  } catch {} finally {
    saving.value = false
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  try { await formRef.value.validate() } catch { return }
  await ElMessageBox.confirm('提交后不可修改，确认提交投标文件？', '警告', { type: 'warning' })
  submitting.value = true
  try {
    let id = form.id
    if (!id) {
      const res = await saveTender({ ...form, projectId }) as any
      id = res.data?.id || res.data
      form.id = id
    } else {
      await saveTender({ ...form, projectId })
    }
    if (id) {
      await submitTender(id)
      ElMessage.success('投标文件已提交')
      const tRes = await getMyTender(projectId) as any
      tender.value = tRes.data
    }
  } catch {} finally {
    submitting.value = false
  }
}
</script>
