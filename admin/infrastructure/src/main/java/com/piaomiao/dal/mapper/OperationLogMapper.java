package com.piaomiao.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.piaomiao.dal.entity.OperationLogDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志Mapper
 */
@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLogDO> {
}
