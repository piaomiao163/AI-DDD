<template>
  <div class="bid-evaluation">
    <el-card shadow="never">
      <template #header>
        <span class="card-title">评标任务</span>
      </template>

      <el-table :data="list" v-loading="loading" stripe border>
        <el-table-column prop="projectName" label="项目名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="purchaseType" label="采购类型" width="100" align="center">
          <template #default="{ row }">{{ purchaseTypeMap[row.purchaseType] || '-' }}</template>
        </el-table-column>
        <el-table-column prop="evalMethod" label="评标方法" width="130" align="center">
          <template #default="{ row }">{{ evalMethodMap[row.evalMethod] || '-' }}</template>
        </el-table-column>
        <el-table-column prop="biddingDeadline" label="投标截止时间" width="170" />
        <el-table-column prop="tenderCount" label="投标数量" width="90" align="center">
          <template #default="{ row }">{{ row.tenderCount ?? '-' }}</template>
        </el-table-column>
        <el-table-column prop="myEvaluated" label="我的评标状态" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="row.myEvaluated ? 'success' : 'warning'" size="small">
              {{ row.myEvaluated ? '已完成' : '待评标' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="130" align="center" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="router.push(`/bid/evaluation/score/${row.id}`)">
              {{ row.myEvaluated ? '查看评分' : '开始评标' }}
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
import { getBidProjectPage, purchaseTypeMap, evalMethodMap } from '@/api/bid'

const router = useRouter()
const list = ref<any[]>([])
const total = ref(0)
const loading = ref(false)
const queryParams = reactive({ pageNum: 1, pageSize: 10, status: 6 })

onMounted(fetchData)

async function fetchData() {
  loading.value = true
  try {
    const res = await getBidProjectPage({ ...queryParams }) as any
    list.value = res.data?.list || []
    total.value = res.data?.total || 0
  } catch {} finally {
    loading.value = false
  }
}
</script>
