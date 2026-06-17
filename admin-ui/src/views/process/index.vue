<template>
  <div class="process-list">
    <!-- 顶部统计卡片 -->
    <div class="stats-cards">
      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon total-icon">
            <el-icon><Document /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ total }}</div>
            <div class="stat-label">总流程数</div>
          </div>
        </div>
      </el-card>

      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon published-icon">
            <el-icon><CircleCheck /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ publishedCount }}</div>
            <div class="stat-label">已发布</div>
          </div>
        </div>
      </el-card>

      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon draft-icon">
            <el-icon><EditPen /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ draftCount }}</div>
            <div class="stat-label">草稿</div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 主卡片 -->
    <el-card class="main-card" shadow="never">
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <span class="title">流程定义管理</span>
            <el-tag type="info" effect="plain" size="small">共 {{ total }} 个流程</el-tag>
          </div>
          <div class="header-right">
            <el-button type="primary" @click="createProcess">
              <el-icon><Plus /></el-icon>
              新建流程
            </el-button>
          </div>
        </div>
      </template>

      <!-- 搜索区域 -->
      <div class="search-area">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="流程名称">
            <el-input
                v-model="searchForm.name"
                placeholder="请输入流程名称"
                clearable
                prefix-icon="Search"
                class="search-input"
            />
          </el-form-item>
          <el-form-item label="流程标识">
            <el-input
                v-model="searchForm.processKey"
                placeholder="请输入流程标识"
                clearable
                prefix-icon="Key"
                class="search-input"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">
              <el-icon><Search /></el-icon>
              查询
            </el-button>
            <el-button @click="resetForm">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 表格 -->
      <el-table
          :data="processList"
          class="process-table full-width"
          stripe
          border
          highlight-current-row
          v-loading="loading"
      >
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="name" label="流程名称" min-width="150">
          <template #default="scope">
            <div class="process-name">
              <el-icon><Files /></el-icon>
              <span>{{ scope.row.name }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="processKey" label="流程标识" min-width="150">
          <template #default="scope">
            <el-tag type="info" effect="plain" size="small">{{ scope.row.processKey }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="流程描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="version" label="版本" width="80" align="center">
          <template #default="scope">
            <el-tag size="small" type="warning">v{{ scope.row.version }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'" effect="dark">
              {{ scope.row.status === 1 ? '已发布' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" align="center">
          <template #default="scope">
            <div class="time-cell">
              <el-icon><Clock /></el-icon>
              <span>{{ scope.row.createTime }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" width="180" align="center">
          <template #default="scope">
            <div class="time-cell">
              <el-icon><Timer /></el-icon>
              <span>{{ scope.row.updateTime }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" align="center" fixed="right">
          <template #default="scope">
            <div class="action-buttons">
              <el-button
                  type="success"
                  size="small"
                  @click="designProcess(scope.row.id)"
              >
                <el-icon><Operation /></el-icon>
                设计
              </el-button>
              <el-button
                  v-if="scope.row.status !== 1"
                  type="warning"
                  size="small"
                  @click="publishProcess(scope.row.id)"
              >
                <el-icon><Check /></el-icon>
                发布
              </el-button>
              <el-button
                  v-if="scope.row.status === 1"
                  type="info"
                  size="small"
                  @click="unpublishProcess(scope.row.id)"
              >
                <el-icon><Close /></el-icon>
                取消发布
              </el-button>
              <el-button type="danger" size="small" @click="deleteProcess(scope.row.id)">
                <el-icon><Delete /></el-icon>
                删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
            v-model:current-page="pagination.pageNum"
            v-model:page-size="pagination.pageSize"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            background
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import {
  Plus, Operation, Check, Delete, Search, Refresh,
  Document, CircleCheck, EditPen, Files, Clock, Timer, Close
} from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useRouter } from 'vue-router';
import * as processApi from '../../api/process';
import '@/styles/css/process/index.css'

const router = useRouter();

const processList = ref<any[]>([]);
const total = ref(0);
const loading = ref(false);
const pagination = ref({
  pageNum: 1,
  pageSize: 10
});

const searchForm = ref({
  name: '',
  processKey: ''
});

// 计算统计数据 - 注意: publishedCount/draftCount 仅基于当前页数据
// 如果需要全量统计，应调用后端专用统计接口
const publishedCount = computed(() => {
  return processList.value.filter(item => item.status === 1).length;
});

const draftCount = computed(() => {
  return total.value - publishedCount.value;
});

// 初始化
onMounted(() => {
  fetchProcesses();
});

// 获取流程列表
const fetchProcesses = async () => {
  loading.value = true;
  try {
    const response = await processApi.getProcessesByPage({
      pageNum: pagination.value.pageNum,
      pageSize: pagination.value.pageSize,
      name: searchForm.value.name,
      processKey: searchForm.value.processKey
    });
    processList.value = response.data.list || [];
    total.value = response.data.total || 0;
  } catch (error) {
    ElMessage.error('获取流程列表失败');
    console.error(error);
  } finally {
    loading.value = false;
  }
};

// 搜索
const handleSearch = () => {
  pagination.value.pageNum = 1;
  fetchProcesses();
};

// 重置
const resetForm = () => {
  searchForm.value = {
    name: '',
    processKey: ''
  };
  pagination.value.pageNum = 1;
  fetchProcesses();
};

// 分页大小变化
const handleSizeChange = (size: number) => {
  pagination.value.pageSize = size;
  fetchProcesses();
};

// 当前页变化
const handleCurrentChange = (page: number) => {
  pagination.value.pageNum = page;
  fetchProcesses();
};

// 新建流程
const createProcess = () => {
  router.push('/process/design');
};

// 设计流程
const designProcess = (id: number) => {
  router.push(`/process/design?id=${id}`);
};

// 发布流程
const publishProcess = async (id: number) => {
  try {
    await ElMessageBox.confirm('确定要发布该流程吗？发布后流程将生效。', '发布确认', {
      confirmButtonText: '确定发布',
      cancelButtonText: '取消',
      type: 'warning',
      distinguishCancelAndClose: true
    });

    await processApi.publishProcess(id);
    ElMessage.success('发布成功');
    fetchProcesses();
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('发布失败');
      console.error(error);
    }
  }
};

// 取消发布流程
const unpublishProcess = async (id: number) => {
  try {
    await ElMessageBox.confirm('取消发布后流程将不再生效，已有流程实例不受影响。确定要取消发布吗？', '取消发布确认', {
      confirmButtonText: '确定取消',
      cancelButtonText: '返回',
      type: 'warning',
      distinguishCancelAndClose: true
    });

    await processApi.unpublishProcess(id);
    ElMessage.success('已取消发布');
    fetchProcesses();
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('取消发布失败');
      console.error(error);
    }
  }
};

// 删除流程
const deleteProcess = async (id: number) => {
  try {
    await ElMessageBox.confirm('删除后无法恢复，确定要删除该流程吗？', '删除确认', {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'error',
      distinguishCancelAndClose: true
    });

    await processApi.deleteProcess(id);
    ElMessage.success('删除成功');
    fetchProcesses();
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败');
      console.error(error);
    }
  }
};
</script>

