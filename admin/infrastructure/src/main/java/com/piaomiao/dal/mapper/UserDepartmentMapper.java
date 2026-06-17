package com.piaomiao.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.piaomiao.dal.entity.UserDepartmentDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDepartmentMapper extends BaseMapper<UserDepartmentDO> {
    List<Long> selectDepartmentIdsByUserId(@Param("userId") Long userId);
    void deleteByUserId(@Param("userId") Long userId);
    void batchInsert(@Param("list") List<UserDepartmentDO> list);
}
