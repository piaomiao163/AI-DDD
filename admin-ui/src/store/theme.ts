import { defineStore } from 'pinia'
import { getTheme, setTheme } from '@/utils/theme'

export const useThemeStore = defineStore('theme', {
  state: () => ({
    theme: getTheme() || 'default',
    sidebar: {
      opened: true,
      withoutAnimation: false
    }
  }),

  actions: {
    setTheme(theme: string) {
      this.theme = theme
      setTheme(theme)
    },

    toggleSidebar() {
      this.sidebar.opened = !this.sidebar.opened
    },

    closeSidebar() {
      this.sidebar.opened = false
    }
  }
})
