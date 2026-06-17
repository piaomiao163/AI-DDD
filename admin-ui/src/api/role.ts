import request from '@/utils/request'

// 角色管理接口
export const roleApi = {
  // 获取角色列表
  getList(params?: any) {
    return request({
      url: '/role/list',
      method: 'get',
      params
    })
  },

  // 根据ID获取角色
  getById(id: number) {
    return request({
      url: `/role/${id}`,
      method: 'get'
    })
  },

  // 根据编码获取角色
  getByCode(code: string) {
    return request({
      url: `/role/code/${code}`,
      method: 'get'
    })
  },

  // 新增角色
  add(data: any) {
    return request({
      url: '/role/save',
      method: 'post',
      data
    })
  },

  // 修改角色
  update(data: any) {
    return request({
      url: '/role/update',
      method: 'put',
      data
    })
  },

  // 删除角色
  delete(id: number) {
    return request({
      url: `/role/delete/${id}`,
      method: 'delete'
    })
  },

  // 批量删除角色
  deleteBatch(ids: number[]) {
    return request({
      url: '/role/delete/batch',
      method: 'delete',
      data: ids
    })
  },

  // 获取角色权限
  getRolePermissions(roleId: number) {
    return request({
      url: `/role/${roleId}/permissions`,
      method: 'get'
    })
  },

  // 分配角色权限
  assignRolePermissions(roleId: number, permissionIds: number[]) {
    return request({
      url: `/role/${roleId}/permissions`,
      method: 'post',
      data: permissionIds
    })
  }
}
