<template>
  <template v-if="!error">
    <router-view />
  </template>
  <template v-else>
    <div class="error-container">
      <el-result
        icon="error"
        title="应用出错"
        sub-title="抱歉，应用发生了错误，请刷新页面重试"
      >
        <template #extra>
          <el-button type="primary" @click="handleRefresh">刷新页面</el-button>
          <el-button @click="handleBackHome">返回首页</el-button>
        </template>
      </el-result>
    </div>
  </template>
</template>

<script setup lang="ts">
import { ref, onErrorCaptured } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import '@/styles/css/app.css'

const router = useRouter()
const error = ref<Error | null>(null)

// 错误捕获
onErrorCaptured((err) => {
  console.error('应用错误:', err)
  error.value = err
  ElMessage.error('应用发生错误，请刷新页面重试')
  return true
})

// 刷新页面
const handleRefresh = () => {
  error.value = null
  window.location.reload()
}

// 返回首页
const handleBackHome = () => {
  error.value = null
  router.push('/')
}
</script>