<template>
  <div class="bid-project">
    <el-card shadow="never">
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span class="card-title">招标项目管理</span>
          <el-button type="primary" @click="router.push('/bid/project/form')">新建项目</el-button>
        </div>
      </template>

      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="项目名称">
          <el-input v-model="queryParams.projectName" placeholder="搜索项目名称" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="采购类型">
          <el-select v-model="queryParams.purchaseType" placeholder="全部" clearable style="width:120px">
            <el-option v-for="(label, value) in purchaseTypeMap" :key="value" :label="label" :value="Number(value)" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="全部" clearable style="width:120px">
            <el-option v-for="(label, value) in projectStatusMap" :key="value" :label="label" :value="Number(value)" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="list" v-loading="loading" stripe border>
        <el-table-column prop="projectName" label="项目名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="purchaseType" label="采购类型" width="100" align="center">
          <template #default="{ row }">{{ purchaseTypeMap[row.purchaseType] || '-' }}</template>
        </el-table-column>
        <el-table-column prop="bidMethod" label="招标方式" width="120" align="center">
          <template #default="{ row }">{{ bidMethodMap[row.bidMethod] || '-' }}</template>
        </el-table-column>
        <el-table-column prop="budgetAmount" label="预算金额(万元)" width="140" align="right">
          <template #default="{ row }">{{ row.budgetAmount?.toLocaleString() || '-' }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="(projectStatusTagMap[row.status] as any)" effect="dark" size="small">
              {{ projectStatusMap[row.status] || '未知' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="360" align="center" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="router.push(`/bid/project/detail/${row.id}`)">详情</el-button>
            <el-button size="small" type="primary" v-if="row.status === 0" @click="router.push(`/bid/project/form/${row.id}`)">编辑</el-button>
            <el-button size="small" type="warning" v-if="row.status === 0" @click="handleSubmit(row.id)">提交审核</el-button>
            <el-button size="small" type="success" v-if="row.status === 1" @click="handlePublish(row.id)">发布</el-button>
            <el-button size="small" type="primary" v-if="row.status === 3" @click="handleOpen(row.id)">触发开标</el-button>
            <el-button size="small" type="primary" v-if="row.status === 5" @click="handleEvaluate(row.id)">开始评标</el-button>
            <el-button size="small" type="primary" v-if="row.status === 6" @click="handlePublicize(row.id)">发起公示</el-button>
            <el-button size="small" type="success" v-if="row.status === 7" @click="handleAward(row)">定标</el-button>
            <el-button size="small" type="danger" v-if="row.status === 0" @click="handleDelete(row.id)">删除</el-button>
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

    <!-- 定标对话框 -->
    <el-dialog v-model="awardDialog.visible" title="定标" width="400px">
      <el-form label-width="100px">
        <el-form-item label="中标投标书ID">
          <el-input-number v-model="awardDialog.winnerTenderId" :min="1" style="width:100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="awardDialog.visible = false">取消</el-button>
        <el-button type="primary" :loading="awardDialog.loading" @click="confirmAward">确认定标</el-button>
      </template>
    </el-dialog>

    <!-- 公示天数对话框 -->
    <el-dialog v-model="publicizeDialog.visible" title="发起公示" width="400px">
      <el-form label-width="100px">
        <el-form-item label="公示天数">
          <el-input-number v-model="publicizeDialog.days" :min="1" :max="30" style="width:100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="publicizeDialog.visible = false">取消</el-button>
        <el-button type="primary" :loading="publicizeDialog.loading" @click="confirmPublicize">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getBidProjectPage, submitBidProject, publishBidProject, deleteBidProject,
  openBidding, startEvaluation, publicizeBidProject, awardBid,
  purchaseTypeMap, bidMethodMap, projectStatusMap, projectStatusTagMap
} from '@/api/bid'

const router = useRouter()
const list = ref<any[]>([])
const total = ref(0)
const loading = ref(false)
const queryParams = reactive({ pageNum: 1, pageSize: 10, projectName: '', purchaseType: null as number | null, status: null as number | null })

const awardDialog = reactive({ visible: false, projectId: 0, winnerTenderId: 0, loading: false })
const publicizeDialog = reactive({ visible: false, projectId: 0, days: 7, loading: false })

onMounted(() => fetchData())

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getBidProjectPage({ ...queryParams }) as any
    list.value = res.data?.list || []
    total.value = res.data?.total || 0
  } catch {
    // handled by request interceptor
  } finally {
    loading.value = false
  }
}

const handleQuery = () => { queryParams.pageNum = 1; fetchData() }
const resetQuery = () => { queryParams.projectName = ''; queryParams.purchaseType = null; queryParams.status = null; handleQuery() }

const handleSubmit = async (id: number) => {
  await ElMessageBox.confirm('确认提交审核？', '提示', { type: 'warning' })
  try {
    await submitBidProject(id)
    ElMessage.success('提交成功')
    fetchData()
  } catch {}
}

const handlePublish = async (id: number) => {
  await ElMessageBox.confirm('确认发布该项目？', '提示', { type: 'warning' })
  try {
    await publishBidProject(id)
    ElMessage.success('发布成功')
    fetchData()
  } catch {}
}

const handleOpen = async (id: number) => {
  await ElMessageBox.confirm('确认触发开标？', '提示', { type: 'warning' })
  try {
    await openBidding(id)
    ElMessage.success('开标成功')
    fetchData()
  } catch {}
}

const handleEvaluate = async (id: number) => {
  await ElMessageBox.confirm('确认开始评标？', '提示', { type: 'warning' })
  try {
    await startEvaluation(id)
    ElMessage.success('评标已开始')
    fetchData()
  } catch {}
}

const handlePublicize = (id: number) => {
  publicizeDialog.projectId = id
  publicizeDialog.days = 7
  publicizeDialog.visible = true
}

const confirmPublicize = async () => {
  publicizeDialog.loading = true
  try {
    await publicizeBidProject(publicizeDialog.projectId, publicizeDialog.days)
    ElMessage.success('公示已发起')
    publicizeDialog.visible = false
    fetchData()
  } catch {} finally {
    publicizeDialog.loading = false
  }
}

const handleAward = (row: any) => {
  awardDialog.projectId = row.id
  awardDialog.winnerTenderId = 0
  awardDialog.visible = true
}

const confirmAward = async () => {
  if (!awardDialog.winnerTenderId) { ElMessage.warning('请输入中标投标书ID'); return }
  awardDialog.loading = true
  try {
    await awardBid(awardDialog.projectId, awardDialog.winnerTenderId)
    ElMessage.success('定标成功')
    awardDialog.visible = false
    fetchData()
  } catch {} finally {
    awardDialog.loading = false
  }
}

const handleDelete = async (id: number) => {
  await ElMessageBox.confirm('确认删除该项目？此操作不可恢复', '警告', { type: 'warning' })
  try {
    await deleteBidProject(id)
    ElMessage.success('删除成功')
    fetchData()
  } catch {}
}
</script>
