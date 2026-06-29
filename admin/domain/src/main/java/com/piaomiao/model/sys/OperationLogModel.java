package com.piaomiao.model.sys;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 操作日志模型
 */
@Data
public class OperationLogModel {
    /**
     * 日志ID
     */
    private Long id;
    
    /**
     * 操作模块
     */
    private String module;
    
    /**
     * 操作类型
     */
    private String type;
    
    /**
     * 操作描述
     */
    private String description;
    
    /**
     * 请求方法
     */
    private String requestMethod;
    
    /**
     * 请求URL
     */
    private String requestUrl;
    
    /**
     * 请求参数
     */
    private String requestParams;
    
    /**
     * 响应结果
     */
    private String responseResult;
    
    /**
     * 操作人ID
     */
    private Long userId;
    
    /**
     * 操作人用户名
     */
    private String username;
    
    /**
     * 操作IP地址
     */
    private String ipAddress;
    
    /**
     * 操作地点
     */
    private String location;
    
    /**
     * 操作状态（0失败 1成功）
     */
    private Integer status;
    
    /**
     * 错误消息
     */
    private String errorMsg;
    
    /**
     * 执行时长（毫秒）
     */
    private Long executionTime;
    
    /**
     * 操作时间
     */
    private LocalDateTime operationTime;
}
