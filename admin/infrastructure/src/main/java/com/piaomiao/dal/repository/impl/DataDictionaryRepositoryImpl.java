package com.piaomiao.dal.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.piaomiao.dal.entity.DataDictionaryDO;
import com.piaomiao.dal.mapper.DataDictionaryMapper;
import com.piaomiao.model.sys.DataDictionary;
import com.piaomiao.dal.util.AuditFieldUtil;
import com.piaomiao.repository.sys.DataDictionaryRepository;
import org.springframework.stereotype.Repository;
import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 数据字典仓库实现
 */
@Repository
public class DataDictionaryRepositoryImpl implements DataDictionaryRepository {
    
    @Resource
    private DataDictionaryMapper dataDictionaryMapper;
    
    @Override
    public DataDictionary save(DataDictionary dataDictionary) {
        DataDictionaryDO doObj = DataDictionaryDO.fromModel(dataDictionary);
        if (doObj.getId() == null) {
            AuditFieldUtil.fillForInsert(doObj);
            dataDictionaryMapper.insert(doObj);
        } else {
            AuditFieldUtil.fillForUpdate(doObj);
            dataDictionaryMapper.updateById(doObj);
        }
        return doObj.toModel();
    }
    
    @Override
    public Optional<DataDictionary> findById(Long id) {
        DataDictionaryDO doObj = dataDictionaryMapper.selectById(id);
        return doObj != null ? Optional.of(doObj.toModel()) : Optional.empty();
    }
    
    @Override
    public Optional<DataDictionary> findByCode(String code) {
        QueryWrapper<DataDictionaryDO> wrapper = new QueryWrapper<>();
        wrapper.eq("code", code);
        DataDictionaryDO doObj = dataDictionaryMapper.selectOne(wrapper);
        return doObj != null ? Optional.of(doObj.toModel()) : Optional.empty();
    }
    
    @Override
    public List<DataDictionary> findAll() {
        List<DataDictionaryDO> doList = dataDictionaryMapper.selectList(null);
        return doList.stream()
                .map(DataDictionaryDO::toModel)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        dataDictionaryMapper.deleteById(id);
    }
}