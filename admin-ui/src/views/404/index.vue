<template>
  <div class="not-found-container">
    <el-result
        icon="warning"
        title="404"
        sub-title="抱歉，您访问的页面不存在"
    >
      <template #extra>
        <el-button type="primary" @click="handleBackHome">返回首页</el-button>
        <el-button @click="handleGoBack">返回上一页</el-button>
      </template>
    </el-result>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import '@/styles/css/404/index.css'

const router = useRouter()

// 返回首页
const handleBackHome = () => {
  router.push('/')
}

// 返回上一页
const handleGoBack = () => {
  router.back()
}
</script>

