import request from '@/utils/request'

export const deptApi = {
  getList(params?: any) {
    return request({
      url: '/department/list',
      method: 'get',
      params
    })
  },

  getTree(params?: any) {
    return request({
      url: '/department/tree',
      method: 'get',
      params
    })
  },

  getById(id: number) {
    return request({
      url: `/department/${id}`,
      method: 'get'
    })
  },

  getByCode(code: string) {
    return request({
      url: `/department/code/${code}`,
      method: 'get'
    })
  },

  getRootDepts() {
    return request({
      url: '/department/root',
      method: 'get'
    })
  },

  getChildrenByParentId(parentId: number) {
    return request({
      url: `/department/children/${parentId}`,
      method: 'get'
    })
  },

  add(data: any) {
    return request({
      url: '/department/save',
      method: 'post',
      data
    })
  },

  update(data: any) {
    return request({
      url: '/department/update',
      method: 'put',
      data
    })
  },

  delete(id: number) {
    return request({
      url: `/department/delete/${id}`,
      method: 'delete'
    })
  },

  deleteBatch(ids: number[]) {
    return request({
      url: '/department/delete/batch',
      method: 'delete',
      data: ids
    })
  }
}
