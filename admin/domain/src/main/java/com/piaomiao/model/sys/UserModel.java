package com.piaomiao.model.sys;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.piaomiao.enums.DateFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户模型
 */
@Data
public class UserModel {
    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 部门ID
     */
    private Long departmentId;

    /**
     * 部门
     */
    private DepartmentModel department;

    /**
     * 角色列表
     */
    private java.util.List<RoleModel> roles;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateFormat.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = DateFormat.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 是否删除
     */
    private Integer deleted;
}
