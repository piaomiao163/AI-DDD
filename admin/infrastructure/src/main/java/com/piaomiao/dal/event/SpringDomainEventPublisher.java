package com.piaomiao.dal.event;

import com.piaomiao.event.BaseDomainEvent;
import com.piaomiao.event.DomainEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 基于 Spring ApplicationEventPublisher 的领域事件发布器实现
 */
@Component
public class SpringDomainEventPublisher implements DomainEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(BaseDomainEvent<?> event) {
        applicationEventPublisher.publishEvent(event);
    }
}
