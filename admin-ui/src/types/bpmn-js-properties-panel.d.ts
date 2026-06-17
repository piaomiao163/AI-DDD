declare module 'bpmn-js-properties-panel' {
  export const BpmnPropertiesPanelModule: any
  export const BpmnPropertiesProviderModule: any
  export const CamundaPlatformPropertiesProviderModule: any
  export function useService(name: string): any
}

declare module 'camunda-bpmn-moddle/resources/camunda.json' {
  const value: Record<string, any>
  export default value
}
