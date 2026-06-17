package com.piaomiao.web;

import com.piaomiao.command.ProcessDefinitionCommand;
import com.piaomiao.dto.ProcessDefinitionDTO;
import com.piaomiao.response.PageResult;
import com.piaomiao.response.Response;
import com.piaomiao.rest.CallbackRest;
import com.piaomiao.rest.TemplateRest;
import com.piaomiao.web.vo.ProcessDefinitionQueryVO;
import com.piaomiao.web.vo.ProcessDefinitionVO;
import com.piaomiao.web.vo.ProcessEdgeVO;
import com.piaomiao.web.vo.ProcessNodeVO;
import com.piaomiao.web.vo.SaveProcessVO;
import com.piaomiao.web.vo.UpdateProcessVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 流程定义控制器
 */
@RestController
@RequestMapping("/process")
public class ProcessDefinitionController {

    @Autowired
    private ProcessDefinitionCommand processDefinitionCommand;

    @Autowired
    private TemplateRest templateRest;

    @PostMapping("/save")
    @PreAuthorize("hasRole('admin') or hasAuthority('process:definition:add')")
    public Response<ProcessDefinitionVO> save(@RequestBody SaveProcessVO saveVO) {
        return templateRest.request(new CallbackRest<ProcessDefinitionVO>() {
            @Override
            public void check() {
                if (saveVO.getName() == null || saveVO.getName().isEmpty()) {
                    throw new IllegalArgumentException("流程名称不能为空");
                }
                if (saveVO.getProcessKey() == null || saveVO.getProcessKey().isEmpty()) {
                    throw new IllegalArgumentException("流程标识不能为空");
                }
            }

            @Override
            public ProcessDefinitionVO execute() {
                ProcessDefinitionDTO savedDTO = processDefinitionCommand.save(saveVO.toDTO());
                return convertToVO(savedDTO);
            }
        }, true);
    }

    @GetMapping("/findById/{id}")
    @PreAuthorize("hasRole('admin') or hasAuthority('process:definition:view')")
    public Response<ProcessDefinitionVO> findById(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<ProcessDefinitionVO>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("流程定义ID不能为空");
                }
            }

            @Override
            public ProcessDefinitionVO execute() {
                ProcessDefinitionDTO dto = processDefinitionCommand.findById(id);
                return convertToVO(dto);
            }
        });
    }

    @GetMapping("/findByKey/{key}")
    @PreAuthorize("hasRole('admin') or hasAuthority('process:definition:view')")
    public Response<ProcessDefinitionVO> findByKey(@PathVariable String key) {
        return templateRest.request(new CallbackRest<ProcessDefinitionVO>() {
            @Override
            public void check() {
                if (key == null || key.isEmpty()) {
                    throw new IllegalArgumentException("流程标识不能为空");
                }
            }

            @Override
            public ProcessDefinitionVO execute() {
                ProcessDefinitionDTO dto = processDefinitionCommand.findByKey(key);
                return convertToVO(dto);
            }
        });
    }

    @GetMapping("/findAll")
    @PreAuthorize("hasRole('admin') or hasAuthority('process:definition:list')")
    public Response<List<ProcessDefinitionVO>> findAll() {
        return templateRest.request(new CallbackRest<List<ProcessDefinitionVO>>() {
            @Override
            public void check() {
            }

            @Override
            public List<ProcessDefinitionVO> execute() {
                List<ProcessDefinitionDTO> dtoList = processDefinitionCommand.findAll();
                return dtoList.stream().map(ProcessDefinitionController.this::convertToVO).collect(Collectors.toList());
            }
        });
    }

    @PostMapping("/findByPage")
    @PreAuthorize("hasRole('admin') or hasAuthority('process:definition:list')")
    public Response<PageResult<ProcessDefinitionVO>> findByPage(@RequestBody ProcessDefinitionQueryVO vo) {
        return templateRest.request(new CallbackRest<PageResult<ProcessDefinitionVO>>() {
            @Override
            public void check() {
            }

            @Override
            public PageResult<ProcessDefinitionVO> execute() {
                PageResult<ProcessDefinitionDTO> pageResult = processDefinitionCommand.findByPage(ProcessDefinitionQueryVO.toDTO(vo));
                List<ProcessDefinitionVO> voList = pageResult.getList().stream()
                        .map(ProcessDefinitionController.this::convertToVO)
                        .collect(Collectors.toList());
                return new PageResult<>(pageResult.getTotal(), voList,
                        pageResult.getPageNum(), pageResult.getPageSize());
            }
        });
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('admin') or hasAuthority('process:definition:edit')")
    public Response<ProcessDefinitionVO> update(@RequestBody UpdateProcessVO updateVO) {
        return templateRest.request(new CallbackRest<ProcessDefinitionVO>() {
            @Override
            public void check() {
                if (updateVO.getId() == null) {
                    throw new IllegalArgumentException("流程定义ID不能为空");
                }
            }

            @Override
            public ProcessDefinitionVO execute() {
                ProcessDefinitionDTO updatedDTO = processDefinitionCommand.update(updateVO.toDTO());
                return convertToVO(updatedDTO);
            }
        }, true);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('admin') or hasAuthority('process:definition:delete')")
    public Response<Void> delete(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("流程定义ID不能为空");
                }
            }

            @Override
            public Void execute() {
                processDefinitionCommand.delete(id);
                return null;
            }
        }, true);
    }

    @PostMapping("/publish/{id}")
    @PreAuthorize("hasRole('admin') or hasAuthority('process:definition:publish')")
    public Response<ProcessDefinitionVO> publish(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<ProcessDefinitionVO>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("流程定义ID不能为空");
                }
            }

            @Override
            public ProcessDefinitionVO execute() {
                ProcessDefinitionDTO dto = processDefinitionCommand.publish(id);
                return convertToVO(dto);
            }
        }, true);
    }

    @PostMapping("/unpublish/{id}")
    @PreAuthorize("hasRole('admin') or hasAuthority('process:definition:publish')")
    public Response<Void> unpublish(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("流程定义ID不能为空");
                }
            }

            @Override
            public Void execute() {
                processDefinitionCommand.unpublish(id);
                return null;
            }
        }, true);
    }

    private ProcessDefinitionVO convertToVO(ProcessDefinitionDTO dto) {
        if (dto == null) return null;
        ProcessDefinitionVO vo = new ProcessDefinitionVO();
        BeanUtils.copyProperties(dto, vo);

        if (dto.getNodes() != null) {
            List<ProcessNodeVO> nodes = dto.getNodes().stream().map(nodeDTO -> {
                ProcessNodeVO nodeVO = new ProcessNodeVO();
                BeanUtils.copyProperties(nodeDTO, nodeVO);
                return nodeVO;
            }).collect(Collectors.toList());
            vo.setNodes(nodes);
        }

        if (dto.getEdges() != null) {
            List<ProcessEdgeVO> edges = dto.getEdges().stream().map(edgeDTO -> {
                ProcessEdgeVO edgeVO = new ProcessEdgeVO();
                BeanUtils.copyProperties(edgeDTO, edgeVO);
                return edgeVO;
            }).collect(Collectors.toList());
            vo.setEdges(edges);
        }

        return vo;
    }
}
