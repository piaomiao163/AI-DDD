// 在浏览器控制台中运行此脚本来诊断 Activiti 属性提供器问题

// 1. 检查 modeler 是否有 activitiPropertiesProvider 服务
try {
  const provider = window.__bpmn_modeler?.get('activitiPropertiesProvider');
  console.log('[DIAG] activitiPropertiesProvider service:', provider);
} catch(e) {
  console.log('[DIAG] activitiPropertiesProvider NOT found in DI:', e.message);
}

// 2. 检查 propertiesPanel 服务
try {
  const pp = window.__bpmn_modeler?.get('propertiesPanel');
  console.log('[DIAG] propertiesPanel service:', pp);
  if (pp && pp._providers) {
    console.log('[DIAG] Registered providers:', pp._providers);
  }
} catch(e) {
  console.log('[DIAG] propertiesPanel NOT found:', e.message);
}

// 3. 检查 moddleExtensions 是否正确注册
try {
  const moddle = window.__bpmn_modeler?.get('moddle');
  console.log('[DIAG] moddle service:', moddle);
} catch(e) {
  console.log('[DIAG] moddle NOT found:', e.message);
}

// 4. 检查 translate 服务
try {
  const translate = window.__bpmn_modeler?.get('translate');
  console.log('[DIAG] translate("User task"):', translate('User task'));
  console.log('[DIAG] translate("Assignee"):', translate('Assignee'));
} catch(e) {
  console.log('[DIAG] translate NOT found:', e.message);
}

console.log('[DIAG] Done. Check results above.');
