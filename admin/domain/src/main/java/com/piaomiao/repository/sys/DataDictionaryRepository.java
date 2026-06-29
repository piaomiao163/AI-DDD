package com.piaomiao.repository.sys;

import com.piaomiao.model.sys.DataDictionary;
import java.util.List;
import java.util.Optional;

/**
 * 数据字典仓库接口
 */
public interface DataDictionaryRepository {
    /**
     * 保存数据字典
     */
    DataDictionary save(DataDictionary dataDictionary);
    
    /**
     * 根据ID查找数据字典
     */
    Optional<DataDictionary> findById(Long id);
    
    /**
     * 根据编码查找数据字典
     */
    Optional<DataDictionary> findByCode(String code);
    
    /**
     * 查找所有数据字典
     */
    List<DataDictionary> findAll();
    
    /**
     * 删除数据字典
     */
    void deleteById(Long id);
}