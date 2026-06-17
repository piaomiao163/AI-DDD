import { defineStore } from 'pinia'
import { loginApi } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { usePermissionStore } from './permission'
import { ElMessage } from 'element-plus'

interface UserInfo {
  id: number
  username: string
  name: string
  nickname?: string
  avatar?: string
  roles: string[]
  permissions: string[]
}

interface UserState {
  token: string
  userInfo: UserInfo | null
  loading: boolean
}

export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    token: getToken() || '',
    userInfo: null,
    loading: false
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    hasUserInfo: (state) => !!state.userInfo,
    userRoles: (state) => state.userInfo?.roles || [],
    userPermissions: (state) => state.userInfo?.permissions || []
  },

  actions: {
    async login(userInfo: { username: string; password: string; captcha?: string; captchaKey?: string }) {
      this.loading = true
      try {
        // 先清除旧的token和用户信息
        this.logout()

        const res: any = await loginApi.login(userInfo)

        if (res.success !== false && res.data) {
          if (res.data.success !== false) {
            if (res.data.token) {
              this.token = res.data.token
              setToken(res.data.token)
              await this.getUserInfo()
              return true
            }
          } else {
            ElMessage.error(res.data.message || '登录失败')
            return false
          }
        }

        ElMessage.error('登录失败，请检查用户名和密码')
        return false
      } catch (error) {
        console.error('登录失败:', error)
        ElMessage.error('登录异常，请稍后重试')
        return false
      } finally {
        this.loading = false
      }
    },

    async getUserInfo() {
      if (!this.token) return false
      
      this.loading = true
      try {
        const res: any = await loginApi.getUserInfo()

        let userData = null
        if (res.success !== false && res.data) {
          userData = res.data
        }

        if (userData) {
          this.userInfo = userData
          const permissions: string[] = []

          if (Array.isArray(userData.roles) && userData.roles.length > 0) {
            // 检查是否有admin角色
            const isAdmin = userData.roles.some((role: any) =>
              (typeof role === 'string' ? role : role?.code)?.toLowerCase() === 'admin'
            )
            userData.roles.forEach((role: any) => {
              if (Array.isArray(role.permissions) && role.permissions.length > 0) {
                role.permissions.forEach((permission: any) => {
                  if (permission.code) {
                    permissions.push(permission.code)
                  }
                })
              }
            })

            if (Array.isArray(userData.permissions) && userData.permissions.length > 0) {
              permissions.push(...userData.permissions)
            }

            const permissionStore = usePermissionStore()
            permissionStore.setPermissions(permissions, isAdmin)
            return true
          }
        }
        return false
      } catch (error: any) {
        console.error('获取用户信息失败:', error)
        if (error?.response?.status === 401) {
          this.logout()
        }
        return false
      } finally {
        this.loading = false
      }
    },

    logout() {
      this.token = ''
      this.userInfo = null
      removeToken()
      // 清除本地存储的登录信息
      localStorage.removeItem('username')
      localStorage.removeItem('password')
      localStorage.removeItem('remember')
      // 清除Pinia store中的权限信息
      const permissionStore = usePermissionStore()
      permissionStore.clearPermissions()
      // 清除所有Cookies
      if (typeof document !== 'undefined') {
        const cookies = document.cookie.split(';')
        for (let i = 0; i < cookies.length; i++) {
          const cookie = cookies[i]
          const eqPos = cookie.indexOf('=')
          const name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie
          document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/'
        }
      }
    }
  }
})