import request from '@/utils/request'

/**
 * 分页结果
 */
export interface PageResult<T> {
  /**
   * 总条数
   */
  total: number

  /**
   * 数据列表
   */
  list: T[]

  /**
   * 当前页码
   */
  pageNum: number

  /**
   * 每页条数
   */
  pageSize: number
}

/**
 * 请假单
 */
export interface Leave {
  /**
   * 请假单ID
   */
  id: number

  /**
   * 请假标题
   */
  title: string

  /**
   * 请假类型
   */
  leaveType: number

  /**
   * 开始日期
   */
  startDate: string

  /**
   * 结束日期
   */
  endDate: string

  /**
   * 请假天数
   */
  days: number

  /**
   * 请假原因
   */
  reason: string

  /**
   * 申请人ID
   */
  applicantId: number

  /**
   * 申请人姓名
   */
  applicantName: string

  /**
   * 申请人部门名称
   */
  deptName: string

  /**
   * 请假状态
   */
  status: number

  /**
   * 流程实例ID
   */
  processInstanceId: number

  /**
   * 审批意见
   */
  approveComment: string

  /**
   * 当前流程节点名称
   */
  currentNodeName: string

  /**
   * 创建时间
   */
  createTime: string

  /**
   * 更新时间
   */
  updateTime: string
}

/**
 * 请假申请请求
 */
export interface ApplyLeaveRequest {
  /**
   * 请假标题
   */
  title: string

  /**
   * 请假类型
   */
  leaveType: number

  /**
   * 开始日期
   */
  startDate: string

  /**
   * 结束日期
   */
  endDate: string

  /**
   * 请假天数
   */
  days: number

  /**
   * 请假原因
   */
  reason: string
}

/**
 * 请假分页查询请求
 */
export interface LeavePageRequest {
  /**
   * 页码
   */
  pageNum: number

  /**
   * 每页条数
   */
  pageSize: number

  /**
   * 请假标题
   */
  title?: string

  /**
   * 请假类型
   */
  leaveType?: number

  /**
   * 请假状态
   */
  status?: number
}

/**
 * 提交请假申请
 * @param data 请假申请请求
 */
export function applyLeave(data: ApplyLeaveRequest) {
  return request({ url: '/leave/apply', method: 'post', data })
}

/**
 * 查询我的请假列表
 * @param data 分页参数
 */
export function getMyLeaveList(data: { pageNum: number; pageSize: number }) {
  return request({ url: '/leave/myList', method: 'post', data, showLoading: false })
}

/**
 * 管理端分页查询请假单
 * @param data 查询条件
 */
export function getLeavePage(data: LeavePageRequest) {
  return request({ url: '/leave/page', method: 'post', data, showLoading: false })
}

/**
 * 查询请假单详情
 * @param id 请假单ID
 */
export function getLeaveDetail(id: number) {
  return request({ url: `/leave/${id}`, method: 'get' })
}

/**
 * 撤回请假单
 * @param id 请假单ID
 * @param reason 撤回原因
 */
export function withdrawLeave(id: number, reason: string) {
  return request({ url: `/leave/${id}/withdraw`, method: 'post', data: { reason } })
}

// ========== 请假审批相关 ==========

/**
 * 请假审批列表项
 */
export interface LeaveApprovalItem {
  /**
   * 请假单ID
   */
  leaveId: number

  /**
   * 请假标题
   */
  title: string

  /**
   * 请假类型
   */
  leaveType: number

  /**
   * 开始日期
   */
  startDate: string

  /**
   * 结束日期
   */
  endDate: string

  /**
   * 请假天数
   */
  days: number

  /**
   * 请假原因
   */
  reason: string

  /**
   * 申请人姓名
   */
  applicantName: string

  /**
   * 申请人部门名称
   */
  deptName: string

  /**
   * 请假状态
   */
  status: number

  /**
   * 当前流程节点名称
   */
  currentNodeName: string

  /**
   * 流程实例ID
   */
  processInstanceId: number

  /**
   * 请假单创建时间
   */
  leaveCreateTime: string

  /**
   * 任务ID
   */
  taskId: string

  /**
   * 任务名称
   */
  taskName: string

  /**
   * 任务创建时间
   */
  taskCreateTime: string
}

/**
 * 请假审批详情
 */
export interface LeaveApprovalDetail {
  /**
   * 请假单ID
   */
  leaveId: number

  /**
   * 请假标题
   */
  title: string

  /**
   * 请假类型
   */
  leaveType: number

  /**
   * 开始日期
   */
  startDate: string

  /**
   * 结束日期
   */
  endDate: string

  /**
   * 请假天数
   */
  days: number

  /**
   * 请假原因
   */
  reason: string

  /**
   * 申请人姓名
   */
  applicantName: string

  /**
   * 申请人部门名称
   */
  deptName: string

  /**
   * 请假状态
   */
  status: number

  /**
   * 当前流程节点名称
   */
  currentNodeName: string

  /**
   * 流程实例ID
   */
  processInstanceId: number

  /**
   * 审批意见
   */
  approveComment: string

  /**
   * 请假单创建时间
   */
  leaveCreateTime: string

  /**
   * 请假单更新时间
   */
  leaveUpdateTime: string

  /**
   * 任务ID
   */
  taskId: string

  /**
   * 任务名称
   */
  taskName: string

  /**
   * Activiti流程实例ID
   */
  actProcessInstanceId: string
}

/**
 * 查询请假审批列表
 * @param data 分页参数
 */
export function getLeaveApprovalList(data: { pageNum: number; pageSize: number }) {
  return request({ url: '/leave/approval/list', method: 'post', data })
}

/**
 * 根据任务ID查询请假审批详情
 * @param taskId 任务ID
 */
export function getLeaveApprovalDetailByTaskId(taskId: string) {
  return request({ url: `/leave/approval/detail/by-task/${taskId}`, method: 'get' })
}

export const leaveTypeMap: Record<number, string> = {
  1: '年假',
  2: '事假',
  3: '病假',
  4: '婚假',
  5: '产假',
  6: '丧假',
  7: '其他'
}

export const leaveStatusMap: Record<number, string> = {
  0: '待审批',
  1: '审批中',
  2: '已通过',
  3: '已驳回',
  4: '已撤回'
}

export const leaveStatusTagType: Record<number, string> = {
  0: 'warning',
  1: 'primary',
  2: 'success',
  3: 'danger',
  4: 'info'
}
