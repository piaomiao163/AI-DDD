import request from '@/utils/request'

// 登录相关接口
export const loginApi = {
  // 用户登录
  login(data: { username: string; password: string; captcha?: string; captchaKey?: string }) {
    return request({
      url: '/login',
      method: 'post',
      data
    })
  },

  // 用户注册
  register(data: any) {
    return request({
      url: '/register',
      method: 'post',
      data
    })
  },

  // 获取验证码
  getCaptcha() {
    return request({
      url: '/captcha',
      method: 'get'
    })
  },

  // 获取当前登录用户信息
  getUserInfo() {
    return request({
      url: '/user/info',
      method: 'get'
    })
  },

  // 用户登出
  logout() {
    return request({
      url: '/logout',
      method: 'post'
    })
  },

  // 修改密码
  changePassword(data: { oldPassword: string; newPassword: string }) {
    return request({
      url: '/change-password',
      method: 'post',
      data
    })
  }
}
