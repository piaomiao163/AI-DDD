package com.piaomiao.web;

import com.piaomiao.command.sys.OperationLogCommand;
import com.piaomiao.model.sys.OperationLogModel;
import com.piaomiao.response.Response;
import com.piaomiao.rest.CallbackRest;
import com.piaomiao.rest.TemplateRest;
import com.piaomiao.service.sys.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志控制器
 */
@RestController
@RequestMapping("/operationLog")
public class OperationLogController {

    @Autowired
    private OperationLogCommand operationLogCommand;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private TemplateRest templateRest;

    /**
     * 根据ID查询操作日志
     * @param id 日志ID
     * @return 操作日志模型
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasAuthority('system:operationLog:view')")
    public Response<OperationLogModel> findById(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<OperationLogModel>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("日志ID不能为空");
                }
            }

            @Override
            public OperationLogModel execute() {
                return operationLogCommand.findById(id);
            }
        });
    }

    /**
     * 查询所有操作日志
     * @return 操作日志列表
     */
    @GetMapping("/list")
    @PreAuthorize("hasRole('admin') or hasAuthority('system:operationLog:list')")
    public Response<List<OperationLogModel>> findAll() {
        return templateRest.request(new CallbackRest<List<OperationLogModel>>() {
            @Override
            public void check() {
            }

            @Override
            public List<OperationLogModel> execute() {
                return operationLogCommand.findAll();
            }
        });
    }

    /**
     * 根据条件查询操作日志
     * @param module 操作模块
     * @param type 操作类型
     * @param username 操作人
     * @return 操作日志列表
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('admin') or hasAuthority('system:operationLog:list')")
    public Response<List<OperationLogModel>> findByCondition(
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String username) {
        return templateRest.request(new CallbackRest<List<OperationLogModel>>() {
            @Override
            public void check() {
            }

            @Override
            public List<OperationLogModel> execute() {
                return operationLogService.findByCondition(module, type, username);
            }
        });
    }

    /**
     * 删除操作日志
     * @param id 日志ID
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('admin') or hasAuthority('system:operationLog:delete')")
    public Response<Void> delete(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("日志ID不能为空");
                }
            }

            @Override
            public Void execute() {
                operationLogCommand.delete(id);
                return null;
            }
        }, true);
    }

    /**
     * 批量删除操作日志
     * @param ids 日志ID列表
     */
    @DeleteMapping("/delete/batch")
    @PreAuthorize("hasRole('admin') or hasAuthority('system:operationLog:delete')")
    public Response<Void> deleteBatch(@RequestBody List<Long> ids) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (ids == null || ids.isEmpty()) {
                    throw new IllegalArgumentException("日志ID列表不能为空");
                }
            }

            @Override
            public Void execute() {
                operationLogCommand.deleteBatch(ids);
                return null;
            }
        }, true);
    }

    /**
     * 清空操作日志
     */
    @DeleteMapping("/clear")
    @PreAuthorize("hasRole('admin') or hasAuthority('system:operationLog:clear')")
    public Response<Void> clear() {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
            }

            @Override
            public Void execute() {
                operationLogCommand.clear();
                return null;
            }
        }, true);
    }
}
