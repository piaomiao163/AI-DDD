<template>
  <div class="operation-log-container">
    <el-card>
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="操作模块">
          <el-input v-model="queryParams.module" placeholder="请输入操作模块" clearable />
        </el-form-item>
        <el-form-item label="操作类型">
          <el-select v-model="queryParams.type" placeholder="请选择操作类型" clearable class="filter-input">
            <el-option label="新增" value="ADD" />
            <el-option label="修改" value="EDIT" />
            <el-option label="删除" value="DELETE" />
            <el-option label="查询" value="QUERY" />
            <el-option label="导出" value="EXPORT" />
            <el-option label="登录" value="LOGIN" />
            <el-option label="登出" value="LOGOUT" />
          </el-select>
        </el-form-item>
        <el-form-item label="操作人员">
          <el-input v-model="queryParams.username" placeholder="请输入操作人员" clearable />
        </el-form-item>
        <el-form-item label="操作状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable class="filter-input">
            <el-option label="成功" :value="1" />
            <el-option label="失败" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">搜索</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-row class="table-toolbar" :gutter="10">
        <el-col>
          <el-button type="danger" icon="Delete" :disabled="multiple" @click="handleDeleteBatch">删除</el-button>
          <el-button type="warning" icon="DeleteFilled" @click="handleClear">清空</el-button>
        </el-col>
      </el-row>

      <el-table
        v-loading="loading"
        :data="logList"
        border
        stripe
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="id" label="日志ID" width="80" align="center" />
        <el-table-column prop="module" label="操作模块" width="120" />
        <el-table-column prop="type" label="操作类型" width="100">
          <template #default="scope">
            <el-tag :type="getTypeType(scope.row.type)">
              {{ getTypeLabel(scope.row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="操作描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="username" label="操作人员" width="120" />
        <el-table-column prop="ipAddress" label="IP地址" width="140" />
        <el-table-column prop="status" label="操作状态" width="80" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="executionTime" label="执行时长(ms)" width="120" align="center" />
        <el-table-column prop="operationTime" label="操作时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right" align="center">
          <template #default="scope">
            <el-button type="primary" size="small" link icon="View" @click="handleView(scope.row)">详情</el-button>
            <el-button type="danger" size="small" link icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleQuery"
        @current-change="handleQuery"
      />
    </el-card>

    <el-dialog v-model="detailDialogVisible" title="操作日志详情" width="700px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="日志ID">{{ currentLog.id }}</el-descriptions-item>
        <el-descriptions-item label="操作模块">{{ currentLog.module }}</el-descriptions-item>
        <el-descriptions-item label="操作类型">
          <el-tag :type="getTypeType(currentLog.type)">{{ getTypeLabel(currentLog.type) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="操作描述">{{ currentLog.description }}</el-descriptions-item>
        <el-descriptions-item label="请求方法">{{ currentLog.requestMethod }}</el-descriptions-item>
        <el-descriptions-item label="请求URL">{{ currentLog.requestUrl }}</el-descriptions-item>
        <el-descriptions-item label="操作人员">{{ currentLog.username }}</el-descriptions-item>
        <el-descriptions-item label="操作人ID">{{ currentLog.userId }}</el-descriptions-item>
        <el-descriptions-item label="IP地址">{{ currentLog.ipAddress }}</el-descriptions-item>
        <el-descriptions-item label="操作地点">{{ currentLog.location }}</el-descriptions-item>
        <el-descriptions-item label="操作状态">
          <el-tag :type="currentLog.status === 1 ? 'success' : 'danger'">
            {{ currentLog.status === 1 ? '成功' : '失败' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="执行时长">{{ currentLog.executionTime }}ms</el-descriptions-item>
        <el-descriptions-item label="操作时间" :span="2">{{ currentLog.operationTime }}</el-descriptions-item>
        <el-descriptions-item label="请求参数" :span="2">
          <pre class="json-pre">{{ formatJson(currentLog.requestParams) }}</pre>
        </el-descriptions-item>
        <el-descriptions-item label="响应结果" :span="2">
          <pre class="json-pre">{{ formatJson(currentLog.responseResult) }}</pre>
        </el-descriptions-item>
        <el-descriptions-item v-if="currentLog.errorMsg" label="错误消息" :span="2">
          <pre class="error-pre">{{ currentLog.errorMsg }}</pre>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { operationLogApi } from '@/api/operationLog'
import '@/styles/css/system/common.css'
import '@/styles/css/system/operationLog.css'

const loading = ref(false)

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  module: '',
  type: '',
  username: '',
  status: undefined as number | undefined
})

const logList = ref<any[]>([])
const total = ref(0)

const multiple = ref(true)
const selectedIds = ref<number[]>([])

const detailDialogVisible = ref(false)
const currentLog = ref<any>({})

const getList = async () => {
  loading.value = true
  try {
    const res: any = await operationLogApi.getList(queryParams)
    logList.value = res.data || []
    total.value = res.data?.length || 0
  } catch (error: any) {
    ElMessage.error(error.message || '获取日志列表失败')
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

const resetQuery = () => {
  queryParams.module = ''
  queryParams.type = ''
  queryParams.username = ''
  queryParams.status = undefined
  handleQuery()
}

const handleSelectionChange = (selection: any[]) => {
  selectedIds.value = selection.map(item => item.id)
  multiple.value = !selection.length
}

const handleView = (row: any) => {
  currentLog.value = row
  detailDialogVisible.value = true
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm(`是否确认删除该日志记录？`, '系统提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const res: any = await operationLogApi.delete(row.id)
      if (res.success !== false) {
        ElMessage.success('删除成功')
        getList()
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    } catch (error: any) {
      ElMessage.error(error.message || '删除失败')
    }
  })
}

const handleDeleteBatch = () => {
  ElMessageBox.confirm(`是否确认删除选中的 ${selectedIds.value.length} 条日志记录？`, '系统提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const res: any = await operationLogApi.deleteBatch(selectedIds.value)
      if (res.success !== false) {
        ElMessage.success('删除成功')
        getList()
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    } catch (error: any) {
      ElMessage.error(error.message || '删除失败')
    }
  })
}

const handleClear = () => {
  ElMessageBox.confirm('是否确认清空所有日志记录？', '系统提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const res: any = await operationLogApi.clear()
      if (res.success !== false) {
        ElMessage.success('清空成功')
        getList()
      } else {
        ElMessage.error(res.message || '清空失败')
      }
    } catch (error: any) {
      ElMessage.error(error.message || '清空失败')
    }
  })
}

const getTypeLabel = (type: string) => {
  const typeMap: Record<string, string> = {
    'ADD': '新增',
    'EDIT': '修改',
    'DELETE': '删除',
    'QUERY': '查询',
    'EXPORT': '导出',
    'LOGIN': '登录',
    'LOGOUT': '登出',
    'OTHER': '其他'
  }
  return typeMap[type] || type
}

const getTypeType = (type: string) => {
  const typeMap: Record<string, any> = {
    'ADD': 'success',
    'EDIT': 'warning',
    'DELETE': 'danger',
    'QUERY': 'info',
    'EXPORT': 'primary',
    'LOGIN': 'success',
    'LOGOUT': 'info'
  }
  return typeMap[type] || ''
}

const formatJson = (json: string) => {
  if (!json) return ''
  try {
    return JSON.stringify(JSON.parse(json), null, 2)
  } catch {
    return json
  }
}

onMounted(() => {
  getList()
})
</script>

