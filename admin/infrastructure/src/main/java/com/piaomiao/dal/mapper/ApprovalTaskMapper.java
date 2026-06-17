package com.piaomiao.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.piaomiao.dal.entity.ApprovalTaskDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 审批任务Mapper
 *
 * @author system
 * @date 2026-06-14
 */
@Mapper
public interface ApprovalTaskMapper extends BaseMapper<ApprovalTaskDO> {
}
