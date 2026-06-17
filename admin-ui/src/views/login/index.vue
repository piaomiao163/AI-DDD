<template>
  <div class="login-container">
    <div class="login-form-wrapper">
      <h2 class="login-title">后台管理系统</h2>
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" class="login-form">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" prefix-icon="User" size="large" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" prefix-icon="Lock" size="large" show-password />
        </el-form-item>
        <el-form-item prop="captcha" class="captcha-item">
          <div class="captcha-wrapper">
            <el-input v-model="loginForm.captcha" placeholder="请输入验证码" prefix-icon="Key" size="large" class="captcha-input" />
            <div class="captcha-image" @click="getCaptcha" :class="{ 'captcha-loading': captchaLoading }">
              <img v-if="captchaImage" :src="captchaImage" alt="验证码" />
              <el-icon v-else-if="captchaLoading"><Loading /></el-icon>
              <div v-else class="captcha-placeholder">点击获取</div>
            </div>
          </div>
        </el-form-item>
        <el-form-item class="remember-item">
          <el-checkbox v-model="loginForm.remember">记住用户名</el-checkbox>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" class="login-button" :loading="loading" @click="handleLogin">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Loading } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'
import { useUserStore } from '@/store/user'
import { loginApi } from '@/api/login'
import '@/styles/css/login/index.css'

const router = useRouter()
const userStore = useUserStore()

const loginFormRef = ref<FormInstance>()
const loading = ref(false)
const captchaImage = ref('')
const captchaKey = ref('')
const captchaLoading = ref(false)

const loginForm = reactive({
  username: localStorage.getItem('username') || '',
  password: '',
  captcha: '',
  remember: localStorage.getItem('remember') === 'true'
})

const rules = reactive<FormRules>({
  captcha: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { min: 4, max: 6, message: '验证码长度不正确', trigger: 'blur' }
  ]
})

// 获取验证码
const getCaptcha = async () => {
  if (captchaLoading.value) return
  
  captchaLoading.value = true
  try {
    const res: any = await loginApi.getCaptcha()
    if (res.success !== false && res.data) {
      captchaImage.value = res.data.captchaImage
      captchaKey.value = res.data.captchaKey
      // 开发环境：自动填充验证码
      if (import.meta.env.DEV && res.data.captchaCode) {
        loginForm.captcha = res.data.captchaCode
      }
    } else {
      ElMessage.error('获取验证码失败')
    }
  } catch (error) {
    ElMessage.error('获取验证码失败，请稍后重试')
  } finally {
    captchaLoading.value = false
  }
}

const handleLogin = async () => {
  if (!loginFormRef.value) return

  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const success = await userStore.login({
          username: loginForm.username,
          password: loginForm.password,
          captcha: loginForm.captcha,
          captchaKey: captchaKey.value
        })

        if (success) {
          localStorage.removeItem('password')
          // 仅保存用户名，禁止在本地存储密码
          if (loginForm.remember) {
            localStorage.setItem('username', loginForm.username)
            localStorage.setItem('remember', 'true')
          } else {
            localStorage.removeItem('username')
            localStorage.removeItem('remember')
          }
          
          ElMessage.success('登录成功')
          router.push('/')
        } else {
          ElMessage.error('登录失败，请检查用户名和密码')
          // 刷新验证码
          await getCaptcha()
        }
      } catch (error) {
        ElMessage.error('登录异常，请稍后重试')
      } finally {
        loading.value = false
      }
    }
  })
}

onMounted(() => {
  getCaptcha()
})
</script>

