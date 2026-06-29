<template>
  <div class="bid-hall">
    <el-card shadow="never">
      <template #header>
        <span class="card-title">招标大厅</span>
      </template>

      <el-form :inline="true" :model="queryParams" style="margin-bottom:16px">
        <el-form-item label="关键词">
          <el-input v-model="queryParams.keyword" placeholder="搜索项目名称" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="采购类型">
          <el-select v-model="queryParams.purchaseType" placeholder="全部" clearable style="width:120px">
            <el-option v-for="(label, value) in purchaseTypeMap" :key="value" :label="label" :value="Number(value)" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <div v-loading="loading">
        <div v-if="list.length === 0 && !loading" style="text-align:center;padding:40px;color:#909399">暂无招标项目</div>
        <el-row :gutter="16">
          <el-col v-for="item in list" :key="item.id" :xs="24" :sm="12" :md="8" :lg="6" style="margin-bottom:16px">
            <el-card shadow="hover" style="height:100%">
              <div style="font-weight:bold;font-size:15px;margin-bottom:8px;color:#303133;overflow:hidden;text-overflow:ellipsis;white-space:nowrap" :title="item.projectName">
                {{ item.projectName }}
              </div>
              <div style="color:#606266;font-size:13px;line-height:2">
                <div>采购类型：<span style="color:#409eff">{{ purchaseTypeMap[item.purchaseType] || '-' }}</span></div>
                <div>招标方式：{{ bidMethodMap[item.bidMethod] || '-' }}</div>
                <div>预算金额：<span style="color:#e6a23c;font-weight:bold">{{ item.budgetAmount?.toLocaleString() || '-' }} 万元</span></div>
                <div>报名截止：{{ item.registrationDeadline || '-' }}</div>
              </div>
              <div style="display:flex;justify-content:space-between;align-items:center;margin-top:12px">
                <el-tag :type="(projectStatusTagMap[item.status] as any)" size="small" effect="dark">
                  {{ projectStatusMap[item.status] || '-' }}
                </el-tag>
                <el-button type="primary" size="small" @click="router.push(`/bid/hall/apply/${item.id}`)">查看详情</el-button>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <div style="display:flex;justify-content:flex-end;margin-top:16px">
        <el-pagination
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[12, 24, 48]"
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
import { getBidProjectHall, purchaseTypeMap, bidMethodMap, projectStatusMap, projectStatusTagMap } from '@/api/bid'

const router = useRouter()
const list = ref<any[]>([])
const total = ref(0)
const loading = ref(false)
const queryParams = reactive({ pageNum: 1, pageSize: 12, keyword: '', purchaseType: null as number | null })

onMounted(fetchData)

async function fetchData() {
  loading.value = true
  try {
    const res = await getBidProjectHall({ ...queryParams }) as any
    list.value = res.data?.list || []
    total.value = res.data?.total || 0
  } catch {} finally {
    loading.value = false
  }
}

const handleQuery = () => { queryParams.pageNum = 1; fetchData() }
const resetQuery = () => { queryParams.keyword = ''; queryParams.purchaseType = null; handleQuery() }
</script>
