import request from '@/utils/request'

// 岗位管理接口
export const postApi = {
  // 获取岗位列表
  getList(params?: any) {
    return request({
      url: '/post/list',
      method: 'get',
      params
    })
  },

  // 根据ID获取岗位
  getById(id: number) {
    return request({
      url: `/post/${id}`,
      method: 'get'
    })
  },

  // 根据编码获取岗位
  getByCode(code: string) {
    return request({
      url: `/post/code/${code}`,
      method: 'get'
    })
  },

  // 新增岗位
  add(data: any) {
    return request({
      url: '/post/save',
      method: 'post',
      data
    })
  },

  // 修改岗位
  update(data: any) {
    return request({
      url: '/post/update',
      method: 'put',
      data
    })
  },

  // 删除岗位
  delete(id: number) {
    return request({
      url: `/post/delete/${id}`,
      method: 'delete'
    })
  },

  // 批量删除岗位
  deleteBatch(ids: number[]) {
    return request({
      url: '/post/delete/batch',
      method: 'delete',
      data: ids
    })
  }
}