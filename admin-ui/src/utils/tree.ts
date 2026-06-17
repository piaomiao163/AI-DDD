/**
 * 通用树形结构构建工具
 */

interface TreeNode {
  id: number | string
  parentId?: number | string | null
  sort?: number
  children?: TreeNode[]
  [key: string]: any
}

type TreeNodeWithChildren<T extends TreeNode> = Omit<T, 'children'> & {
  children: TreeNodeWithChildren<T>[]
}

/**
 * 将扁平列表构建为树形结构
 * @param data 扁平数据列表
 * @param options 配置项
 * @param options.sortByKey 排序字段，默认 'sort'，传 null 不排序
 * @param options.rootFilter 根节点过滤函数，返回 true 的根节点会被保留
 * @returns 树形结构
 */
export function buildTree<T extends TreeNode>(
  data: T[],
  options?: {
    sortByKey?: string | null
    rootFilter?: (node: TreeNodeWithChildren<T>) => boolean
  }
): TreeNodeWithChildren<T>[] {
  if (!data || data.length === 0) return []

  const sortByKey = options?.sortByKey ?? 'sort'
  const rootFilter = options?.rootFilter

  const map = new Map<number | string, TreeNodeWithChildren<T>>()
  const roots: TreeNodeWithChildren<T>[] = []

  data.forEach(item => {
    map.set(item.id, { ...item, children: [] } as TreeNodeWithChildren<T>)
  })

  data.forEach(item => {
    if (item.parentId && map.has(item.parentId)) {
      map.get(item.parentId)!.children.push(map.get(item.id)!)
    } else {
      roots.push(map.get(item.id)!)
    }
  })

  if (sortByKey) {
    const sortTree = (nodes: TreeNodeWithChildren<T>[]) => {
      nodes.sort((a, b) => ((a as any)[sortByKey] ?? 0) - ((b as any)[sortByKey] ?? 0))
      nodes.forEach(node => {
        if (node.children && node.children.length > 0) {
          sortTree(node.children)
        }
      })
    }
    sortTree(roots)
  }

  if (rootFilter) {
    return roots.filter(rootFilter)
  }

  return roots
}
