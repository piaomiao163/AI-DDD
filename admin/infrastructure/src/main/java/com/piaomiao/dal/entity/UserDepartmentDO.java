package com.piaomiao.dal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_user_department")
public class UserDepartmentDO {
    private Long id;
    private Long userId;
    private Long departmentId;
}
