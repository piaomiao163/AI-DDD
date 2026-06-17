import { defineStore } from 'pinia'

/**
 * 权限管理store
 */
export const usePermissionStore = defineStore('permission', {
  state: () => ({
    // 权限列表
    permissions: [] as string[],
    // 是否admin角色
    isAdmin: false
  }),

  getters: {
    /**
     * 检查是否拥有某个权限
     */
    hasPermission: (state) => {
      return (permission: string): boolean => {
        // admin角色拥有所有权限
        if (state.isAdmin) {
          return true
        }
        return state.permissions.includes(permission)
      }
    }
  },

  actions: {
    /**
     * 设置权限列表和admin标记
     */
    setPermissions(permissions: string[], isAdmin: boolean = false) {
      this.permissions = permissions
      this.isAdmin = isAdmin
    },

    /**
     * 清除权限
     */
    clearPermissions() {
      this.permissions = []
      this.isAdmin = false
    }
  }
})
