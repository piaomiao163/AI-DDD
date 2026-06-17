import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'

// 扩展AxiosRequestConfig类型
declare module 'axios' {
  interface AxiosRequestConfig {
    showLoading?: boolean
  }
}
import { ElMessage, ElLoading } from 'element-plus'
import { getToken, removeToken } from './auth'
import router from '@/router'

// 环境配置
const BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api'

// 创建axios实例
const service: AxiosInstance = axios.create({
  baseURL: BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  }
})

// 请求加载状态管理
let loadingInstance: any = null

// 请求拦截器
service.interceptors.request.use(
  (config: any) => {
    // 显示加载状态
    if (config.showLoading !== false) {
      loadingInstance = ElLoading.service({
        lock: true,
        text: '加载中...',
        background: 'rgba(0, 0, 0, 0.7)'
      })
    }
    
    // 添加token
    const token = getToken()
    if (token) {
      config.headers = config.headers || {}
      config.headers.Authorization = `Bearer ${token}`
    }
    
    // 添加请求时间戳，避免缓存
    if (config.method === 'get') {
      config.params = {
        ...config.params,
        _t: Date.now()
      }
    }
    
    return config
  },
  (error) => {
    // 关闭加载状态
    if (loadingInstance) {
      loadingInstance.close()
      loadingInstance = null
    }
    ElMessage.error('请求配置错误')
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    // 关闭加载状态
    if (loadingInstance) {
      loadingInstance.close()
      loadingInstance = null
    }
    
    const res = response.data
    
    // 兼容后端返回的格式
    if (res.success === false) {
      // 处理401未授权
      if (res.code === 401 || res.status === 401) {
        removeToken()
        router.push('/login')
        ElMessage.error(res.message || '未授权，请重新登录')
        return Promise.reject(new Error(res.message || '未授权，请重新登录'))
      }

      // 其他业务错误：提示并reject
      ElMessage.error(res.message || '操作失败')
      return Promise.reject(new Error(res.message || '操作失败'))
    }
    
    // 返回响应数据
    return res
  },
  (error) => {
    // 关闭加载状态
    if (loadingInstance) {
      loadingInstance.close()
      loadingInstance = null
    }
    
    // 处理网络错误
    let errorMessage = '网络错误'
    if (error.response) {
      switch (error.response.status) {
        case 400:
          errorMessage = '请求参数错误'
          break
        case 401:
          errorMessage = '未授权，请重新登录'
          removeToken()
          router.push('/login')
          break
        case 403:
          errorMessage = '拒绝访问'
          break
        case 404:
          errorMessage = '请求地址不存在'
          break
        case 500:
          errorMessage = '服务器内部错误'
          break
        default:
          errorMessage = `请求失败(${error.response.status})`
      }
    } else if (error.message) {
      errorMessage = error.message
    }
    
    ElMessage.error(errorMessage)
    return Promise.reject(error)
  }
)

// 导出请求方法
export default service

// 导出常用请求方法
export const request = {
  get<T = any>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return service.get(url, config)
  },
  post<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return service.post(url, data, config)
  },
  put<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return service.put(url, data, config)
  },
  delete<T = any>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return service.delete(url, config)
  }
}