import Cookies from 'js-cookie'

const TOKEN_KEY = 'admin-token'

export function getToken() {
  return Cookies.get(TOKEN_KEY)
}

export function setToken(token: string) {
  return Cookies.set(TOKEN_KEY, token, { path: '/', domain: '' })
}

export function removeToken() {
  return Cookies.remove(TOKEN_KEY, { path: '/', domain: '' })
}
