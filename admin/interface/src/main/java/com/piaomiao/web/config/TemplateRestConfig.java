package com.piaomiao.web.config;

import com.piaomiao.rest.TemplateRest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 事务配置类
 */
@Configuration
public class TemplateRestConfig {
    @Bean
    public TemplateRest templateRest(PlatformTransactionManager transactionManager, @org.springframework.beans.factory.annotation.Autowired(required = false) RedisTemplate<String, Object> redisTemplate) {
        TemplateRest templateRest = new TemplateRest();
        templateRest.setTransactionManager(transactionManager);
        if (redisTemplate != null) {
            templateRest.setRedisTemplate(redisTemplate);
        }
        return templateRest;
    }
}