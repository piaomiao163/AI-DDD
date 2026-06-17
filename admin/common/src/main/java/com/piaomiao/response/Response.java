package com.piaomiao.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通用响应对象
 * @param <T> 响应数据类型
 */
@Data
public class Response<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 响应码
     */
    private int code;

    /**
     * 成功状态
     */
    private boolean success;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 时间戳
     */
    private LocalDateTime timestamp;

    /**
     * 构造方法
     */
    public Response() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * 构造方法
     */
    public Response(int code, boolean success, String message, T data) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * 创建成功响应
     */
    public static <T> Response<T> success(T data) {
        return new Response<>(200, true, "操作成功", data);
    }

    /**
     * 创建成功响应（无数据）
     */
    public static <T> Response<T> success() {
        return new Response<>(200, true, "操作成功", null);
    }

    /**
     * 创建失败响应
     */
    public static <T> Response<T> fail(int code, String message) {
        return new Response<>(code, false, message, null);
    }

    /**
     * 创建失败响应（默认错误码）
     */
    public static <T> Response<T> fail(String message) {
        return new Response<>(500, false, message, null);
    }
}