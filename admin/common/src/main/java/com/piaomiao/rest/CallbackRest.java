package com.piaomiao.rest;

public interface CallbackRest<T> {
    // 校验参数
    void check();
    // 执行业务逻辑
    T execute();
}