package com.piaomiao.event;

/**
 * 领域事件发布器接口
 * <p>
 * 领域层通过此接口发布事件，基础设施层提供 Spring 实现。
 * 在 RepositoryImpl 中注入使用，避免 domain 层依赖 Spring。
 */
public interface DomainEventPublisher {

    /**
     * 发布领域事件
     *
     * @param event 领域事件
     */
    void publish(BaseDomainEvent<?> event);
}
