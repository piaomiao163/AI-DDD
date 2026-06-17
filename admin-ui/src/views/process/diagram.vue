<template>
  <div class="process-diagram-view">
    <div class="diagram-header">
      <el-button text @click="back">
        <el-icon><ArrowLeft/></el-icon>
        返回
      </el-button>
      <span class="diagram-title">流程图查看</span>
    </div>
    <div class="diagram-content">
      <img
        v-if="diagramUrl"
        :src="diagramUrl"
        alt="流程图"
        class="diagram-image"
      />
      <div v-else-if="loading" class="diagram-loading">
        <el-icon class="is-loading" :size="32"><Loading /></el-icon>
        <span>加载流程图中...</span>
      </div>
      <div v-else class="diagram-empty">
        <span>无法加载流程图</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { ArrowLeft, Loading } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useRouter, useRoute } from 'vue-router'
import request from '@/utils/request'
import '@/styles/css/process/diagram.css'

const router = useRouter()
const route = useRoute()

const diagramUrl = ref<string>('')
const loading = ref(false)

onMounted(async () => {
  const id = route.params.id as string
  if (!id) {
    ElMessage.error('缺少流程实例ID')
    return
  }

  loading.value = true
  try {
    // 使用业务表ID获取流程图（通过request工具自动带token）
    const response = await request.get(`/process-diagram/by-business-id/${id}`, {
      responseType: 'blob'
    })
    const blob = new Blob([response as any], { type: 'image/svg+xml' })
    diagramUrl.value = URL.createObjectURL(blob)
  } catch (error: any) {
    console.error('加载流程图失败:', error)
    if (error?.response?.status === 404) {
      ElMessage.error('流程实例不存在')
    } else {
      ElMessage.error('加载流程图失败')
    }
  } finally {
    loading.value = false
  }
})

onBeforeUnmount(() => {
  if (diagramUrl.value) {
    URL.revokeObjectURL(diagramUrl.value)
  }
})

const back = () => {
  router.back()
}
</script>
