package com.piaomiao.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

/**
 * 领域事件基类
 * <p>
 * 所有领域事件继承此类，通过 Spring ApplicationEvent 机制发布。
 * 使用 @TransactionalEventListener(phase = AFTER_COMMIT) 可在事务提交后处理。
 *
 * @param <T> 领域实体类型
 */
@Getter
public abstract class BaseDomainEvent<T> extends ApplicationEvent {

    /**
     * 事件唯一标识
     */
    private final String eventId;

    /**
     * 实体ID
     */
    private final Long entityId;

    /**
     * 实体类型简称
     */
    private final String entityType;

    /**
     * 事件发生时间
     */
    private final LocalDateTime occurredOn;

    /**
     * 关联的领域实体
     */
    private final T entity;

    protected BaseDomainEvent(Object source, T entity, Long entityId, String entityType) {
        super(source);
        this.eventId = java.util.UUID.randomUUID().toString();
        this.entityId = entityId;
        this.entityType = entityType;
        this.occurredOn = LocalDateTime.now();
        this.entity = entity;
    }
}
