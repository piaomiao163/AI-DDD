import request from '@/utils/request'

export interface ProcessInstance {
  id: number
  title: string
  processInstanceId: string
  processDefinitionKey: string
  processDefinitionDbId: number
  processName: string
  businessKey: string
  businessType?: string
  startUserId: string
  startUserDbId?: number
  startUserName?: string
  startTime: string
  endTime: string | null
  status: number  // 0-运行中, 1-已完成, 2-已终止, 3-已撤回
  currentNodeName?: string
  priority?: number
  deleteReason: string | null
  variables: Record<string, any>
  createTime?: string
  updateTime?: string
}

export interface StartProcessRequest {
  processDefinitionKey: string
  title: string
  businessKey?: string
  businessType?: string
  priority?: number
  variables?: Record<string, any>
}

export function startProcess(data: StartProcessRequest) {
  return request({ url: '/process-instance/start', method: 'post', data })
}

export function getRunningInstances(data: { pageNum: number; pageSize: number }) {
  return request({ url: '/process-instance/running', method: 'post', data })
}

export function getHistoryInstances(data: { pageNum: number; pageSize: number }) {
  return request({ url: '/process-instance/history', method: 'post', data })
}

export function getInstanceDetail(id: number) {
  return request({ url: `/process-instance/${id}`, method: 'get' })
}

export function terminateInstance(id: number, reason: string) {
  return request({ url: `/process-instance/${id}/terminate`, method: 'post', data: { reason } })
}

export function withdrawInstance(id: number, reason: string) {
  return request({ url: `/process-instance/${id}/withdraw`, method: 'post', data: { reason } })
}

export function getInstancesByPage(data: { pageNum: number; pageSize: number; title?: string; status?: number; businessType?: string }) {
  return request({ url: '/process-instance/page', method: 'post', data })
}
