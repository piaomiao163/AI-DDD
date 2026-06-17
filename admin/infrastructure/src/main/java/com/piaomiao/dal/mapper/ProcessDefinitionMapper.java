package com.piaomiao.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.piaomiao.dal.entity.ProcessDefinitionDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流程定义Mapper接口
 */
@Mapper
public interface ProcessDefinitionMapper extends BaseMapper<ProcessDefinitionDO> {
}
