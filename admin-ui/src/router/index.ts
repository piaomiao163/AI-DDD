import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'
import { usePermissionStore } from '@/store/permission'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', keepAlive: false }
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/layout/index.vue'),
    redirect: '/dashboard',
    meta: { title: '首页', keepAlive: true },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页', icon: 'House', keepAlive: true }
      },
      {
        path: 'system/user',
        name: 'User',
        component: () => import('@/views/system/user/index.vue'),
        meta: { title: '用户管理', icon: 'User', keepAlive: true, permission: 'system:user:manage' }
      },
      {
        path: 'system/role',
        name: 'Role',
        component: () => import('@/views/system/role/index.vue'),
        meta: { title: '角色管理', icon: 'UserFilled', keepAlive: true, permission: 'system:role:manage' }
      },
      {
        path: 'system/menu',
        name: 'Menu',
        component: () => import('@/views/system/menu/index.vue'),
        meta: { title: '菜单管理', icon: 'Menu', keepAlive: true, permission: 'system:menu:manage' }
      },
      {
        path: 'system/dept',
        name: 'Dept',
        component: () => import('@/views/system/dept/index.vue'),
        meta: { title: '部门管理', icon: 'OfficeBuilding', keepAlive: true, permission: 'system:department:manage' }
      },
      {
        path: 'system/permission',
        name: 'Permission',
        component: () => import('@/views/system/permission/index.vue'),
        meta: { title: '权限管理', icon: 'Key', keepAlive: true, permission: 'system:permission:manage' }
      },
      {
        path: 'system/dataDictionary',
        name: 'DataDictionary',
        component: () => import('@/views/system/dataDictionary/index.vue'),
        meta: { title: '数据字典', icon: 'Grid', keepAlive: true, permission: 'system:dataDictionary:manage' }
      },
      {
        path: 'system/operationLog',
        name: 'OperationLog',
        component: () => import('@/views/system/operationLog/index.vue'),
        meta: { title: '操作日志', icon: 'Document', keepAlive: false, permission: 'system:operationLog:manage' }
      },
      {
        path: 'system/post',
        name: 'Post',
        component: () => import('@/views/system/post/index.vue'),
        meta: { title: '岗位管理', icon: 'Post', keepAlive: true, permission: 'system:post:manage' }
      },
      {
        path: 'process',
        name: 'Process',
        component: () => import('@/views/process/index.vue'),
        meta: { title: '流程定义', icon: 'DataBoard', keepAlive: true, permission: 'process:definition:list' }
      },
      {
        path: 'process/design',
        name: 'ProcessDesign',
        component: () => import('@/views/process/design.vue'),
        meta: { title: '流程设计', icon: 'EditPen', keepAlive: true, permission: 'process:definition:edit' }
      },
      {
        path: 'process/start',
        name: 'ProcessStart',
        component: () => import('@/views/process/start.vue'),
        meta: { title: '发起流程', icon: 'Position', keepAlive: true, permission: 'process:instance:start' }
      },
      {
        path: 'process/my-instances',
        name: 'MyInstances',
        component: () => import('@/views/process/myInstances.vue'),
        meta: { title: '我的流程', icon: 'List', keepAlive: true, permission: 'process:instance:my' }
      },
      {
        path: 'process/todo',
        name: 'TodoTasks',
        component: () => import('@/views/process/todoTasks.vue'),
        meta: { title: '待办任务', icon: 'Tickets', keepAlive: true, permission: 'task:todo' }
      },
      {
        path: 'process/diagram/:id',
        name: 'ProcessDiagram',
        component: () => import('@/views/process/diagram.vue'),
        meta: { title: '流程图', icon: 'Picture', keepAlive: false, permission: 'process:diagram:view' }
      },
      {
        path: 'leave/apply',
        name: 'LeaveApply',
        component: () => import('@/views/leave/apply.vue'),
        meta: { title: '请假申请', icon: 'Calendar', keepAlive: true, permission: 'leave:apply' }
      },
      {
        path: 'leave/my',
        name: 'MyLeave',
        component: () => import('@/views/leave/myList.vue'),
        meta: { title: '我的请假', icon: 'List', keepAlive: true, permission: 'leave:my:list' }
      },
      {
        path: 'leave/approval',
        name: 'LeaveApproval',
        component: () => import('@/views/leave/approval.vue'),
        meta: { title: '请假审批', icon: 'Stamp', keepAlive: true, permission: 'leave:approval:list' }
      },
      {
        path: 'leave/manage',
        name: 'LeaveManage',
        component: () => import('@/views/leave/index.vue'),
        meta: { title: '请假管理', icon: 'DataBoard', keepAlive: true, permission: 'leave:manage' }
      },
      // 招投标管理
      {
        path: 'bid/project',
        name: 'BidProject',
        component: () => import('@/views/bid/project/index.vue'),
        meta: { title: '项目管理', icon: 'Document', keepAlive: true, permission: 'bid:project:list' }
      },
      {
        path: 'bid/project/form',
        name: 'BidProjectForm',
        component: () => import('@/views/bid/project/form.vue'),
        meta: { title: '新建项目', icon: 'Document', keepAlive: false, permission: 'bid:project:add' }
      },
      {
        path: 'bid/project/form/:id',
        name: 'BidProjectEdit',
        component: () => import('@/views/bid/project/form.vue'),
        meta: { title: '编辑项目', icon: 'Document', keepAlive: false, permission: 'bid:project:edit' }
      },
      {
        path: 'bid/project/detail/:id',
        name: 'BidProjectDetail',
        component: () => import('@/views/bid/project/detail.vue'),
        meta: { title: '项目详情', icon: 'Document', keepAlive: false, permission: 'bid:project:view' }
      },
      {
        path: 'bid/hall',
        name: 'BidHall',
        component: () => import('@/views/bid/hall/index.vue'),
        meta: { title: '招标大厅', icon: 'Shop', keepAlive: true, permission: 'bid:hall:view' }
      },
      {
        path: 'bid/hall/apply/:projectId',
        name: 'BidApply',
        component: () => import('@/views/bid/hall/apply.vue'),
        meta: { title: '报名', icon: 'Shop', keepAlive: false, permission: 'bid:registration:add' }
      },
      {
        path: 'bid/tender',
        name: 'BidTender',
        component: () => import('@/views/bid/tender/index.vue'),
        meta: { title: '我的投标', icon: 'Tickets', keepAlive: true, permission: 'bid:tender:list' }
      },
      {
        path: 'bid/tender/form/:projectId',
        name: 'BidTenderForm',
        component: () => import('@/views/bid/tender/form.vue'),
        meta: { title: '投标文件', icon: 'Tickets', keepAlive: false, permission: 'bid:tender:edit' }
      },
      {
        path: 'bid/expert',
        name: 'BidExpert',
        component: () => import('@/views/bid/expert/index.vue'),
        meta: { title: '专家库', icon: 'User', keepAlive: true, permission: 'bid:expert:list' }
      },
      {
        path: 'bid/evaluation',
        name: 'BidEvaluation',
        component: () => import('@/views/bid/evaluation/index.vue'),
        meta: { title: '评标任务', icon: 'Star', keepAlive: true, permission: 'bid:evaluation:list' }
      },
      {
        path: 'bid/evaluation/score/:projectId',
        name: 'BidScore',
        component: () => import('@/views/bid/evaluation/score.vue'),
        meta: { title: '评分', icon: 'Star', keepAlive: false, permission: 'bid:evaluation:score' }
      },
      {
        path: 'bid/complaint',
        name: 'BidComplaint',
        component: () => import('@/views/bid/complaint/index.vue'),
        meta: { title: '质疑投诉', icon: 'Warning', keepAlive: true, permission: 'bid:complaint:list' }
      },
      {
        path: 'bid/contract',
        name: 'BidContract',
        component: () => import('@/views/bid/contract/index.vue'),
        meta: { title: '合同管理', icon: 'Document', keepAlive: true, permission: 'bid:contract:list' }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/404/index.vue'),
    meta: { title: '页面不存在', keepAlive: false }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(_to, _from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  }
})

router.beforeEach(async (to, _from, next) => {
  document.title = `${to.meta.title || '后台管理系统'} - AI-DDD`

  const userStore = useUserStore()
  const permissionStore = usePermissionStore()

  if (to.path === '/login') {
    next()
  } else {
    if (userStore.token) {
      if (!userStore.hasUserInfo) {
        const loaded = await userStore.getUserInfo()
        if (!loaded) {
          userStore.logout()
          next('/login')
          return
        }
      }
      if (!userStore.token) {
        next('/login')
        return
      }
      const requiredPermission = to.meta.permission as string | undefined
      if (requiredPermission && !permissionStore.hasPermission(requiredPermission)) {
        ElMessage.warning('暂无权限访问该菜单')
        next('/dashboard')
        return
      }
      next()
    } else {
      next('/login')
    }
  }
})

router.afterEach(() => {
})

export default router
