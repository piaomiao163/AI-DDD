import Cookies from 'js-cookie'

const THEME_KEY = 'admin-theme'

export function getTheme() {
  return Cookies.get(THEME_KEY)
}

export function setTheme(theme: string) {
  return Cookies.set(THEME_KEY, theme)
}
