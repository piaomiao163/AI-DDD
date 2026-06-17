import request from '@/utils/request'

// 操作日志接口
export const operationLogApi = {
  // 获取操作日志列表
  getList(params?: any) {
    return request({
      url: '/operationLog/list',
      method: 'get',
      params
    })
  },

  // 根据条件搜索操作日志
  search(params: { module?: string; type?: string; username?: string }) {
    return request({
      url: '/operationLog/search',
      method: 'get',
      params
    })
  },

  // 根据ID获取操作日志
  getById(id: number) {
    return request({
      url: `/operationLog/${id}`,
      method: 'get'
    })
  },

  // 删除操作日志
  delete(id: number) {
    return request({
      url: `/operationLog/delete/${id}`,
      method: 'delete'
    })
  },

  // 批量删除操作日志
  deleteBatch(ids: number[]) {
    return request({
      url: '/operationLog/delete/batch',
      method: 'delete',
      data: ids
    })
  },

  // 清空操作日志
  clear() {
    return request({
      url: '/operationLog/clear',
      method: 'delete'
    })
  }
}
