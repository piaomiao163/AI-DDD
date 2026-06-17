package com.piaomiao.rest;

import com.piaomiao.response.Response;
import lombok.Setter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TemplateRest extends AbstractTemplateRest implements Serializable {
    private static final long serialVersionUID = 1L;

    // 设置事务管理器
    @Setter
    private PlatformTransactionManager transactionManager;

    // 设置Redis模板，用于分布式锁
    @Setter
    private RedisTemplate<String, Object> redisTemplate;

    // 本地锁，用于防止同一JVM内的并发
    private final Lock localLock = new ReentrantLock();

    // 执行请求（无事务）
    public <T> Response<T> request(CallbackRest<T> callback) {
        try {
            callback.check();
            T t = callback.execute();
            return Response.success(t);
        } catch (Exception e) {
            return Response.fail("操作失败：" + e.getMessage());
        }
    }

    // 执行请求（可选事务）
    public <T> Response<T> request(CallbackRest<T> callback, boolean useTransaction) {
        if (useTransaction && transactionManager != null) {
            return requestWithTransaction(callback);
        } else {
            return request(callback);
        }
    }

    // 执行请求（带事务）
    private <T> Response<T> requestWithTransaction(CallbackRest<T> callback) {
        TransactionStatus status = null;
        try {
            // 1.开始事务
            status = transactionManager.getTransaction(new DefaultTransactionDefinition());

            // 2.校验参数
            callback.check();

            // 3.执行业务逻辑（包含领域事件发布，@TransactionalEventListener BEFORE_COMMIT 在此阶段触发）
            T t = callback.execute();

            // 4.提交事务（BEFORE_COMMIT 事件处理器在此之前已执行完毕）
            transactionManager.commit(status);

            // 5.包装返回值为Response
            return Response.success(t);
        } catch (Exception e) {
            // 6.异常时回滚事务（仅事务未完成时回滚）
            if (status != null && !status.isCompleted()) {
                transactionManager.rollback(status);
            }
            // 7.异常处理
            return Response.fail("操作失败：" + e.getMessage());
        }
    }

    // 执行请求（带分布式锁）
    public <T> Response<T> requestWithLock(CallbackRest<T> callback, String lockKey, long lockTimeout) {
        return requestWithLock(callback, lockKey, lockTimeout, false);
    }

    // 执行请求（带分布式锁和事务）
    public <T> Response<T> requestWithLock(CallbackRest<T> callback, String lockKey, long lockTimeout, boolean useTransaction) {
        if (lockKey == null || lockKey.isEmpty()) {
            return Response.fail("锁键不能为空");
        }

        // 1.尝试获取本地锁，防止同一JVM内的并发
        if (!localLock.tryLock()) {
            return Response.fail("操作过于频繁，请稍后重试");
        }

        boolean acquired = false;
        try {
            // 2.尝试获取分布式锁
            acquired = tryAcquireLock(lockKey, lockTimeout);
            if (!acquired) {
                return Response.fail("系统繁忙，请稍后重试");
            }

            // 3.根据是否需要事务执行请求
            if (useTransaction && transactionManager != null) {
                return requestWithTransaction(callback);
            } else {
                return this.request(callback);
            }
        } finally {
            // 4.释放分布式锁
            if (acquired) {
                releaseLock(lockKey);
            }
            // 5.释放本地锁
            localLock.unlock();
        }
    }

    // 尝试获取分布式锁
    private boolean tryAcquireLock(String lockKey, long lockTimeout) {
        if (redisTemplate == null) {
            return true;
        }

        try {
            Boolean result = redisTemplate.opsForValue()
                    .setIfAbsent(lockKey, "locked", lockTimeout, TimeUnit.SECONDS);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            return true;
        }
    }

    // 释放分布式锁
    private void releaseLock(String lockKey) {
        if (redisTemplate != null) {
            try {
                redisTemplate.delete(lockKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
