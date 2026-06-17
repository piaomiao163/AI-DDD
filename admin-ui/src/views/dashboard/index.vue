<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <el-col :span="6" v-for="item in stats" :key="item.title">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" :style="{ backgroundColor: item.color }">
              <el-icon :size="30"><component :is="item.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ item.value }}</div>
              <div class="stat-title">{{ item.title }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" class="mt-20">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>欢迎使用后台管理系统</span>
            </div>
          </template>
          <div class="welcome-content">
            <h3>欢迎您，管理员！</h3>
            <p>这是一个基于 Vue3 + Element Plus + TypeScript 的后台管理系统</p>
            <p>支持主题切换、用户管理、角色管理、菜单管理等功能</p>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { User, UserFilled, Document, Setting } from '@element-plus/icons-vue'
import '@/styles/css/dashboard/index.css'

const stats = ref([
  { title: '用户总数', value: '128', color: '#409EFF', icon: User },
  { title: '角色总数', value: '12', color: '#67C23A', icon: UserFilled },
  { title: '菜单总数', value: '56', color: '#E6A23C', icon: Document },
  { title: '系统版本', value: 'v1.0.0', color: '#F56C6C', icon: Setting }
])
</script>

