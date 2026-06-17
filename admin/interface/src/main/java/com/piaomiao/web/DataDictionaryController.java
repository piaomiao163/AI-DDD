package com.piaomiao.web;

import com.piaomiao.command.DataDictionaryCommand;
import com.piaomiao.model.DataDictionaryItem;
import com.piaomiao.model.DataDictionary;
import com.piaomiao.web.vo.DataDictionaryVO;
import com.piaomiao.web.vo.DataDictionaryItemVO;
import com.piaomiao.response.Response;
import com.piaomiao.rest.CallbackRest;
import com.piaomiao.rest.TemplateRest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据字典控制器
 */
@RestController
@RequestMapping("/dataDictionary")
public class DataDictionaryController {
    
    @Resource
    private DataDictionaryCommand dataDictionaryCommand;
    
    @Resource
    private TemplateRest templateRest;
    
    /**
     * 获取所有数据字典
     */
    @PreAuthorize("hasAuthority('system:dataDictionary:list')")
    @GetMapping("/list")
    public Response<List<DataDictionaryVO>> list() {
        return templateRest.request(new CallbackRest<List<DataDictionaryVO>>() {
            @Override
            public void check() {
            }

            @Override
            public List<DataDictionaryVO> execute() {
                List<DataDictionary> models = dataDictionaryCommand.getAllDataDictionaries();
                return models.stream()
                        .map(DataDictionaryVO::fromModel)
                        .collect(Collectors.toList());
            }
        });
    }
    
    /**
     * 根据ID获取数据字典
     */
    @PreAuthorize("hasAuthority('system:dataDictionary:query')")
    @GetMapping("/get/{id}")
    public Response<DataDictionaryVO> get(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<DataDictionaryVO>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("字典ID不能为空");
                }
            }

            @Override
            public DataDictionaryVO execute() {
                DataDictionary model = dataDictionaryCommand.getDataDictionaryById(id);
                return DataDictionaryVO.fromModel(model);
            }
        });
    }
    
    /**
     * 创建数据字典
     */
    @PreAuthorize("hasAuthority('system:dataDictionary:add')")
    @PostMapping("/create")
    public Response<DataDictionaryVO> create(@RequestBody DataDictionaryVO vo) {
        return templateRest.request(new CallbackRest<DataDictionaryVO>() {
            @Override
            public void check() {
                if (vo == null) {
                    throw new IllegalArgumentException("字典VO不能为空");
                }
            }

            @Override
            public DataDictionaryVO execute() {
                DataDictionary model = dataDictionaryCommand.createDataDictionary(DataDictionaryVO.toDTO(vo));
                return DataDictionaryVO.fromModel(model);
            }
        }, true);
    }
    
    /**
     * 更新数据字典
     */
    @PreAuthorize("hasAuthority('system:dataDictionary:update')")
    @PostMapping("/update")
    public Response<DataDictionaryVO> update(@RequestBody DataDictionaryVO vo) {
        return templateRest.request(new CallbackRest<DataDictionaryVO>() {
            @Override
            public void check() {
                if (vo == null) {
                    throw new IllegalArgumentException("字典VO不能为空");
                }
                if (vo.getId() == null) {
                    throw new IllegalArgumentException("字典ID不能为空");
                }
            }

            @Override
            public DataDictionaryVO execute() {
                DataDictionary model = dataDictionaryCommand.updateDataDictionary(DataDictionaryVO.toDTO(vo));
                return DataDictionaryVO.fromModel(model);
            }
        }, true);
    }
    
    /**
     * 删除数据字典
     */
    @PreAuthorize("hasAuthority('system:dataDictionary:delete')")
    @PostMapping("/delete/{id}")
    public Response<Void> delete(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("字典ID不能为空");
                }
            }

            @Override
            public Void execute() {
                dataDictionaryCommand.deleteDataDictionary(id);
                return null;
            }
        }, true);
    }
    
    /**
     * 获取字典项列表
     */
    @PreAuthorize("hasAuthority('system:dataDictionary:item:list')")
    @GetMapping("/item/list/{dictionaryId}")
    public Response<List<DataDictionaryItemVO>> getItemList(@PathVariable Long dictionaryId) {
        return templateRest.request(new CallbackRest<List<DataDictionaryItemVO>>() {
            @Override
            public void check() {
                if (dictionaryId == null) {
                    throw new IllegalArgumentException("字典ID不能为空");
                }
            }

            @Override
            public List<DataDictionaryItemVO> execute() {
                List<DataDictionaryItem> models = dataDictionaryCommand.getDataDictionaryItemsByDictionaryId(dictionaryId);
                return models.stream()
                        .map(DataDictionaryItemVO::fromModel)
                        .collect(Collectors.toList());
            }
        });
    }
    
    /**
     * 根据字典编码获取启用的字典项
     */
    @GetMapping("/item/enabled/{code}")
    public Response<List<DataDictionaryItemVO>> getEnabledItems(@PathVariable String code) {
        return templateRest.request(new CallbackRest<List<DataDictionaryItemVO>>() {
            @Override
            public void check() {
                if (code == null || code.isEmpty()) {
                    throw new IllegalArgumentException("字典编码不能为空");
                }
            }

            @Override
            public List<DataDictionaryItemVO> execute() {
                List<DataDictionaryItem> models = dataDictionaryCommand.getEnabledDataDictionaryItemsByCode(code);
                return models.stream()
                        .map(DataDictionaryItemVO::fromModel)
                        .collect(Collectors.toList());
            }
        });
    }
    
    /**
     * 创建字典项
     */
    @PreAuthorize("hasAuthority('system:dataDictionary:item:add')")
    @PostMapping("/item/create")
    public Response<DataDictionaryItemVO> createItem(@RequestBody DataDictionaryItemVO vo) {
        return templateRest.request(new CallbackRest<DataDictionaryItemVO>() {
            @Override
            public void check() {
                if (vo == null) {
                    throw new IllegalArgumentException("字典项VO不能为空");
                }
            }

            @Override
            public DataDictionaryItemVO execute() {
                DataDictionaryItem model = dataDictionaryCommand.createDataDictionaryItem(DataDictionaryItemVO.toDTO(vo));
                return DataDictionaryItemVO.fromModel(model);
            }
        }, true);
    }
    
    /**
     * 更新字典项
     */
    @PreAuthorize("hasAuthority('system:dataDictionary:item:update')")
    @PostMapping("/item/update")
    public Response<DataDictionaryItemVO> updateItem(@RequestBody DataDictionaryItemVO vo) {
        return templateRest.request(new CallbackRest<DataDictionaryItemVO>() {
            @Override
            public void check() {
                if (vo == null) {
                    throw new IllegalArgumentException("字典项VO不能为空");
                }
                if (vo.getId() == null) {
                    throw new IllegalArgumentException("字典项ID不能为空");
                }
            }

            @Override
            public DataDictionaryItemVO execute() {
                DataDictionaryItem model = dataDictionaryCommand.updateDataDictionaryItem(DataDictionaryItemVO.toDTO(vo));
                return DataDictionaryItemVO.fromModel(model);
            }
        }, true);
    }
    
    /**
     * 删除字典项
     */
    @PreAuthorize("hasAuthority('system:dataDictionary:item:delete')")
    @PostMapping("/item/delete/{id}")
    public Response<Void> deleteItem(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("字典项ID不能为空");
                }
            }

            @Override
            public Void execute() {
                dataDictionaryCommand.deleteDataDictionaryItem(id);
                return null;
            }
        }, true);
    }
}