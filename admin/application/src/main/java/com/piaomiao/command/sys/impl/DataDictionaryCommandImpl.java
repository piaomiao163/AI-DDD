package com.piaomiao.command.sys.impl;

import com.piaomiao.command.sys.DataDictionaryCommand;
import com.piaomiao.dto.sys.DataDictionaryDTO;
import com.piaomiao.dto.sys.DataDictionaryItemDTO;
import com.piaomiao.model.sys.DataDictionary;
import com.piaomiao.model.sys.DataDictionaryItem;
import com.piaomiao.repository.sys.DataDictionaryRepository;
import com.piaomiao.repository.sys.DataDictionaryItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 数据字典命令实现
 */
@Service
public class DataDictionaryCommandImpl implements DataDictionaryCommand {
    
    @Resource
    private DataDictionaryRepository dataDictionaryRepository;
    
    @Resource
    private DataDictionaryItemRepository dataDictionaryItemRepository;
    
    @Override
    @Transactional
    public DataDictionary createDataDictionary(DataDictionaryDTO dto) {
        DataDictionary dataDictionary = new DataDictionary();
        dataDictionary.setName(dto.getName());
        dataDictionary.setCode(dto.getCode());
        dataDictionary.setDescription(dto.getDescription());
        dataDictionary.setStatus(dto.getStatus());
        dataDictionary.setCreateTime(LocalDateTime.now());
        dataDictionary.setUpdateTime(LocalDateTime.now());
        return dataDictionaryRepository.save(dataDictionary);
    }
    
    @Override
    @Transactional
    public DataDictionary updateDataDictionary(DataDictionaryDTO dto) {
        DataDictionary dataDictionary = dataDictionaryRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("数据字典不存在"));
        dataDictionary.setName(dto.getName());
        dataDictionary.setCode(dto.getCode());
        dataDictionary.setDescription(dto.getDescription());
        dataDictionary.setStatus(dto.getStatus());
        dataDictionary.setUpdateTime(LocalDateTime.now());
        return dataDictionaryRepository.save(dataDictionary);
    }
    
    @Override
    @Transactional
    public void deleteDataDictionary(Long id) {
        // 先删除字典项
        dataDictionaryItemRepository.deleteByDictionaryId(id);
        // 再删除字典
        dataDictionaryRepository.deleteById(id);
    }
    
    @Override
    public DataDictionary getDataDictionaryById(Long id) {
        return dataDictionaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("数据字典不存在"));
    }
    
    @Override
    public List<DataDictionary> getAllDataDictionaries() {
        return dataDictionaryRepository.findAll();
    }
    
    @Override
    @Transactional
    public DataDictionaryItem createDataDictionaryItem(DataDictionaryItemDTO dto) {
        DataDictionaryItem item = new DataDictionaryItem();
        item.setDictionaryId(dto.getDictionaryId());
        item.setName(dto.getName());
        item.setValue(dto.getValue());
        item.setSort(dto.getSort());
        item.setStatus(dto.getStatus());
        item.setCreateTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        return dataDictionaryItemRepository.save(item);
    }
    
    @Override
    @Transactional
    public DataDictionaryItem updateDataDictionaryItem(DataDictionaryItemDTO dto) {
        DataDictionaryItem item = dataDictionaryItemRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("数据字典项不存在"));
        item.setDictionaryId(dto.getDictionaryId());
        item.setName(dto.getName());
        item.setValue(dto.getValue());
        item.setSort(dto.getSort());
        item.setStatus(dto.getStatus());
        item.setUpdateTime(LocalDateTime.now());
        return dataDictionaryItemRepository.save(item);
    }
    
    @Override
    @Transactional
    public void deleteDataDictionaryItem(Long id) {
        dataDictionaryItemRepository.deleteById(id);
    }
    
    @Override
    public List<DataDictionaryItem> getDataDictionaryItemsByDictionaryId(Long dictionaryId) {
        return dataDictionaryItemRepository.findByDictionaryId(dictionaryId);
    }
    
    @Override
    public List<DataDictionaryItem> getEnabledDataDictionaryItemsByCode(String dictionaryCode) {
        return dataDictionaryItemRepository.findEnabledByDictionaryCode(dictionaryCode);
    }
}