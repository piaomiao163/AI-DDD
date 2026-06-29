package com.piaomiao.web.bid;

import com.piaomiao.response.Response;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * Stub endpoints for bid sub-modules (registration / tender / evaluation).
 * Returns empty collections to prevent 404 errors on the detail page.
 */
@RestController
public class BidStubController {

    @GetMapping("/bid/registration/project/{projectId}")
    public Response<List<Object>> getRegistrationsByProject(@PathVariable Long projectId) {
        return Response.success(Collections.emptyList());
    }

    @GetMapping("/bid/registration/my")
    public Response<Object> getMyRegistration(@RequestParam(required = false) Long projectId) {
        return Response.success(null);
    }

    @PostMapping("/bid/registration/page")
    public Response<Object> registrationPage(@RequestBody(required = false) Object body) {
        return Response.success(Collections.emptyList());
    }

    @PostMapping("/bid/registration/save")
    public Response<Object> saveRegistration(@RequestBody Object body) {
        return Response.success(null);
    }

    @PostMapping("/bid/registration/approve/{id}")
    public Response<Object> approveRegistration(@PathVariable Long id) {
        return Response.success(null);
    }

    @PostMapping("/bid/registration/reject/{id}")
    public Response<Object> rejectRegistration(@PathVariable Long id, @RequestBody(required = false) Object body) {
        return Response.success(null);
    }

    @GetMapping("/bid/tender/project/{projectId}")
    public Response<List<Object>> getTendersByProject(@PathVariable Long projectId) {
        return Response.success(Collections.emptyList());
    }

    @GetMapping("/bid/tender/my/{projectId}")
    public Response<Object> getMyTender(@PathVariable Long projectId) {
        return Response.success(null);
    }

    @GetMapping("/bid/tender/{id}")
    public Response<Object> getTenderById(@PathVariable Long id) {
        return Response.success(null);
    }

    @PostMapping("/bid/tender/save")
    public Response<Object> saveTender(@RequestBody Object body) {
        return Response.success(null);
    }

    @PostMapping("/bid/tender/submit/{id}")
    public Response<Object> submitTender(@PathVariable Long id) {
        return Response.success(null);
    }

    @GetMapping("/bid/evaluation/project/{projectId}")
    public Response<List<Object>> getEvaluationsByProject(@PathVariable Long projectId) {
        return Response.success(Collections.emptyList());
    }

    @GetMapping("/bid/evaluation/my")
    public Response<Object> getMyEvaluation(@RequestParam(required = false) Long tenderId,
                                             @RequestParam(required = false) Long expertId) {
        return Response.success(null);
    }

    @PostMapping("/bid/evaluation/save")
    public Response<Object> saveEvaluation(@RequestBody Object body) {
        return Response.success(null);
    }

    @PostMapping("/bid/evaluation/submit/{id}")
    public Response<Object> submitEvaluation(@PathVariable Long id) {
        return Response.success(null);
    }

    @PostMapping("/bid/expert/page")
    public Response<Object> expertPage(@RequestBody(required = false) Object body) {
        return Response.success(Collections.emptyList());
    }

    @GetMapping("/bid/expert/{id}")
    public Response<Object> getExpertById(@PathVariable Long id) {
        return Response.success(null);
    }

    @PostMapping("/bid/expert/save")
    public Response<Object> saveExpert(@RequestBody Object body) {
        return Response.success(null);
    }

    @PutMapping("/bid/expert/update")
    public Response<Object> updateExpert(@RequestBody Object body) {
        return Response.success(null);
    }

    @DeleteMapping("/bid/expert/delete/{id}")
    public Response<Object> deleteExpert(@PathVariable Long id) {
        return Response.success(null);
    }

    @PostMapping("/bid/expert/blacklist/{id}")
    public Response<Object> blacklistExpert(@PathVariable Long id, @RequestBody(required = false) Object body) {
        return Response.success(null);
    }

    @PostMapping("/bid/expert/unblacklist/{id}")
    public Response<Object> unblacklistExpert(@PathVariable Long id) {
        return Response.success(null);
    }

    @PostMapping("/bid/expert/select")
    public Response<Object> selectExperts(@RequestBody Object body) {
        return Response.success(null);
    }

    @PostMapping("/bid/complaint/page")
    public Response<Object> complaintPage(@RequestBody(required = false) Object body) {
        return Response.success(Collections.emptyList());
    }

    @GetMapping("/bid/complaint/my")
    public Response<List<Object>> getMyComplaints() {
        return Response.success(Collections.emptyList());
    }

    @PostMapping("/bid/complaint/save")
    public Response<Object> saveComplaint(@RequestBody Object body) {
        return Response.success(null);
    }

    @PostMapping("/bid/complaint/reply/{id}")
    public Response<Object> replyComplaint(@PathVariable Long id, @RequestBody(required = false) Object body) {
        return Response.success(null);
    }

    @PostMapping("/bid/complaint/close/{id}")
    public Response<Object> closeComplaint(@PathVariable Long id) {
        return Response.success(null);
    }

    @PostMapping("/bid/complaint/escalate/{id}")
    public Response<Object> escalateComplaint(@PathVariable Long id) {
        return Response.success(null);
    }

    @PostMapping("/bid/contract/page")
    public Response<Object> contractPage(@RequestBody(required = false) Object body) {
        return Response.success(Collections.emptyList());
    }

    @GetMapping("/bid/contract/{id}")
    public Response<Object> getContractById(@PathVariable Long id) {
        return Response.success(null);
    }

    @PostMapping("/bid/contract/save")
    public Response<Object> saveContract(@RequestBody Object body) {
        return Response.success(null);
    }

    @PutMapping("/bid/contract/update")
    public Response<Object> updateContract(@RequestBody Object body) {
        return Response.success(null);
    }

    @PostMapping("/bid/contract/sign/{id}")
    public Response<Object> signContract(@PathVariable Long id, @RequestBody(required = false) Object body) {
        return Response.success(null);
    }

    @PostMapping("/bid/contract/execute/{id}")
    public Response<Object> executeContract(@PathVariable Long id) {
        return Response.success(null);
    }

    @PostMapping("/bid/contract/complete/{id}")
    public Response<Object> completeContract(@PathVariable Long id) {
        return Response.success(null);
    }

    @PostMapping("/bid/contract/terminate/{id}")
    public Response<Object> terminateContract(@PathVariable Long id) {
        return Response.success(null);
    }
}
