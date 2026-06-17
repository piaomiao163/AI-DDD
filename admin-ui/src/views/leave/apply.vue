<template>
  <div class="leave-apply">
    <el-card shadow="never">
      <template #header>
        <span class="card-title">请假申请</span>
      </template>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" class="leave-apply-form">
        <el-form-item label="请假标题" prop="title">
          <el-input v-model="form.title" placeholder="如：年假申请" clearable />
        </el-form-item>
        <el-form-item label="请假类型" prop="leaveType">
          <el-select v-model="form.leaveType" placeholder="请选择" class="full-width">
            <el-option v-for="(label, value) in leaveTypeMap" :key="value" :label="label" :value="Number(value)" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker v-model="form.startDate" type="date" placeholder="选择开始日期" value-format="YYYY-MM-DD" class="full-width" @change="calcDays" />
        </el-form-item>
        <el-form-item label="结束日期" prop="endDate">
          <el-date-picker v-model="form.endDate" type="date" placeholder="选择结束日期" value-format="YYYY-MM-DD" class="full-width" @change="calcDays" />
        </el-form-item>
        <el-form-item label="请假天数" prop="days">
          <el-input-number v-model="form.days" :min="0.5" :step="0.5" :precision="1" class="full-width" />
        </el-form-item>
        <el-form-item label="请假原因" prop="reason">
          <el-input v-model="form.reason" type="textarea" :rows="4" placeholder="请输入请假原因" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">提交申请</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { applyLeave, leaveTypeMap } from '@/api/leave'
import '@/styles/css/leave/apply.css'

const formRef = ref<FormInstance>()
const submitting = ref(false)

const form = reactive({
  title: '',
  leaveType: null as number | null,
  startDate: '',
  endDate: '',
  days: 1,
  reason: ''
})

const rules: FormRules = {
  title: [{ required: true, message: '请输入请假标题', trigger: 'blur' }],
  leaveType: [{ required: true, message: '请选择请假类型', trigger: 'change' }],
  startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
  endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }],
  days: [{ required: true, message: '请输入请假天数', trigger: 'blur' }],
  reason: [{ required: true, message: '请输入请假原因', trigger: 'blur' }]
}

const calcDays = () => {
  if (form.startDate && form.endDate) {
    const start = new Date(form.startDate)
    const end = new Date(form.endDate)
    const diff = (end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24) + 1
    if (diff > 0) {
      form.days = Math.round(diff * 10) / 10
    }
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate()
  submitting.value = true
  try {
    await applyLeave({
      title: form.title,
      leaveType: form.leaveType!,
      startDate: form.startDate,
      endDate: form.endDate,
      days: form.days,
      reason: form.reason
    })
    ElMessage.success('请假申请提交成功')
    resetForm()
  } catch {
    ElMessage.error('提交失败')
  } finally {
    submitting.value = false
  }
}

const resetForm = () => {
  form.title = ''
  form.leaveType = null
  form.startDate = ''
  form.endDate = ''
  form.days = 1
  form.reason = ''
  formRef.value?.clearValidate()
}
</script>
