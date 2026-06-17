package com.piaomiao.dal.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.piaomiao.dal.entity.DataDictionaryDO;
import com.piaomiao.dal.entity.DataDictionaryItemDO;
import com.piaomiao.dal.mapper.DataDictionaryItemMapper;
import com.piaomiao.dal.mapper.DataDictionaryMapper;
import com.piaomiao.model.DataDictionaryItem;
import com.piaomiao.dal.util.AuditFieldUtil;
import com.piaomiao.repository.DataDictionaryItemRepository;
import org.springframework.stereotype.Repository;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 数据字典项仓库实现
 */
@Repository
public class DataDictionaryItemRepositoryImpl implements DataDictionaryItemRepository {
    
    @Resource
    private DataDictionaryItemMapper dataDictionaryItemMapper;
    
    @Resource
    private DataDictionaryMapper dataDictionaryMapper;
    
    @Override
    public DataDictionaryItem save(DataDictionaryItem item) {
        DataDictionaryItemDO doObj = DataDictionaryItemDO.fromModel(item);
        if (doObj.getId() == null) {
            AuditFieldUtil.fillForInsert(doObj);
            dataDictionaryItemMapper.insert(doObj);
        } else {
            AuditFieldUtil.fillForUpdate(doObj);
            dataDictionaryItemMapper.updateById(doObj);
        }
        return doObj.toModel();
    }
    
    @Override
    public Optional<DataDictionaryItem> findById(Long id) {
        DataDictionaryItemDO doObj = dataDictionaryItemMapper.selectById(id);
        return doObj != null ? Optional.of(doObj.toModel()) : Optional.empty();
    }
    
    @Override
    public List<DataDictionaryItem> findByDictionaryId(Long dictionaryId) {
        QueryWrapper<DataDictionaryItemDO> wrapper = new QueryWrapper<>();
        wrapper.eq("dictionary_id", dictionaryId);
        wrapper.orderByAsc("sort");
        List<DataDictionaryItemDO> doList = dataDictionaryItemMapper.selectList(wrapper);
        return doList.stream()
                .map(DataDictionaryItemDO::toModel)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<DataDictionaryItem> findEnabledByDictionaryCode(String dictionaryCode) {
        // 先查找字典
        QueryWrapper<DataDictionaryDO> dictWrapper = new QueryWrapper<>();
        dictWrapper.eq("code", dictionaryCode);
        DataDictionaryDO dictDO = dataDictionaryMapper.selectOne(dictWrapper);
        
        if (dictDO == null) {
            return Collections.emptyList();
        }
        
        // 再查找启用的字典项
        QueryWrapper<DataDictionaryItemDO> itemWrapper = new QueryWrapper<>();
        itemWrapper.eq("dictionary_id", dictDO.getId());
        itemWrapper.eq("status", 1);
        itemWrapper.orderByAsc("sort");
        List<DataDictionaryItemDO> doList = dataDictionaryItemMapper.selectList(itemWrapper);
        
        return doList.stream()
                .map(DataDictionaryItemDO::toModel)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        dataDictionaryItemMapper.deleteById(id);
    }
    
    @Override
    public void deleteByDictionaryId(Long dictionaryId) {
        QueryWrapper<DataDictionaryItemDO> wrapper = new QueryWrapper<>();
        wrapper.eq("dictionary_id", dictionaryId);
        dataDictionaryItemMapper.delete(wrapper);
    }
}