package com.piaomiao.web;

import com.piaomiao.service.ProcessInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 流程图控制器
 * <p>
 * 返回二进制PNG流，不使用 TemplateRest 包装（二进制响应不适合 JSON 封装）
 */
@Slf4j
@RestController
@RequestMapping("/process-diagram")
public class ProcessDiagramController {

    @Autowired
    private ProcessInstanceService processInstanceService;

    /**
     * 通过业务表ID获取流程图
     */
    @GetMapping(value = "/by-business-id/{id}", produces = "image/svg+xml")
    @PreAuthorize("hasRole('admin') or hasAuthority('process:diagram:view')")
    public void getProcessDiagramByBusinessId(@PathVariable Long id, HttpServletResponse response) {
        try (InputStream diagram = processInstanceService.generateDiagram(id)) {
            if (diagram == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "流程实例不存在");
                return;
            }
            response.setContentType("image/svg+xml");
            try (OutputStream out = response.getOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = diagram.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
            }
        } catch (Exception e) {
            log.error("生成流程图失败, businessId={}", id, e);
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "生成流程图失败");
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * 通过Activiti流程实例ID获取流程图（保留兼容）
     */
    @GetMapping(value = "/{processInstanceId}", produces = "image/svg+xml")
    @PreAuthorize("hasRole('admin') or hasAuthority('process:diagram:view')")
    public void getProcessDiagram(@PathVariable String processInstanceId, HttpServletResponse response) {
        // 通过Activiti ID查找业务记录再生成流程图
        try {
            com.piaomiao.model.ProcessInstanceModel model = processInstanceService.findByActId(processInstanceId);
            if (model == null || model.getId() == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "流程实例不存在");
                return;
            }
            try (InputStream diagram = processInstanceService.generateDiagram(model.getId())) {
                if (diagram == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "流程实例不存在");
                    return;
                }
                response.setContentType("image/svg+xml");
                try (OutputStream out = response.getOutputStream()) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = diagram.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                    out.flush();
                }
            }
        } catch (Exception e) {
            log.error("生成流程图失败, processInstanceId={}", processInstanceId, e);
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "生成流程图失败");
            } catch (Exception ignored) {
            }
        }
    }
}
