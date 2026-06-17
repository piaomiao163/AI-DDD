<template>
  <div class="process-design">
    <!-- 顶部工具栏 -->
    <div class="design-header">
      <div class="header-left">
        <el-button
          text
          @click="back"
        >
          <el-icon>
            <ArrowLeft />
          </el-icon>
          返回
        </el-button>
        <el-divider direction="vertical" />
        <div class="process-info">
          <el-input
            label="流程名称"
            v-model="processForm.name"
            placeholder="请输入流程名称"
            class="w-200"
          />
          <el-input
            label="流程标识"
            v-model="processForm.processKey"
            placeholder="请输入流程标识,确保唯一不可重复!(必填)"
            class="w-200"
          />
          <el-tag
            v-if="processId"
            size="small"
            type="success"
            effect="plain"
          >已保存</el-tag>
          <el-tag
            v-else
            size="small"
            type="info"
            effect="plain"
          >新建</el-tag>
        </div>
      </div>

      <div class="header-right">
        <el-button @click="importXml">
          <el-icon><Upload/></el-icon>
          导入XML
        </el-button>
        <el-button @click="exportXml">
          <el-icon><Document/></el-icon>
          导出XML
        </el-button>
        <el-button @click="clearProcess">
          <el-icon><Delete/></el-icon>
          清空
        </el-button>
        <el-divider direction="vertical"/>
        <el-button type="primary" @click="saveProcess" :loading="saving">
          <el-icon><Check/></el-icon>
          保存
        </el-button>
        <el-button type="success" @click="publishProcess" :disabled="!processId" :loading="publishing">
          <el-icon><Share/></el-icon>
          发布
        </el-button>
      </div>
    </div>

    <!-- 主体内容 -->
    <div class="design-body">
      <!-- bpmn-js 画布 -->
      <div
        ref="canvasRef"
        id="js-properties-panel"
        class="bpmn-canvas"
      ></div>
      <!-- 属性面板 -->
      <div
        ref="propertiesPanelRef"
        class="properties-panel-container"
      ></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, reactive } from 'vue'
import { Check, ArrowLeft, Delete, Share, Document, Upload } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter, useRoute } from 'vue-router'
import * as processApi from '@/api/process'
import BpmnModeler from 'bpmn-js/lib/Modeler'
import {
  BpmnPropertiesPanelModule,
  BpmnPropertiesProviderModule,
  CamundaPlatformPropertiesProviderModule
} from 'bpmn-js-properties-panel'
import camundaModdleDescriptor from 'camunda-bpmn-moddle/resources/camunda.json'

import customTranslateModule from '@/utils/customTranslateModule'
import '@/styles/css/process/design.css'
import 'bpmn-js/dist/assets/diagram-js.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css'
import '@bpmn-io/properties-panel/dist/assets/properties-panel.css'
// import 'bpmn-js-properties-panel/dist/assets/bpmn-js-properties-panel.css' // 右边工具栏样式

const router = useRouter()
const route = useRoute()
// bpmn-js 画布
const canvasRef = ref<HTMLDivElement>()
// 属性面板
const propertiesPanelRef = ref<HTMLDivElement>()

let modeler: any = null

const processId = ref<string>('')
const processForm = reactive({
  name: '',
  processKey: '',
  category: '',
  description: ''
})
const saving = ref(false)
const publishing = ref(false)
const initBpmnModeler = async () => {
  try {
    if (!canvasRef.value || !propertiesPanelRef.value) return
    // 初始化bpmn-modeler
    modeler = new BpmnModeler({
      // 容器
      container: canvasRef.value,
      // 属性面板
      propertiesPanel: {
        parent: propertiesPanelRef.value
      },
      // bpmn-js-properties-panel模块
      additionalModules: [
        BpmnPropertiesPanelModule,
        BpmnPropertiesProviderModule,
        CamundaPlatformPropertiesProviderModule,
        customTranslateModule
      ],
      // 模型扩展：camunda 给属性面板用，activiti 兼容后端已有的 XML
      moddleExtensions: {
        activiti: camundaModdleDescriptor
      }
    })

    await modeler.createDiagram()
    const canvas = modeler.get('canvas')
    canvas.zoom('fit-viewport', 'auto')

    const id = route.query.id as string
    if (id) {
      await loadProcess(Number(id))
    }

    ElMessage.success('流程设计器初始化成功')
  } catch (error) {
    console.error('初始化失败:', error)
    ElMessage.error(`流程设计器初始化失败: ${(error as Error).message}`)
  }
}

// 加载流程
const loadProcess = async (id: number) => {
  try {
    const response = await processApi.getProcessById(id)
    const data = response.data
    processForm.name = data.name
    processForm.processKey = data.processKey
    processForm.description = data.description || ''
    processForm.category = data.category || ''
    processId.value = String(data.id)

    if (data.xml) {
      await modeler.importXML(data.xml)
      const canvas = modeler.get('canvas')
      canvas.zoom('fit-viewport', 'auto')
    }
  } catch (error) {
    console.error('加载流程失败:', error)
    ElMessage.error('加载流程失败')
  }
}

// 保存流程
const saveProcess = async () => {
  if (!processForm.name) {
    ElMessage.warning('请输入流程名称')
    return
  }
  if (!processForm.processKey) {
    ElMessage.warning('请输入流程标识')
    return
  }

  saving.value = true
  try {
    // 保存XML
    const { xml } = await modeler.saveXML({ format: true })
    const processData: any = {
      name: processForm.name,
      processKey: processForm.processKey,
      description: processForm.description,
      category: processForm.category,
      xml: xml
    }

    if (processId.value) {
      processData.id = Number(processId.value)
      await processApi.updateProcess(processData)
    } else {
      const res = await processApi.saveProcess(processData)
      if (res.data && res.data.id) {
        processId.value = String(res.data.id)
      }
    }
    ElMessage.success('保存成功')
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}
// 发布流程
const publishProcess = async () => {
  if (!processId.value) {
    ElMessage.warning('请先保存流程')
    return
  }

  try {
    await ElMessageBox.confirm('确定要发布该流程吗？发布后流程将生效。', '发布确认', {
      confirmButtonText: '确定发布',
      cancelButtonText: '取消',
      type: 'warning'
    })
  } catch {
    return
  }

  publishing.value = true
  try {
    const { xml } = await modeler.saveXML({ format: true })
    // 先更新流程
    await processApi.updateProcess({ id: Number(processId.value), xml } as any)
    // 再发布流程
    await processApi.publishProcess(Number(processId.value))
    ElMessage.success('发布成功')
  } catch (error) {
    console.error('发布失败:', error)
    ElMessage.error('发布失败')
  } finally {
    publishing.value = false
  }
}
// 导入BPMN XML：选择本地 .bpmn/.xml 文件并加载到设计器画布
const importXml = () => {
  const input = document.createElement('input')
  input.type = 'file'
  input.accept = '.bpmn,.xml,.bpmn20.xml,application/xml,text/xml'
  input.onchange = async (e) => {
    const file = (e.target as HTMLInputElement).files?.[0]
    if (!file) return
    try {
      const xml = await file.text()
      await modeler.importXML(xml)
      const canvas = modeler.get('canvas')
      canvas.zoom('fit-viewport', 'auto')
      ElMessage.success('导入XML成功')
    } catch (error) {
      console.error('导入XML失败:', error)
      ElMessage.error('导入XML失败，请检查文件是否为合法的 BPMN XML')
    }
  }
  input.click()
}
// 导出BPMN XML
const exportXml = async () => {
  try {
    const { xml } = await modeler.saveXML({ format: true })
    const blob = new Blob([xml], { type: 'application/xml' })
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `${processForm.name || 'process'}.bpmn20.xml`
    a.click()
    URL.revokeObjectURL(url)
    ElMessage.success('BPMN XML导出成功')
  } catch (error) {
    ElMessage.error('导出失败')
  }
}
// 清空流程图
const clearProcess = () => {
  ElMessageBox.confirm('确定要清空当前流程图吗？', '提示', { type: 'warning' })
    .then(async () => {
      await modeler.createDiagram()
      const canvas = modeler.get('canvas')
      canvas.zoom('fit-viewport', 'auto')
      ElMessage.success('流程图已清空')
    })
    .catch(() => {})
}
// 返回
const back = () => {
  router.back()
}
// 初始化
onMounted(() => {
  initBpmnModeler()
})

// 组件卸载时清理
onBeforeUnmount(() => {
  if (modeler) {
    modeler.destroy()
    modeler = null
  }
})
</script>

