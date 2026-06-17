import request from '@/utils/request'

const dataDictionaryApi = {
  // 获取数据字典列表
  list() {
    return request({
      url: '/dataDictionary/list',
      method: 'get'
    })
  },
  
  // 根据ID获取数据字典
  get(id: number) {
    return request({
      url: `/dataDictionary/get/${id}`,
      method: 'get'
    })
  },
  
  // 创建数据字典
  create(data: any) {
    return request({
      url: '/dataDictionary/create',
      method: 'post',
      data
    })
  },
  
  // 更新数据字典
  update(data: any) {
    return request({
      url: '/dataDictionary/update',
      method: 'post',
      data
    })
  },
  
  // 删除数据字典
  delete(id: number) {
    return request({
      url: `/dataDictionary/delete/${id}`,
      method: 'post'
    })
  },
  
  // 获取字典项列表
  getItemList(dictionaryId: number) {
    return request({
      url: `/dataDictionary/item/list/${dictionaryId}`,
      method: 'get'
    })
  },
  
  // 根据字典编码获取启用的字典项
  getEnabledItems(code: string) {
    return request({
      url: `/dataDictionary/item/enabled/${code}`,
      method: 'get'
    })
  },
  
  // 创建字典项
  createItem(data: any) {
    return request({
      url: '/dataDictionary/item/create',
      method: 'post',
      data
    })
  },
  
  // 更新字典项
  updateItem(data: any) {
    return request({
      url: '/dataDictionary/item/update',
      method: 'post',
      data
    })
  },
  
  // 删除字典项
  deleteItem(id: number) {
    return request({
      url: `/dataDictionary/item/delete/${id}`,
      method: 'post'
    })
  }
}

export default dataDictionaryApi