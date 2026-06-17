import request from '@/utils/request'

export interface Task {
  taskId: string
  taskName: string
  taskDefinitionKey: string
  processInstanceId: string
  processDefinitionId: string
  processName: string
  processDefinitionKey: string
  assignee: string | null
  owner: string | null
  createTime: string
  dueDate: string | null
  description: string | null
  priority: number
  claimTime: string | null
  endTime: string | null
  deleteReason: string | null
  outcome: string | null
  title: string | null
  businessType: string | null
  businessId: number | null
  processVariables: Record<string, any>
}
/**
 * 获取待办任务列表
 * @param data 分页参数
 * @returns 
 */
export function getTodoTasks(data: { pageNum: number; pageSize: number }) {
  return request({ url: '/task/todo', method: 'post', data })
}

/**
 * 获取已办任务列表
 * @param data 分页参数
 * @returns 
 */
export function getDoneTasks(data: { pageNum: number; pageSize: number }) {
  return request({ url: '/task/done', method: 'post', data })
}

/**
 * 获取可认领任务列表
 * @param data 分页参数
 * @returns 
 */
export function getClaimableTasks(data: { pageNum: number; pageSize: number }) {
  return request({ url: '/task/claimable', method: 'post', data })
}

/**
 * 获取任务详情
 * @param taskId 任务ID
 * @returns 
 */
export function getTaskDetail(taskId: string) {
  return request({ url: `/task/${taskId}`, method: 'get' })
}

/**
 * 认领任务
 * @param taskId 任务ID
 * @returns 
 */
export function claimTask(taskId: string) {
  return request({ url: `/task/${taskId}/claim`, method: 'post' })
}

/**
 * 取消认领任务
 * @param taskId 任务ID
 * @returns 
 */
export function unclaimTask(taskId: string) {
  return request({ url: `/task/${taskId}/unclaim`, method: 'post' })
}

/**
 * 完成任务
 * @param taskId 任务ID
 * @param variables 任务变量
 * @returns 
 */
export function completeTask(taskId: string, variables?: Record<string, any>) {
  return request({ url: `/task/${taskId}/complete`, method: 'post', data: { variables } })
}

/**
 * 委派任务
 * @param taskId 任务ID
 * @param delegateUserId 委派人ID
 * @returns 
 */
export function delegateTask(taskId: string, delegateUserId: string) {
  return request({ url: `/task/${taskId}/delegate`, method: 'post', data: { delegateUserId } })
}

/**
 * 拒绝任务
 * @param taskId 任务ID
 * @param processInstanceId 流程实例ID
 * @param reason 拒绝原因
 * @returns 
 */
export function rejectTask(taskId: string, processInstanceId: string, reason: string) {
  return request({ url: `/task/${taskId}/reject`, method: 'post', data: { processInstanceId, reason } })
}
