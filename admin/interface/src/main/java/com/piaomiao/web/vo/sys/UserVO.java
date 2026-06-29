package com.piaomiao.web.vo.sys;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.piaomiao.dto.sys.UserDTO;
import com.piaomiao.model.sys.DepartmentModel;
import com.piaomiao.model.sys.PermissionModel;
import com.piaomiao.model.sys.RoleModel;
import com.piaomiao.model.sys.UserModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户VO
 */
@Data
public class UserVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码，仅允许请求写入，不允许响应输出
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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
     * 部门信息
     */
    private DepartmentInfo department;

    /**
     * 角色ID列表
     */
    private List<Long> roleIds;

    /**
     * 角色信息列表
     */
    private List<RoleInfo> roles;

    /**
     * 用户VO转DTO
     *
     * @param vo 用户VO
     * @return 用户DTO
     */
    public static UserDTO toDTO(UserVO vo) {
        UserDTO dto = new UserDTO();
        dto.setId(vo.getId());
        dto.setUsername(vo.getUsername());
        dto.setPassword(vo.getPassword());
        dto.setNickname(vo.getNickname());
        dto.setEmail(vo.getEmail());
        dto.setPhone(vo.getPhone());
        dto.setStatus(vo.getStatus());
        dto.setDepartmentId(vo.getDepartmentId());
        dto.setRoles(vo.getRoleIds());
        return dto;
    }

    /**
     * 用户模型转VO
     *
     * @param model 用户模型
     * @return 用户VO
     */
    public static UserVO fromModel(UserModel model) {
        if (model == null) {
            return null;
        }
        UserVO vo = new UserVO();
        vo.setId(model.getId());
        vo.setUsername(model.getUsername());
        vo.setNickname(model.getNickname());
        vo.setEmail(model.getEmail());
        vo.setPhone(model.getPhone());
        vo.setStatus(model.getStatus());
        vo.setDepartmentId(model.getDepartmentId());
        vo.setDepartment(DepartmentInfo.fromModel(model.getDepartment()));
        if (model.getRoles() != null) {
            vo.setRoles(model.getRoles().stream().map(RoleInfo::fromModel).collect(Collectors.toList()));
            vo.setRoleIds(model.getRoles().stream().map(RoleModel::getId).collect(Collectors.toList()));
        }
        return vo;
    }

    /**
     * 部门基础信息
     */
    @Data
    public static class DepartmentInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 部门ID
         */
        private Long id;

        /**
         * 部门名称
         */
        private String name;

        /**
         * 部门编码
         */
        private String code;

        /**
         * 部门模型转基础信息
         *
         * @param model 部门模型
         * @return 部门基础信息
         */
        public static DepartmentInfo fromModel(DepartmentModel model) {
            if (model == null) {
                return null;
            }
            DepartmentInfo info = new DepartmentInfo();
            info.setId(model.getId());
            info.setName(model.getName());
            info.setCode(model.getCode());
            return info;
        }
    }

    /**
     * 角色基础信息
     */
    @Data
    public static class RoleInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 角色ID
         */
        private Long id;

        /**
         * 角色名称
         */
        private String name;

        /**
         * 角色编码
         */
        private String code;

        /**
         * 权限列表
         */
        private List<PermissionInfo> permissions;

        /**
         * 角色模型转基础信息
         *
         * @param model 角色模型
         * @return 角色基础信息
         */
        public static RoleInfo fromModel(RoleModel model) {
            if (model == null) {
                return null;
            }
            RoleInfo info = new RoleInfo();
            info.setId(model.getId());
            info.setName(model.getName());
            info.setCode(model.getCode());
            if (model.getPermissions() != null) {
                info.setPermissions(model.getPermissions().stream()
                        .map(PermissionInfo::fromModel)
                        .collect(Collectors.toList()));
            }
            return info;
        }
    }

    /**
     * 权限基础信息
     */
    @Data
    public static class PermissionInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 权限ID
         */
        private Long id;

        /**
         * 权限名称
         */
        private String name;

        /**
         * 权限编码
         */
        private String code;

        /**
         * 权限类型
         */
        private Integer type;

        /**
         * 权限模型转基础信息
         *
         * @param model 权限模型
         * @return 权限基础信息
         */
        public static PermissionInfo fromModel(PermissionModel model) {
            if (model == null) {
                return null;
            }
            PermissionInfo info = new PermissionInfo();
            info.setId(model.getId());
            info.setName(model.getName());
            info.setCode(model.getCode());
            info.setType(model.getType());
            return info;
        }
    }
}
