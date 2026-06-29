package com.piaomiao.dal.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 用户实体
 */
@TableName("sys_user")
public class UserDO {
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
     * 状态：0-禁用，1-启用
     */
    private Integer status;
    
    /**
     * 部门ID
     */
    private Long departmentId;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
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
    @TableLogic
    private Integer deleted;

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    /**
     * 转换为模型
     * @return 用户模型
     */
    public com.piaomiao.model.sys.UserModel toModel() {
        com.piaomiao.model.sys.UserModel userModel = new com.piaomiao.model.sys.UserModel();
        userModel.setId(id);
        userModel.setUsername(username);
        userModel.setPassword(password);
        userModel.setNickname(nickname);
        userModel.setEmail(email);
        userModel.setPhone(phone);
        userModel.setStatus(status);
        userModel.setDepartmentId(departmentId);
        userModel.setCreateTime(createTime);
        userModel.setUpdateTime(updateTime);
        userModel.setCreateBy(createBy);
        userModel.setUpdateBy(updateBy);
        userModel.setDeleted(deleted);
        return userModel;
    }

    /**
     * 从模型转换
     * @param userModel 用户模型
     * @return 用户实体
     */
    public static UserDO fromModel(com.piaomiao.model.sys.UserModel userModel) {
        UserDO userDO = new UserDO();
        userDO.setId(userModel.getId());
        userDO.setUsername(userModel.getUsername());
        userDO.setPassword(userModel.getPassword());
        userDO.setNickname(userModel.getNickname());
        userDO.setEmail(userModel.getEmail());
        userDO.setPhone(userModel.getPhone());
        userDO.setStatus(userModel.getStatus());
        userDO.setDepartmentId(userModel.getDepartmentId());
        userDO.setCreateTime(userModel.getCreateTime());
        userDO.setUpdateTime(userModel.getUpdateTime());
        userDO.setCreateBy(userModel.getCreateBy());
        userDO.setUpdateBy(userModel.getUpdateBy());
        userDO.setDeleted(userModel.getDeleted());
        return userDO;
    }
}
