package com.piaomiao.web.config;

import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Activiti配置 - 仅定制化，不覆盖自动配置的Bean
 */
@Configuration
public class ActivitiConfig implements ProcessEngineConfigurationConfigurer {

    @Override
    public void configure(SpringProcessEngineConfiguration config) {
        // 使用UUID生成器替代默认的DbIdGenerator
        config.setIdGenerator(new org.activiti.engine.impl.persistence.StrongUuidGenerator());
    }

    @Bean
    public ProcessDiagramGenerator processDiagramGenerator() {
        return new DefaultProcessDiagramGenerator();
    }
}
