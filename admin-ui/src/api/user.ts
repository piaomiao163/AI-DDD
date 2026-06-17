import request from '@/utils/request'

// 用户管理接口
export const userApi = {
  // 获取用户列表
  getList(params?: any) {
    return request({
      url: '/user/list',
      method: 'get',
      params
    })
  },

  // 根据ID获取用户
  getById(id: number) {
    return request({
      url: `/user/${id}`,
      method: 'get'
    })
  },

  // 根据用户名获取用户
  getByUsername(username: string) {
    return request({
      url: `/user/username/${username}`,
      method: 'get'
    })
  },

  // 新增用户
  add(data: any) {
    return request({
      url: '/user/save',
      method: 'post',
      data
    })
  },

  // 修改用户
  update(data: any) {
    return request({
      url: '/user/update',
      method: 'put',
      data
    })
  },

  // 删除用户
  delete(id: number) {
    return request({
      url: `/user/delete/${id}`,
      method: 'delete'
    })
  },

  // 批量删除用户
  deleteBatch(ids: number[]) {
    return request({
      url: '/user/delete/batch',
      method: 'delete',
      data: ids
    })
  },

  // 获取用户角色
  getUserRoles(userId: number) {
    return request({
      url: `/user/${userId}/roles`,
      method: 'get'
    })
  },

  // 分配用户角色
  assignUserRoles(userId: number, roleIds: number[]) {
    return request({
      url: `/user/${userId}/roles`,
      method: 'post',
      data: roleIds
    })
  }
}
