package com.piaomiao.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.piaomiao.dal.entity.PostDO;

/**
 * 岗位数据访问接口
 */
public interface PostMapper extends BaseMapper<PostDO> {
    /**
     * 根据编码查询岗位
     * @param code 岗位编码
     * @return 岗位数据访问对象
     */
    PostDO selectByCode(String code);
}
