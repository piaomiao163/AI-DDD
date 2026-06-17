package com.piaomiao.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.piaomiao.dal.entity.UserDO;

/**
 * 用户Mapper
 */
public interface UserMapper extends BaseMapper<UserDO> {
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户实体
     */
    UserDO selectByUsername(String username);
}