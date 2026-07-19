package com.chen.intellectualproperty.controller;

import com.chen.intellectualproperty.exception.BusinessException;
import com.chen.intellectualproperty.model.dto.*;
import com.chen.intellectualproperty.model.entity.*;
import com.chen.intellectualproperty.model.enums.EmployeeRoleCode;
import com.chen.intellectualproperty.model.query.PatentDisclosureQuery;
import com.chen.intellectualproperty.model.vo.SessionUserVO;
import com.chen.intellectualproperty.service.AuthSessionService;
import com.chen.intellectualproperty.service.DisclosureWorkflowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 交底流程接口（按员工类型鉴权）
 * <p>
 * ENTRY 录入 | SPONSOR 主办本人 | PROCESS 流程确认/定稿待报 | ADMIN 全部
 * </p>
 */
@RestController
@RequestMapping("/api/disclosure-workflow")
@RequiredArgsConstructor
public class DisclosureWorkflowController {

    private final DisclosureWorkflowService workflowService;
    private final ObjectMapper objectMapper;
    private final AuthSessionService authSessionService;

    // ---------- 交底主数据 ----------

    @PostMapping(value = "/disclosures", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Result<PatentDisclosure> create(@RequestParam String token, @RequestBody DisclosureCreateDTO dto) {
        try {
            authSessionService.requireAnyRole(token, EmployeeRoleCode.ENTRY, EmployeeRoleCode.ADMIN);
            return Result.success(workflowService.createDisclosure(dto));
        } catch (BusinessException | IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping(value = "/disclosures/with-attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<PatentDisclosure> createWithAttachments(
            @RequestParam String token,
            @RequestParam("data") String dataJson,
            @RequestParam("disclosureDoc") MultipartFile disclosureDoc,
            @RequestParam(value = "otherFiles", required = false) MultipartFile[] otherFiles) {
        try {
            SessionUserVO session = authSessionService.requireAnyRole(
                    token, EmployeeRoleCode.ENTRY, EmployeeRoleCode.ADMIN);
            DisclosureCreateDTO dto = objectMapper.readValue(dataJson, DisclosureCreateDTO.class);
            if (dto.getDisclosure() != null) {
                if (dto.getDisclosure().getEntryUserId() == null) {
                    dto.getDisclosure().setEntryUserId(session.getUserId() == null ? null : session.getUserId().longValue());
                }
                if (dto.getDisclosure().getEntryUserName() == null || dto.getDisclosure().getEntryUserName().isBlank()) {
                    dto.getDisclosure().setEntryUserName(
                            session.getEmployeeName() != null ? session.getEmployeeName() : session.getNickName());
                }
            }
            Long uid = session.getUserId() == null ? null : session.getUserId().longValue();
            String uname = session.getEmployeeName() != null ? session.getEmployeeName() : session.getNickName();
            return Result.success(workflowService.createDisclosureWithAttachments(
                    dto, disclosureDoc, otherFiles, uid, uname));
        } catch (BusinessException | IllegalArgumentException | IllegalStateException e) {
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            return Result.fail("录入失败: " + e.getMessage());
        }
    }

    @PostMapping("/disclosures/copy")
    public Result<PatentDisclosure> copy(@RequestParam String token, @RequestBody DisclosureCopyDTO dto) {
        try {
            authSessionService.requireAnyRole(token, EmployeeRoleCode.ENTRY, EmployeeRoleCode.ADMIN);
            return Result.success(workflowService.copyDisclosure(dto));
        } catch (BusinessException | IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    @PutMapping("/disclosures/{id}")
    public Result<PatentDisclosure> update(@RequestParam String token,
                                           @PathVariable Long id,
                                           @RequestBody PatentDisclosure body) {
        try {
            SessionUserVO session = authSessionService.requireActiveEmployee(token);
            PatentDisclosure exist = loadDisclosure(id);
            assertCanViewOrEdit(session, exist, true);
            return Result.success(workflowService.updateDisclosure(id, body));
        } catch (BusinessException | IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("/disclosures/search")
    public Result<List<PatentDisclosure>> search(@RequestParam String token,
                                                 @RequestBody(required = false) PatentDisclosureQuery query) {
        try {
            SessionUserVO session = authSessionService.requireActiveEmployee(token);
            if (query == null) {
                query = new PatentDisclosureQuery();
            }
            // 纯主办人：强制只看自己的
            if (isSponsorOnly(session)) {
                query.setSponsorUserId(session.getUserId() == null ? -1L : session.getUserId().longValue());
            }
            return Result.success(workflowService.search(query));
        } catch (BusinessException e) {
            return Result.fail(e.getMessage());
        }
    }

    @GetMapping("/disclosures/by-sponsor/{sponsorUserId}")
    public Result<List<PatentDisclosure>> bySponsor(@RequestParam String token,
                                                    @PathVariable Long sponsorUserId) {
        try {
            SessionUserVO session = authSessionService.requireActiveEmployee(token);
            if (isSponsorOnly(session)) {
                sponsorUserId = session.getUserId() == null ? -1L : session.getUserId().longValue();
            } else {
                authSessionService.requireAnyRole(token,
                        EmployeeRoleCode.SPONSOR, EmployeeRoleCode.ENTRY,
                        EmployeeRoleCode.PROCESS, EmployeeRoleCode.ADMIN);
            }
            return Result.success(workflowService.listBySponsorUserId(sponsorUserId));
        } catch (BusinessException e) {
            return Result.fail(e.getMessage());
        }
    }

    @GetMapping("/disclosures/{id}")
    public Result<Map<String, Object>> detail(@RequestParam String token, @PathVariable Long id) {
        try {
            SessionUserVO session = authSessionService.requireActiveEmployee(token);
            PatentDisclosure exist = loadDisclosure(id);
            assertCanViewOrEdit(session, exist, false);
            return Result.success(workflowService.detail(id));
        } catch (BusinessException | IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("/disclosures/{id}/status")
    public Result<?> changeStatus(@RequestParam String token,
                                  @PathVariable Long id,
                                  @RequestBody StatusChangeDTO dto) {
        try {
            SessionUserVO session = authSessionService.requireActiveEmployee(token);
            PatentDisclosure exist = loadDisclosure(id);
            boolean pending = dto != null && ("12".equals(dto.getToStatus()) || "定稿待报".equals(dto.getToStatus()));
            if (pending) {
                authSessionService.requireAnyRole(token, EmployeeRoleCode.PROCESS, EmployeeRoleCode.ADMIN);
            } else {
                // 主办改状态（处理过程）
                if (!session.isAdmin()) {
                    authSessionService.requireAnyRole(token, EmployeeRoleCode.SPONSOR);
                    authSessionService.requireSponsorOf(session, exist.getSponsorUserId());
                }
            }
            fillOperator(dto, session);
            if (pending) {
                return Result.success(workflowService.markPendingReportAndSync(id, dto));
            }
            return Result.success(workflowService.changeStatus(id, dto));
        } catch (BusinessException | IllegalArgumentException | IllegalStateException e) {
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("/disclosures/{id}/pending-report")
    public Result<Map<String, Object>> pendingReport(@RequestParam String token,
                                                     @PathVariable Long id,
                                                     @RequestBody(required = false) StatusChangeDTO dto) {
        try {
            SessionUserVO session = authSessionService.requireAnyRole(
                    token, EmployeeRoleCode.PROCESS, EmployeeRoleCode.ADMIN);
            if (dto == null) {
                dto = new StatusChangeDTO();
            }
            fillOperator(dto, session);
            return Result.success(workflowService.markPendingReportAndSync(id, dto));
        } catch (BusinessException | IllegalArgumentException | IllegalStateException e) {
            return Result.fail(e.getMessage());
        }
    }

    // ---------- 附件 ----------

    @PostMapping("/disclosures/{id}/attachments")
    public Result<DisclosureAttachment> uploadAttachment(
            @RequestParam String token,
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "bizType", defaultValue = "DISCLOSURE_OTHER") String bizType) {
        try {
            SessionUserVO session = authSessionService.requireActiveEmployee(token);
            PatentDisclosure exist = loadDisclosure(id);
            // 录入或主办（本人）或管理
            if (!session.isAdmin()
                    && !authSessionService.hasAnyRole(session, EmployeeRoleCode.ENTRY)
                    && !authSessionService.hasAnyRole(session, EmployeeRoleCode.SPONSOR)) {
                throw new BusinessException("无权上传附件");
            }
            if (authSessionService.hasAnyRole(session, EmployeeRoleCode.SPONSOR)
                    && !session.isAdmin()
                    && !authSessionService.hasAnyRole(session, EmployeeRoleCode.ENTRY)) {
                authSessionService.requireSponsorOf(session, exist.getSponsorUserId());
            }
            Long uid = session.getUserId() == null ? null : session.getUserId().longValue();
            String uname = session.getEmployeeName() != null ? session.getEmployeeName() : session.getNickName();
            return Result.success(workflowService.uploadAttachment(id, bizType, file, uid, uname));
        } catch (BusinessException | IllegalArgumentException | IllegalStateException e) {
            return Result.fail(e.getMessage());
        }
    }

    @GetMapping("/disclosures/{id}/attachments")
    public Result<List<DisclosureAttachment>> listAttachments(@RequestParam String token, @PathVariable Long id) {
        try {
            SessionUserVO session = authSessionService.requireActiveEmployee(token);
            assertCanViewOrEdit(session, loadDisclosure(id), false);
            return Result.success(workflowService.listAttachments(id));
        } catch (BusinessException | IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    @DeleteMapping("/attachments/{attachmentId}")
    public Result<?> deleteAttachment(@RequestParam String token, @PathVariable Long attachmentId) {
        try {
            SessionUserVO session = authSessionService.requireAnyRole(
                    token, EmployeeRoleCode.ENTRY, EmployeeRoleCode.SPONSOR, EmployeeRoleCode.ADMIN);
            workflowService.deleteAttachment(attachmentId);
            return Result.success(null);
        } catch (BusinessException | IllegalArgumentException | IllegalStateException e) {
            return Result.fail(e.getMessage());
        }
    }

    // ---------- 申请包 ----------

    @PostMapping("/disclosures/{id}/packages")
    public Result<ApplicationPackage> uploadPackage(
            @RequestParam String token,
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam("packageType") String packageType) {
        try {
            SessionUserVO session = authSessionService.requireAnyRole(
                    token, EmployeeRoleCode.SPONSOR, EmployeeRoleCode.ADMIN);
            PatentDisclosure exist = loadDisclosure(id);
            authSessionService.requireSponsorOf(session, exist.getSponsorUserId());
            Long uid = session.getUserId() == null ? null : session.getUserId().longValue();
            String uname = session.getEmployeeName() != null ? session.getEmployeeName() : session.getNickName();
            return Result.success(workflowService.uploadPackage(id, packageType, file, uid, uname));
        } catch (BusinessException | IllegalArgumentException | IllegalStateException e) {
            return Result.fail(e.getMessage());
        }
    }

    @GetMapping("/disclosures/{id}/packages")
    public Result<List<ApplicationPackage>> listPackages(@RequestParam String token, @PathVariable Long id) {
        try {
            SessionUserVO session = authSessionService.requireActiveEmployee(token);
            assertCanViewOrEdit(session, loadDisclosure(id), false);
            return Result.success(workflowService.listPackages(id));
        } catch (BusinessException | IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("/packages/{packageId}/confirm")
    public Result<ApplicationPackage> confirmPackage(
            @RequestParam String token,
            @PathVariable Long packageId) {
        try {
            SessionUserVO session = authSessionService.requireAnyRole(
                    token, EmployeeRoleCode.PROCESS, EmployeeRoleCode.ADMIN);
            Long uid = session.getUserId() == null ? null : session.getUserId().longValue();
            String uname = session.getEmployeeName() != null ? session.getEmployeeName() : session.getNickName();
            return Result.success(workflowService.confirmPackage(packageId, uid, uname));
        } catch (BusinessException | IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    // ---------- 邮件 ----------

    @GetMapping("/mail-templates")
    public Result<List<MailTemplate>> mailTemplates(@RequestParam String token) {
        try {
            authSessionService.requireAnyRole(token,
                    EmployeeRoleCode.SPONSOR, EmployeeRoleCode.ENTRY, EmployeeRoleCode.ADMIN, EmployeeRoleCode.PROCESS);
            return Result.success(workflowService.listMailTemplates());
        } catch (BusinessException e) {
            return Result.fail(e.getMessage());
        }
    }

    @GetMapping("/mail-templates/{code}/preview")
    public Result<MailTemplate> previewTemplate(@RequestParam String token,
                                                @PathVariable("code") String code,
                                                @RequestParam Long disclosureId) {
        try {
            SessionUserVO session = authSessionService.requireAnyRole(
                    token, EmployeeRoleCode.SPONSOR, EmployeeRoleCode.ADMIN);
            PatentDisclosure exist = loadDisclosure(disclosureId);
            authSessionService.requireSponsorOf(session, exist.getSponsorUserId());
            return Result.success(workflowService.previewTemplate(code, disclosureId));
        } catch (BusinessException | IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("/mail/send")
    public Result<MailSendLog> sendMail(@RequestParam String token, @RequestBody MailSendDTO dto) {
        try {
            SessionUserVO session = authSessionService.requireAnyRole(
                    token, EmployeeRoleCode.SPONSOR, EmployeeRoleCode.ADMIN);
            if (dto == null || dto.getDisclosureId() == null) {
                throw new IllegalArgumentException("disclosureId 不能为空");
            }
            PatentDisclosure exist = loadDisclosure(dto.getDisclosureId());
            authSessionService.requireSponsorOf(session, exist.getSponsorUserId());
            dto.setSenderUserId(session.getUserId() == null ? null : session.getUserId().longValue());
            dto.setSenderName(session.getEmployeeName() != null ? session.getEmployeeName() : session.getNickName());
            return Result.success(workflowService.sendMail(dto));
        } catch (BusinessException | IllegalArgumentException | IllegalStateException e) {
            return Result.fail(e.getMessage());
        }
    }

    @GetMapping("/disclosures/{id}/mail-logs")
    public Result<List<MailSendLog>> mailLogs(@RequestParam String token, @PathVariable Long id) {
        try {
            SessionUserVO session = authSessionService.requireActiveEmployee(token);
            assertCanViewOrEdit(session, loadDisclosure(id), false);
            return Result.success(workflowService.listMailLogs(id));
        } catch (BusinessException | IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    // ---------- 附属 ----------

    @GetMapping("/disclosures/{id}/fees")
    public Result<List<FeePayment>> fees(@RequestParam String token, @PathVariable Long id) {
        try {
            SessionUserVO session = authSessionService.requireActiveEmployee(token);
            assertCanViewOrEdit(session, loadDisclosure(id), false);
            return Result.success(workflowService.listFees(id));
        } catch (BusinessException | IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    @GetMapping("/disclosures/{id}/invoices")
    public Result<List<Invoice>> invoices(@RequestParam String token, @PathVariable Long id) {
        try {
            SessionUserVO session = authSessionService.requireActiveEmployee(token);
            assertCanViewOrEdit(session, loadDisclosure(id), false);
            return Result.success(workflowService.listInvoices(id));
        } catch (BusinessException | IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    @GetMapping("/disclosures/{id}/status-logs")
    public Result<List<DisclosureStatusLog>> statusLogs(@RequestParam String token, @PathVariable Long id) {
        try {
            SessionUserVO session = authSessionService.requireActiveEmployee(token);
            assertCanViewOrEdit(session, loadDisclosure(id), false);
            return Result.success(workflowService.listStatusLogs(id));
        } catch (BusinessException | IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    // ---------- helpers ----------

    private PatentDisclosure loadDisclosure(Long id) {
        Map<String, Object> detail = workflowService.detail(id);
        Object d = detail.get("disclosure");
        if (!(d instanceof PatentDisclosure)) {
            throw new IllegalArgumentException("交底不存在: " + id);
        }
        return (PatentDisclosure) d;
    }

    /** 仅主办（无录入/流程/管理） */
    private boolean isSponsorOnly(SessionUserVO s) {
        if (s == null || s.isAdmin()) {
            return false;
        }
        boolean sponsor = authSessionService.hasAnyRole(s, EmployeeRoleCode.SPONSOR);
        boolean other = authSessionService.hasAnyRole(s, EmployeeRoleCode.ENTRY, EmployeeRoleCode.PROCESS);
        return sponsor && !other;
    }

    /**
     * @param edit true=改主数据：录入/主办本人/管理；false=查看：录入/流程/主办本人/管理
     */
    private void assertCanViewOrEdit(SessionUserVO session, PatentDisclosure d, boolean edit) {
        if (session.isAdmin()) {
            return;
        }
        if (authSessionService.hasAnyRole(session, EmployeeRoleCode.ENTRY, EmployeeRoleCode.PROCESS)) {
            if (edit && !authSessionService.hasAnyRole(session, EmployeeRoleCode.ENTRY, EmployeeRoleCode.SPONSOR)) {
                // 纯流程不可改主数据
                if (!authSessionService.hasAnyRole(session, EmployeeRoleCode.ENTRY)) {
                    throw new BusinessException("流程人员不可修改交底主数据");
                }
            }
            return;
        }
        if (authSessionService.hasAnyRole(session, EmployeeRoleCode.SPONSOR)) {
            authSessionService.requireSponsorOf(session, d.getSponsorUserId());
            return;
        }
        throw new BusinessException("无权访问该交底");
    }

    private void fillOperator(StatusChangeDTO dto, SessionUserVO session) {
        if (dto == null) {
            return;
        }
        if (dto.getOperatorUserId() == null && session.getUserId() != null) {
            dto.setOperatorUserId(session.getUserId().longValue());
        }
        if (dto.getOperatorName() == null || dto.getOperatorName().isBlank()) {
            dto.setOperatorName(session.getEmployeeName() != null ? session.getEmployeeName() : session.getNickName());
        }
    }
}
