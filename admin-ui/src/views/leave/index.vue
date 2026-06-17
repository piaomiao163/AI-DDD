<template>
  <div class="leave-manage">
    <el-card shadow="never">
      <template #header>
        <span class="card-title">请假管理</span>
      </template>
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="标题">
          <el-input v-model="queryParams.title" placeholder="搜索标题" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="queryParams.leaveType" placeholder="全部" clearable class="filter-select">
            <el-option v-for="(label, value) in leaveTypeMap" :key="value" :label="label" :value="Number(value)" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="全部" clearable class="filter-select">
            <el-option v-for="(label, value) in leaveStatusMap" :key="value" :label="label" :value="Number(value)" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="list" v-loading="loading" stripe border>
        <el-table-column prop="title" label="标题" min-width="160" show-overflow-tooltip />
        <el-table-column prop="applicantName" label="申请人" width="100" />
        <el-table-column prop="deptName" label="部门" width="100" />
        <el-table-column prop="leaveType" label="类型" width="90" align="center">
          <template #default="scope">
            {{ leaveTypeMap[scope.row.leaveType] || '未知' }}
          </template>
        </el-table-column>
        <el-table-column label="日期范围" min-width="180">
          <template #default="scope">
            {{ scope.row.startDate }} ~ {{ scope.row.endDate }}
          </template>
        </el-table-column>
        <el-table-column prop="days" label="天数" width="80" align="center" />
        <el-table-column prop="status" label="状态" width="90" align="center">
          <template #default="scope">
            <el-tag :type="leaveStatusTagType[scope.row.status]" effect="dark" size="small">
              {{ leaveStatusMap[scope.row.status] || '未知' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="currentNodeName" label="当前节点" width="110" align="center">
          <template #default="scope">
            {{ scope.row.currentNodeName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交时间" width="170" />
        <el-table-column prop="approveComment" label="审批意见" min-width="120" show-overflow-tooltip>
          <template #default="scope">
            {{ scope.row.approveComment || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center" fixed="right">
          <template #default="scope">
            <el-button v-if="scope.row.processInstanceId" size="small" @click="viewDiagram(scope.row.processInstanceId)">
              <el-icon><View /></el-icon>
              流程图
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="fetchData"
          background
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { View } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { getLeavePage, leaveTypeMap, leaveStatusMap, leaveStatusTagType, type Leave, type LeavePageRequest, type PageResult } from '@/api/leave'
import '@/styles/css/leave/index.css'

const router = useRouter()
const list = ref<Leave[]>([])
const total = ref(0)
const loading = ref(false)
const queryParams = reactive({ pageNum: 1, pageSize: 10, title: '', leaveType: null as number | null, status: null as number | null })

onMounted(() => { fetchData() })

const fetchData = async () => {
  loading.value = true
  try {
    const data: LeavePageRequest = { pageNum: queryParams.pageNum, pageSize: queryParams.pageSize }
    if (queryParams.title) data.title = queryParams.title
    if (queryParams.leaveType !== null) data.leaveType = queryParams.leaveType
    if (queryParams.status !== null) data.status = queryParams.status
    const res = await getLeavePage(data) as { data: PageResult<Leave> }
    list.value = res.data?.list || []
    total.value = res.data?.total || 0
  } catch (error: unknown) {
    console.error('获取请假列表失败', error)
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  queryParams.pageNum = 1
  fetchData()
}

const resetQuery = () => {
  queryParams.title = ''
  queryParams.leaveType = null
  queryParams.status = null
  handleQuery()
}

const handleSizeChange = () => {
  queryParams.pageNum = 1
  fetchData()
}

const viewDiagram = (processInstanceId: number) => {
  router.push(`/process/diagram/${processInstanceId}`)
}
</script>
