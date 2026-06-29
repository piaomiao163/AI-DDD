<template>
  <div class="bid-tender">
    <el-card shadow="never">
      <template #header>
        <span class="card-title">我的投标</span>
      </template>

      <el-table :data="list" v-loading="loading" stripe border>
        <el-table-column prop="projectName" label="项目名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="purchaseType" label="采购类型" width="100" align="center">
          <template #default="{ row }">{{ purchaseTypeMap[row.purchaseType] || '-' }}</template>
        </el-table-column>
        <el-table-column prop="biddingDeadline" label="投标截止时间" width="170" />
        <el-table-column prop="tenderStatus" label="投标状态" width="110" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.tenderStatus !== undefined"
              :type="row.tenderStatus === 4 ? 'success' : row.tenderStatus === 5 ? 'danger' : row.tenderStatus === 1 ? 'primary' : 'info'"
              size="small">
              {{ tenderStatusMap[row.tenderStatus] || '未投标' }}
            </el-tag>
            <el-tag v-else type="info" size="small">未投标</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalPrice" label="报价(万元)" width="120" align="right">
          <template #default="{ row }">{{ row.totalPrice?.toLocaleString() || '-' }}</template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary"
              :disabled="row.tenderStatus === 1"
              @click="router.push(`/bid/tender/form/${row.projectId || row.id}`)">
              {{ row.tenderStatus === 1 ? '已提交' : '编辑投标文件' }}
            </el-button>
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
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getRegistrationPage, purchaseTypeMap, tenderStatusMap } from '@/api/bid'

const router = useRouter()
const list = ref<any[]>([])
const total = ref(0)
const loading = ref(false)
const queryParams = reactive({ pageNum: 1, pageSize: 10 })

onMounted(fetchData)

async function fetchData() {
  loading.value = true
  try {
    const res = await getRegistrationPage({ ...queryParams, status: 1 }) as any
    list.value = res.data?.list || []
    total.value = res.data?.total || 0
  } catch {} finally {
    loading.value = false
  }
}
</script>
