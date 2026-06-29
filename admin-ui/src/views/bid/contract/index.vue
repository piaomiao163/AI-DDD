<template>
  <div class="bid-contract">
    <el-card shadow="never">
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span class="card-title">合同管理</span>
          <el-button type="primary" @click="openDialog()">创建合同</el-button>
        </div>
      </template>

      <el-table :data="list" v-loading="loading" stripe border>
        <el-table-column prop="contractName" label="合同名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="projectName" label="关联项目" min-width="150" show-overflow-tooltip />
        <el-table-column prop="partyA" label="甲方" min-width="130" show-overflow-tooltip />
        <el-table-column prop="partyB" label="乙方" min-width="130" show-overflow-tooltip />
        <el-table-column prop="amount" label="合同金额(万元)" width="140" align="right">
          <template #default="{ row }">{{ row.amount?.toLocaleString() || '-' }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="contractTagMap[row.status] as any" size="small" effect="dark">
              {{ contractStatusMap[row.status] || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="320" align="center" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="openDialog(row)">查看/编辑</el-button>
            <el-button size="small" type="success" v-if="row.status === 1" @click="openSignDialog(row.id)">签署</el-button>
            <el-button size="small" type="primary" v-if="row.status === 2" @click="handleExecute(row.id)">开始履行</el-button>
            <el-button size="small" type="success" v-if="row.status === 3" @click="handleComplete(row.id)">完结</el-button>
            <el-button size="small" type="danger" v-if="[1,2,3].includes(row.status)" @click="handleTerminate(row.id)">解除</el-button>
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

    <!-- 创建/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogForm.id ? '编辑合同' : '创建合同'" width="600px" @close="resetDialogForm">
      <el-form ref="dialogFormRef" :model="dialogForm" :rules="dialogRules" label-width="120px">
        <el-form-item label="合同名称" prop="contractName">
          <el-input v-model="dialogForm.contractName" clearable />
        </el-form-item>
        <el-form-item label="关联项目ID" prop="projectId">
          <el-input-number v-model="dialogForm.projectId" :min="1" style="width:100%" />
        </el-form-item>
        <el-form-item label="甲方" prop="partyA">
          <el-input v-model="dialogForm.partyA" clearable />
        </el-form-item>
        <el-form-item label="乙方" prop="partyB">
          <el-input v-model="dialogForm.partyB" clearable />
        </el-form-item>
        <el-form-item label="合同金额(万元)" prop="amount">
          <el-input-number v-model="dialogForm.amount" :min="0" :precision="2" style="width:100%" />
        </el-form-item>
        <el-form-item label="合同开始日期">
          <el-date-picker v-model="dialogForm.startDate" type="date" value-format="YYYY-MM-DD" style="width:100%" />
        </el-form-item>
        <el-form-item label="合同结束日期">
          <el-date-picker v-model="dialogForm.endDate" type="date" value-format="YYYY-MM-DD" style="width:100%" />
        </el-form-item>
        <el-form-item label="合同正文">
          <el-input v-model="dialogForm.content" type="textarea" :rows="6" placeholder="输入合同正文内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="dialogLoading" @click="handleDialogSubmit">保存</el-button>
      </template>
    </el-dialog>

    <!-- 签署对话框 -->
    <el-dialog v-model="signDialog.visible" title="签署合同" width="360px">
      <el-form label-width="100px">
        <el-form-item label="签署日期">
          <el-date-picker v-model="signDialog.signDate" type="date" value-format="YYYY-MM-DD" style="width:100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="signDialog.visible = false">取消</el-button>
        <el-button type="success" :loading="signDialog.loading" @click="handleSign">确认签署</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import {
  getContractPage, saveContract, updateContract,
  signContract, executeContract, completeContract, terminateContract,
  contractStatusMap
} from '@/api/bid'

const contractTagMap: Record<number, string> = { 0: 'info', 1: 'warning', 2: 'primary', 3: 'success', 4: 'success', 5: 'danger' }

const list = ref<any[]>([])
const total = ref(0)
const loading = ref(false)
const queryParams = reactive({ pageNum: 1, pageSize: 10 })

const dialogVisible = ref(false)
const dialogLoading = ref(false)
const dialogFormRef = ref<FormInstance>()
const dialogForm = reactive({
  id: null as number | null,
  contractName: '', projectId: 0, partyA: '', partyB: '',
  amount: 0, startDate: '', endDate: '', content: ''
})
const dialogRules: FormRules = {
  contractName: [{ required: true, message: '请输入合同名称', trigger: 'blur' }],
  partyA: [{ required: true, message: '请输入甲方', trigger: 'blur' }],
  partyB: [{ required: true, message: '请输入乙方', trigger: 'blur' }]
}

const signDialog = reactive({ visible: false, id: 0, signDate: '', loading: false })

onMounted(fetchData)

async function fetchData() {
  loading.value = true
  try {
    const res = await getContractPage({ ...queryParams }) as any
    list.value = res.data?.list || []
    total.value = res.data?.total || 0
  } catch {} finally {
    loading.value = false
  }
}

const openDialog = (row?: any) => {
  resetDialogForm()
  if (row) Object.assign(dialogForm, row)
  dialogVisible.value = true
}
const resetDialogForm = () => {
  dialogForm.id = null; dialogForm.contractName = ''; dialogForm.projectId = 0;
  dialogForm.partyA = ''; dialogForm.partyB = ''; dialogForm.amount = 0;
  dialogForm.startDate = ''; dialogForm.endDate = ''; dialogForm.content = ''
  dialogFormRef.value?.clearValidate()
}

const handleDialogSubmit = async () => {
  if (!dialogFormRef.value) return
  try { await dialogFormRef.value.validate() } catch { return }
  dialogLoading.value = true
  try {
    if (dialogForm.id) {
      await updateContract({ ...dialogForm })
    } else {
      await saveContract({ ...dialogForm })
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    fetchData()
  } catch {} finally { dialogLoading.value = false }
}

const openSignDialog = (id: number) => {
  signDialog.id = id; signDialog.signDate = ''; signDialog.visible = true
}
const handleSign = async () => {
  if (!signDialog.signDate) { ElMessage.warning('请选择签署日期'); return }
  signDialog.loading = true
  try {
    await signContract(signDialog.id, signDialog.signDate)
    ElMessage.success('签署成功')
    signDialog.visible = false
    fetchData()
  } catch {} finally { signDialog.loading = false }
}

const handleExecute = async (id: number) => {
  await ElMessageBox.confirm('确认开始履行该合同？', '提示', { type: 'warning' })
  try { await executeContract(id); ElMessage.success('已开始履行'); fetchData() } catch {}
}
const handleComplete = async (id: number) => {
  await ElMessageBox.confirm('确认完结该合同？', '提示', { type: 'warning' })
  try { await completeContract(id); ElMessage.success('合同已完结'); fetchData() } catch {}
}
const handleTerminate = async (id: number) => {
  await ElMessageBox.confirm('确认解除该合同？此操作不可恢复', '警告', { type: 'warning' })
  try { await terminateContract(id); ElMessage.success('合同已解除'); fetchData() } catch {}
}
</script>
