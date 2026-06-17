package com.piaomiao.repository;

import com.piaomiao.model.DataDictionaryItem;
import java.util.List;
import java.util.Optional;

/**
 * 数据字典项仓库接口
 */
public interface DataDictionaryItemRepository {
    /**
     * 保存数据字典项
     */
    DataDictionaryItem save(DataDictionaryItem item);
    
    /**
     * 根据ID查找数据字典项
     */
    Optional<DataDictionaryItem> findById(Long id);
    
    /**
     * 根据字典ID查找所有字典项
     */
    List<DataDictionaryItem> findByDictionaryId(Long dictionaryId);
    
    /**
     * 根据字典编码查找所有启用的字典项
     */
    List<DataDictionaryItem> findEnabledByDictionaryCode(String dictionaryCode);
    
    /**
     * 删除数据字典项
     */
    void deleteById(Long id);
    
    /**
     * 根据字典ID删除所有字典项
     */
    void deleteByDictionaryId(Long dictionaryId);
}