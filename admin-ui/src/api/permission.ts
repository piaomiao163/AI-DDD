import request from '@/utils/request'

// 权限管理接口
export const permissionApi = {
  // 获取权限列表
  getList(params?: any) {
    return request({
      url: '/permission/list',
      method: 'get',
      params
    })
  },

  // 根据ID获取权限
  getById(id: number) {
    return request({
      url: `/permission/${id}`,
      method: 'get'
    })
  },

  // 根据编码获取权限
  getByCode(code: string) {
    return request({
      url: `/permission/code/${code}`,
      method: 'get'
    })
  },

  // 新增权限
  add(data: any) {
    return request({
      url: '/permission/save',
      method: 'post',
      data
    })
  },

  // 修改权限
  update(data: any) {
    return request({
      url: '/permission/update',
      method: 'put',
      data
    })
  },

  // 删除权限
  delete(id: number) {
    return request({
      url: `/permission/delete/${id}`,
      method: 'delete'
    })
  },

  // 批量删除权限
  deleteBatch(ids: number[]) {
    return request({
      url: '/permission/delete/batch',
      method: 'delete',
      data: ids
    })
  }
}
