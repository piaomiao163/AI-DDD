package com.piaomiao.web.vo.sys;

import com.piaomiao.dto.sys.OperationLogDTO;
import lombok.Data;

import java.io.Serializable;

@Data
public class OperationLogVO implements Serializable {
    private static final long serialVersionUID = 1L;

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
    private String operationTime;

    public static OperationLogDTO toDTO(OperationLogVO vo) {
        OperationLogDTO dto = new OperationLogDTO();
        dto.setId(vo.getId());
        dto.setModule(vo.getModule());
        dto.setType(vo.getType());
        dto.setDescription(vo.getDescription());
        dto.setRequestMethod(vo.getRequestMethod());
        dto.setRequestUrl(vo.getRequestUrl());
        dto.setRequestParams(vo.getRequestParams());
        dto.setResponseResult(vo.getResponseResult());
        dto.setUserId(vo.getUserId());
        dto.setUsername(vo.getUsername());
        dto.setIpAddress(vo.getIpAddress());
        dto.setLocation(vo.getLocation());
        dto.setStatus(vo.getStatus());
        dto.setErrorMsg(vo.getErrorMsg());
        dto.setExecutionTime(vo.getExecutionTime());
        return dto;
    }
}
