<template>
  <div class="bid-expert">
    <el-card shadow="never">
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span class="card-title">专家库管理</span>
          <el-button type="primary" @click="openDialog()">新增专家</el-button>
        </div>
      </template>

      <el-form :inline="true" :model="queryParams" style="margin-bottom:8px">
        <el-form-item label="姓名">
          <el-input v-model="queryParams.name" placeholder="搜索姓名" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="专业方向">
          <el-input v-model="queryParams.specialty" placeholder="搜索专业" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="list" v-loading="loading" stripe border>
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="specialty" label="专业方向" min-width="150" />
        <el-table-column prop="title" label="职称" width="120" />
        <el-table-column prop="organization" label="单位" min-width="180" show-overflow-tooltip />
        <el-table-column prop="phone" label="联系电话" width="130" />
        <el-table-column prop="blacklisted" label="黑名单" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.blacklisted ? 'danger' : 'success'" size="small">{{ row.blacklisted ? '是' : '否' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="240" align="center" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="openDialog(row)">编辑</el-button>
            <el-button size="small" :type="row.blacklisted ? 'success' : 'warning'"
              @click="row.blacklisted ? handleUnblacklist(row.id) : handleBlacklist(row.id)">
              {{ row.blacklisted ? '移出黑名单' : '加入黑名单' }}
            </el-button>
            <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogForm.id ? '编辑专家' : '新增专家'" width="520px" @close="resetDialogForm">
      <el-form ref="dialogFormRef" :model="dialogForm" :rules="dialogRules" label-width="100px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="dialogForm.name" clearable />
        </el-form-item>
        <el-form-item label="专业方向" prop="specialty">
          <el-input v-model="dialogForm.specialty" clearable />
        </el-form-item>
        <el-form-item label="职称" prop="title">
          <el-input v-model="dialogForm.title" clearable />
        </el-form-item>
        <el-form-item label="单位" prop="organization">
          <el-input v-model="dialogForm.organization" clearable />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="dialogForm.phone" clearable />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="dialogForm.email" clearable />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="dialogForm.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="dialogLoading" @click="handleDialogSubmit">保存</el-button>
      </template>
    </el-dialog>

    <!-- 黑名单原因对话框 -->
    <el-dialog v-model="blacklistDialog.visible" title="加入黑名单原因" width="400px">
      <el-input v-model="blacklistDialog.reason" type="textarea" :rows="3" placeholder="请输入原因" />
      <template #footer>
        <el-button @click="blacklistDialog.visible = false">取消</el-button>
        <el-button type="warning" :loading="blacklistDialog.loading" @click="confirmBlacklist">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { getExpertPage, saveExpert, updateExpert, deleteExpert, blacklistExpert, unblacklistExpert } from '@/api/bid'

const list = ref<any[]>([])
const total = ref(0)
const loading = ref(false)
const queryParams = reactive({ pageNum: 1, pageSize: 10, name: '', specialty: '' })

const dialogVisible = ref(false)
const dialogLoading = ref(false)
const dialogFormRef = ref<FormInstance>()
const dialogForm = reactive({ id: null as number | null, name: '', specialty: '', title: '', organization: '', phone: '', email: '', remark: '' })
const dialogRules: FormRules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  specialty: [{ required: true, message: '请输入专业方向', trigger: 'blur' }]
}

const blacklistDialog = reactive({ visible: false, id: 0, reason: '', loading: false })

onMounted(fetchData)

async function fetchData() {
  loading.value = true
  try {
    const res = await getExpertPage({ ...queryParams }) as any
    list.value = res.data?.list || []
    total.value = res.data?.total || 0
  } catch {} finally {
    loading.value = false
  }
}

const handleQuery = () => { queryParams.pageNum = 1; fetchData() }
const resetQuery = () => { queryParams.name = ''; queryParams.specialty = ''; handleQuery() }

const openDialog = (row?: any) => {
  resetDialogForm()
  if (row) Object.assign(dialogForm, row)
  dialogVisible.value = true
}
const resetDialogForm = () => {
  dialogForm.id = null; dialogForm.name = ''; dialogForm.specialty = ''; dialogForm.title = '';
  dialogForm.organization = ''; dialogForm.phone = ''; dialogForm.email = ''; dialogForm.remark = ''
  dialogFormRef.value?.clearValidate()
}

const handleDialogSubmit = async () => {
  if (!dialogFormRef.value) return
  try { await dialogFormRef.value.validate() } catch { return }
  dialogLoading.value = true
  try {
    if (dialogForm.id) {
      await updateExpert({ ...dialogForm })
    } else {
      await saveExpert({ ...dialogForm })
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    fetchData()
  } catch {} finally {
    dialogLoading.value = false
  }
}

const handleBlacklist = (id: number) => {
  blacklistDialog.id = id; blacklistDialog.reason = ''; blacklistDialog.visible = true
}
const confirmBlacklist = async () => {
  if (!blacklistDialog.reason) { ElMessage.warning('请输入原因'); return }
  blacklistDialog.loading = true
  try {
    await blacklistExpert(blacklistDialog.id, blacklistDialog.reason)
    ElMessage.success('已加入黑名单')
    blacklistDialog.visible = false
    fetchData()
  } catch {} finally { blacklistDialog.loading = false }
}
const handleUnblacklist = async (id: number) => {
  await ElMessageBox.confirm('确认移出黑名单？', '提示', { type: 'warning' })
  try { await unblacklistExpert(id); ElMessage.success('已移出黑名单'); fetchData() } catch {}
}
const handleDelete = async (id: number) => {
  await ElMessageBox.confirm('确认删除该专家？', '警告', { type: 'warning' })
  try { await deleteExpert(id); ElMessage.success('删除成功'); fetchData() } catch {}
}
</script>
