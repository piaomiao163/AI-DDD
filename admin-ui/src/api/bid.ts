import request from '@/utils/request'

// ========== 招标项目 ==========
export const getBidProjectPage = (data: any) => request({ url: '/bid/project/page', method: 'post', data })
export const getBidProjectHall = (data: any) => request({ url: '/bid/project/hall', method: 'post', data })
export const getBidProjectById = (id: number) => request({ url: `/bid/project/${id}`, method: 'get' })
export const saveBidProject = (data: any) => request({ url: '/bid/project/save', method: 'post', data })
export const updateBidProject = (data: any) => request({ url: '/bid/project/update', method: 'put', data })
export const submitBidProject = (id: number) => request({ url: `/bid/project/submit/${id}`, method: 'post' })
export const publishBidProject = (id: number) => request({ url: `/bid/project/publish/${id}`, method: 'post' })
export const deleteBidProject = (id: number) => request({ url: `/bid/project/delete/${id}`, method: 'delete' })
export const openBidding = (id: number) => request({ url: `/bid/project/open/${id}`, method: 'post' })
export const startEvaluation = (id: number) => request({ url: `/bid/project/evaluate/${id}`, method: 'post' })
export const publicizeBidProject = (id: number, days: number) => request({ url: `/bid/project/publicize/${id}`, method: 'post', data: { days } })
export const awardBid = (projectId: number, winnerTenderId: number) => request({ url: '/bid/project/award', method: 'post', data: { projectId, winnerTenderId } })

// ========== 供应商报名 ==========
export const getRegistrationPage = (data: any) => request({ url: '/bid/registration/page', method: 'post', data })
export const getRegistrationByProject = (projectId: number) => request({ url: `/bid/registration/project/${projectId}`, method: 'get' })
export const getMyRegistration = (projectId: number) => request({ url: '/bid/registration/my', method: 'get', params: { projectId } })
export const saveRegistration = (data: any) => request({ url: '/bid/registration/save', method: 'post', data })
export const approveRegistration = (id: number) => request({ url: `/bid/registration/approve/${id}`, method: 'post' })
export const rejectRegistration = (id: number, reason: string) => request({ url: `/bid/registration/reject/${id}`, method: 'post', data: { reason } })

// ========== 投标书 ==========
export const saveTender = (data: any) => request({ url: '/bid/tender/save', method: 'post', data })
export const submitTender = (id: number) => request({ url: `/bid/tender/submit/${id}`, method: 'post' })
export const getTenderById = (id: number) => request({ url: `/bid/tender/${id}`, method: 'get' })
export const getTendersByProject = (projectId: number) => request({ url: `/bid/tender/project/${projectId}`, method: 'get' })
export const getMyTender = (projectId: number) => request({ url: `/bid/tender/my/${projectId}`, method: 'get' })

// ========== 专家库 ==========
export const getExpertPage = (data: any) => request({ url: '/bid/expert/page', method: 'post', data })
export const getExpertById = (id: number) => request({ url: `/bid/expert/${id}`, method: 'get' })
export const saveExpert = (data: any) => request({ url: '/bid/expert/save', method: 'post', data })
export const updateExpert = (data: any) => request({ url: '/bid/expert/update', method: 'put', data })
export const deleteExpert = (id: number) => request({ url: `/bid/expert/delete/${id}`, method: 'delete' })
export const blacklistExpert = (id: number, reason: string) => request({ url: `/bid/expert/blacklist/${id}`, method: 'post', data: { reason } })
export const unblacklistExpert = (id: number) => request({ url: `/bid/expert/unblacklist/${id}`, method: 'post' })
export const selectExperts = (data: any) => request({ url: '/bid/expert/select', method: 'post', data })

// ========== 评标 ==========
export const saveEvaluation = (data: any) => request({ url: '/bid/evaluation/save', method: 'post', data })
export const submitEvaluation = (id: number) => request({ url: `/bid/evaluation/submit/${id}`, method: 'post' })
export const getEvaluationByProject = (projectId: number) => request({ url: `/bid/evaluation/project/${projectId}`, method: 'get' })
export const getMyEvaluation = (tenderId: number, expertId: number) => request({ url: '/bid/evaluation/my', method: 'get', params: { tenderId, expertId } })

// ========== 质疑投诉 ==========
export const getComplaintPage = (data: any) => request({ url: '/bid/complaint/page', method: 'post', data })
export const getMyComplaints = () => request({ url: '/bid/complaint/my', method: 'get' })
export const saveComplaint = (data: any) => request({ url: '/bid/complaint/save', method: 'post', data })
export const replyComplaint = (id: number, content: string) => request({ url: `/bid/complaint/reply/${id}`, method: 'post', data: { content } })
export const closeComplaint = (id: number) => request({ url: `/bid/complaint/close/${id}`, method: 'post' })
export const escalateComplaint = (id: number) => request({ url: `/bid/complaint/escalate/${id}`, method: 'post' })

// ========== 合同 ==========
export const getContractPage = (data: any) => request({ url: '/bid/contract/page', method: 'post', data })
export const getContractById = (id: number) => request({ url: `/bid/contract/${id}`, method: 'get' })
export const saveContract = (data: any) => request({ url: '/bid/contract/save', method: 'post', data })
export const updateContract = (data: any) => request({ url: '/bid/contract/update', method: 'put', data })
export const signContract = (id: number, signDate: string) => request({ url: `/bid/contract/sign/${id}`, method: 'post', data: { signDate } })
export const executeContract = (id: number) => request({ url: `/bid/contract/execute/${id}`, method: 'post' })
export const completeContract = (id: number) => request({ url: `/bid/contract/complete/${id}`, method: 'post' })
export const terminateContract = (id: number) => request({ url: `/bid/contract/terminate/${id}`, method: 'post' })

// ========== 字典/常量 ==========
export const purchaseTypeMap: Record<number, string> = { 1: '货物', 2: '服务', 3: '工程' }
export const bidMethodMap: Record<number, string> = { 1: '公开招标', 2: '邀请招标', 3: '竞争性谈判', 4: '询价', 5: '单一来源' }
export const projectStatusMap: Record<number, string> = { 0: '草稿', 1: '待审核', 2: '已发布', 3: '报名中', 4: '已截标', 5: '开标中', 6: '评标中', 7: '公示中', 8: '已定标', 9: '已归档', 10: '废标' }
export const projectStatusTagMap: Record<number, string> = { 0: 'info', 1: 'warning', 2: 'primary', 3: 'success', 4: '', 5: 'danger', 6: 'warning', 7: 'success', 8: 'success', 9: 'info', 10: 'danger' }
export const evalMethodMap: Record<number, string> = { 1: '综合评分法', 2: '最低价法', 3: '两阶段评标' }
export const registrationStatusMap: Record<number, string> = { 0: '待审核', 1: '已通过', 2: '已拒绝' }
export const tenderStatusMap: Record<number, string> = { 0: '草稿', 1: '已提交', 2: '已开标', 3: '资格审查中', 4: '通过', 5: '淘汰' }
export const contractStatusMap: Record<number, string> = { 0: '草稿', 1: '待签署', 2: '已签署', 3: '履行中', 4: '已完结', 5: '已解除' }
