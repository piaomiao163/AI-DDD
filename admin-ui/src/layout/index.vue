<template>
  <div class="layout-container" :class="`theme-${themeStore.theme}`">
    <el-container>
      <el-aside :width="sidebarWidth" class="sidebar">
        <div class="logo">
          <h2>管理系统</h2>
        </div>
        <el-menu
          :default-active="activeMenu"
          :collapse="!sidebar.opened"
          :unique-opened="true"
          router
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
        >
          <el-menu-item index="/dashboard">
            <el-icon><House /></el-icon>
            <template #title>首页</template>
          </el-menu-item>

          <el-sub-menu index="/system">
            <template #title>
              <el-icon><Setting /></el-icon>
              <span>系统管理</span>
            </template>
            <el-menu-item index="/system/user" v-if="permissionStore.hasPermission('system:user:manage')">
              <el-icon><User /></el-icon>
              <template #title>用户管理</template>
            </el-menu-item>
            <el-menu-item index="/system/role" v-if="permissionStore.hasPermission('system:role:manage')">
              <el-icon><UserFilled /></el-icon>
              <template #title>角色管理</template>
            </el-menu-item>
            <el-menu-item index="/system/menu" v-if="permissionStore.hasPermission('system:menu:list')">
              <el-icon><Menu /></el-icon>
              <template #title>菜单管理</template>
            </el-menu-item>
            <el-menu-item index="/system/dept" v-if="permissionStore.hasPermission('system:department:manage')">
              <el-icon><OfficeBuilding /></el-icon>
              <template #title>部门管理</template>
            </el-menu-item>
            <el-menu-item index="/system/permission" v-if="permissionStore.hasPermission('system:permission:manage')">
              <el-icon><Key /></el-icon>
              <template #title>权限管理</template>
            </el-menu-item>
            <el-menu-item index="/system/dataDictionary" v-if="permissionStore.hasPermission('system:dataDictionary:manage')">
              <el-icon><Grid /></el-icon>
              <template #title>数据字典</template>
            </el-menu-item>
            <el-menu-item index="/system/operationLog" v-if="permissionStore.hasPermission('system:operationLog:manage')">
              <el-icon><Document /></el-icon>
              <template #title>操作日志</template>
            </el-menu-item>
            <el-menu-item index="/system/post" v-if="permissionStore.hasPermission('system:post:manage')">
              <el-icon><Briefcase /></el-icon>
              <template #title>岗位管理</template>
            </el-menu-item>
          </el-sub-menu>

          <el-menu-item index="/process" v-if="permissionStore.hasPermission('process:definition:list')">
            <el-icon><DataBoard /></el-icon>
            <template #title>流程定义</template>
          </el-menu-item>

          <el-sub-menu index="/leave">
            <template #title>
              <el-icon><Calendar /></el-icon>
              <span>请假管理</span>
            </template>
            <el-menu-item index="/leave/apply" v-if="permissionStore.hasPermission('leave:apply')">
              <el-icon><EditPen /></el-icon>
              <template #title>请假申请</template>
            </el-menu-item>
            <el-menu-item index="/leave/my" v-if="permissionStore.hasPermission('leave:my:list')">
              <el-icon><List /></el-icon>
              <template #title>我的请假</template>
            </el-menu-item>
            <el-menu-item index="/leave/approval" v-if="permissionStore.hasPermission('leave:approval:list')">
              <el-icon><Stamp /></el-icon>
              <template #title>请假审批</template>
            </el-menu-item>
            <el-menu-item index="/leave/manage" v-if="permissionStore.hasPermission('leave:manage')">
              <el-icon><DataBoard /></el-icon>
              <template #title>请假管理</template>
            </el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>

      <el-container>
        <el-header class="header">
          <div class="header-left">
            <div class="collapse-btn" @click="toggleSidebar">
              <el-icon :size="20">
                <Fold v-if="sidebar.opened" />
                <Expand v-else />
              </el-icon>
            </div>
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item>{{ currentRoute?.meta?.title }}</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          <div class="header-right">
            <el-dropdown @command="handleCommand">
              <span class="user-info">
                <el-avatar :size="32" icon="UserFilled" />
                <span class="username">{{ userStore.userInfo?.nickname || userStore.userInfo?.username || '管理员' }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="theme">
                    <span>主题切换</span>
                  </el-dropdown-item>
                  <el-dropdown-item command="logout">
                    <span>退出登录</span>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>

        <el-main class="main-content">
          <router-view />
        </el-main>
      </el-container>
    </el-container>

    <el-dialog v-model="themeDialogVisible" title="主题切换" width="400px">
      <el-radio-group v-model="currentTheme">
        <el-radio-button v-for="theme in themes" :key="theme.value" :label="theme.value">
          {{ theme.label }}
        </el-radio-button>
      </el-radio-group>
      <template #footer>
        <el-button @click="themeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleThemeChange">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Fold, Expand, House, Setting, User, UserFilled, Menu, OfficeBuilding, Key, Grid, Document, DataBoard, EditPen, Briefcase, Calendar, List, Stamp } from '@element-plus/icons-vue'
import { useThemeStore } from '@/store/theme'
import { useUserStore } from '@/store/user'
import { usePermissionStore } from '@/store/permission'
import '@/styles/css/layout/index.css'

const route = useRoute()
const router = useRouter()
const themeStore = useThemeStore()
const userStore = useUserStore()
const permissionStore = usePermissionStore()

const themeDialogVisible = ref(false)
const currentTheme = ref(themeStore.theme)

const themes = [
  { label: '默认', value: 'default' },
  { label: '暗黑', value: 'dark' },
  { label: '蓝色', value: 'blue' }
]

const sidebar = computed(() => themeStore.sidebar)
const sidebarWidth = computed(() => sidebar.value.opened ? '200px' : '64px')
const activeMenu = computed(() => route.path)
const currentRoute = computed(() => route)

onMounted(() => {
  applyTheme(themeStore.theme)
})

const toggleSidebar = () => {
  themeStore.toggleSidebar()
}

const handleCommand = async (command: string) => {
  if (command === 'theme') {
    currentTheme.value = themeStore.theme
    themeDialogVisible.value = true
  } else if (command === 'logout') {
    try {
      await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      userStore.logout()
      ElMessage.success('退出成功')
      router.push('/login')
    } catch {
      // 用户取消
    }
  }
}

const handleThemeChange = () => {
  themeStore.setTheme(currentTheme.value)
  applyTheme(currentTheme.value)
  themeDialogVisible.value = false
  ElMessage.success('主题切换成功')
}

const applyTheme = (theme: string) => {
  document.body.className = document.body.className.replace(/theme-\w+/g, '')
  document.body.classList.add(`theme-${theme}`)
}
</script>
