package com.piaomiao.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.piaomiao.dal.entity.ProcessInstanceDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流程实例Mapper
 */
@Mapper
public interface ProcessInstanceMapper extends BaseMapper<ProcessInstanceDO> {
}
