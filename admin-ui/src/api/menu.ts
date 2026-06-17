import request from '@/utils/request'

// 菜单管理接口
export const menuApi = {
  // 获取菜单列表
  getList(params?: any) {
    return request({
      url: '/menu/list',
      method: 'get',
      params
    })
  },

  // 根据ID获取菜单
  getById(id: number) {
    return request({
      url: `/menu/${id}`,
      method: 'get'
    })
  },

  // 获取根菜单
  getRootMenus() {
    return request({
      url: '/menu/root',
      method: 'get'
    })
  },

  // 根据父菜单ID获取子菜单
  getChildrenByParentId(parentId: number) {
    return request({
      url: `/menu/children/${parentId}`,
      method: 'get'
    })
  },

  // 新增菜单
  add(data: any) {
    return request({
      url: '/menu/save',
      method: 'post',
      data
    })
  },

  // 修改菜单
  update(data: any) {
    return request({
      url: '/menu/update',
      method: 'put',
      data
    })
  },

  // 删除菜单
  delete(id: number) {
    return request({
      url: `/menu/delete/${id}`,
      method: 'delete'
    })
  },

  // 批量删除菜单
  deleteBatch(ids: number[]) {
    return request({
      url: '/menu/delete/batch',
      method: 'delete',
      data: ids
    })
  },

  // 获取菜单树
  getTree() {
    return request({
      url: '/menu/tree',
      method: 'get'
    })
  }
}
