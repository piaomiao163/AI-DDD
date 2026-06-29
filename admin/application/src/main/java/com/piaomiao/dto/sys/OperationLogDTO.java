package com.piaomiao.dto.sys;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OperationLogDTO {
    private Long id;
    private String module;
    private String type;
    private String description;
    private String requestMethod;
    private String requestUrl;
    private String requestParams;
    private String responseResult;
    private Long userId;
    private String username;
    private String ipAddress;
    private String location;
    private Integer status;
    private String errorMsg;
    private Long executionTime;
    private LocalDateTime operationTime;
}