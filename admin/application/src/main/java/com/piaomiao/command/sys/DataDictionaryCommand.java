package com.piaomiao.command.sys;

import com.piaomiao.dto.sys.DataDictionaryDTO;
import com.piaomiao.dto.sys.DataDictionaryItemDTO;
import com.piaomiao.model.sys.DataDictionary;
import com.piaomiao.model.sys.DataDictionaryItem;
import java.util.List;

/**
 * 数据字典命令接口
 */
public interface DataDictionaryCommand {
    /**
     * 创建数据字典
     */
    DataDictionary createDataDictionary(DataDictionaryDTO dto);
    
    /**
     * 更新数据字典
     */
    DataDictionary updateDataDictionary(DataDictionaryDTO dto);
    
    /**
     * 删除数据字典
     */
    void deleteDataDictionary(Long id);
    
    /**
     * 根据ID查询数据字典
     */
    DataDictionary getDataDictionaryById(Long id);
    
    /**
     * 查询所有数据字典
     */
    List<DataDictionary> getAllDataDictionaries();
    
    /**
     * 创建数据字典项
     */
    DataDictionaryItem createDataDictionaryItem(DataDictionaryItemDTO dto);
    
    /**
     * 更新数据字典项
     */
    DataDictionaryItem updateDataDictionaryItem(DataDictionaryItemDTO dto);
    
    /**
     * 删除数据字典项
     */
    void deleteDataDictionaryItem(Long id);
    
    /**
     * 根据字典ID查询字典项
     */
    List<DataDictionaryItem> getDataDictionaryItemsByDictionaryId(Long dictionaryId);
    
    /**
     * 根据字典编码查询启用的字典项
     */
    List<DataDictionaryItem> getEnabledDataDictionaryItemsByCode(String dictionaryCode);
}
