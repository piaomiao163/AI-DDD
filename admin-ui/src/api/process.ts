import request from '@/utils/request';

// 流程定义相关API

export interface ProcessDefinition {
  id?: number;
  name: string;
  processKey: string;
  description: string;
  category?: string;
  xml?: string;
  version?: number;
  status?: number;
  createTime?: string;
  updateTime?: string;
  deploymentId?: string;
  actProcessDefinitionId?: string;
}

export interface ProcessPageRequest {
  pageNum: number;
  pageSize: number;
  name?: string;
  processKey?: string;
}

export interface ProcessPageResponse {
  data: ProcessDefinition[];
  total: number;
}

// 保存流程定义
export function saveProcess(process: ProcessDefinition) {
  return request({
    url: '/process/save',
    method: 'post',
    data: process
  });
}

// 根据ID查询流程定义
export function getProcessById(id: number) {
  return request({
    url: `/process/findById/${id}`,
    method: 'get'
  });
}

// 根据Key查询流程定义
export function getProcessByKey(key: string) {
  return request({
    url: `/process/findByKey/${key}`,
    method: 'get'
  });
}

// 查询所有流程定义
export function getAllProcesses() {
  return request({
    url: '/process/findAll',
    method: 'get'
  });
}

// 分页查询流程定义
export function getProcessesByPage(data: ProcessPageRequest) {
  return request({
    url: '/process/findByPage',
    method: 'post',
    data
  });
}

// 更新流程定义
export function updateProcess(process: ProcessDefinition) {
  return request({
    url: '/process/update',
    method: 'put',
    data: process
  });
}

// 删除流程定义
export function deleteProcess(id: number) {
  return request({
    url: `/process/delete/${id}`,
    method: 'delete'
  });
}

// 发布流程定义
export function publishProcess(id: number) {
  return request({
    url: `/process/publish/${id}`,
    method: 'post'
  });
}

// 取消发布流程定义
export function unpublishProcess(id: number) {
  return request({
    url: `/process/unpublish/${id}`,
    method: 'post'
  });
}

// 获取流程图PNG（二进制流）
export function getProcessDiagram(processInstanceId: string) {
  return request({
    url: `/process-diagram/${processInstanceId}`,
    method: 'get',
    responseType: 'blob'
  });
}
