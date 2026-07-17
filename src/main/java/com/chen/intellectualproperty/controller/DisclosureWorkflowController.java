package com.chen.intellectualproperty.controller;

import com.chen.intellectualproperty.model.dto.*;
import com.chen.intellectualproperty.model.entity.*;
import com.chen.intellectualproperty.model.query.PatentDisclosureQuery;
import com.chen.intellectualproperty.service.DisclosureWorkflowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 交底流程接口
 * <p>路径前缀 /api/disclosure-workflow</p>
 */
@RestController
@RequestMapping("/api/disclosure-workflow")
@RequiredArgsConstructor
public class DisclosureWorkflowController {

    private final DisclosureWorkflowService workflowService;

    // ---------- 交底主数据 ----------

    /** 录入交底（同步写缴费表、开票表） */
    @PostMapping("/disclosures")
    public Result<PatentDisclosure> create(@RequestBody DisclosureCreateDTO dto) {
        try {
            return Result.success(workflowService.createDisclosure(dto));
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    /** 复制历史交底 */
    @PostMapping("/disclosures/copy")
    public Result<PatentDisclosure> copy(@RequestBody DisclosureCopyDTO dto) {
        try {
            return Result.success(workflowService.copyDisclosure(dto));
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    /** 更新交底（主办处理中可改字段） */
    @PutMapping("/disclosures/{id}")
    public Result<PatentDisclosure> update(@PathVariable Long id, @RequestBody PatentDisclosure body) {
        try {
            return Result.success(workflowService.updateDisclosure(id, body));
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    /** 组合查询 */
    @PostMapping("/disclosures/search")
    public Result<List<PatentDisclosure>> search(@RequestBody(required = false) PatentDisclosureQuery query) {
        return Result.success(workflowService.search(query));
    }

    /** 主办人：我的交底 */
    @GetMapping("/disclosures/by-sponsor/{sponsorUserId}")
    public Result<List<PatentDisclosure>> bySponsor(@PathVariable Long sponsorUserId) {
        return Result.success(workflowService.listBySponsorUserId(sponsorUserId));
    }

    /** 交底详情（含附件/申请包/缴费/开票/状态日志/邮件） */
    @GetMapping("/disclosures/{id}")
    public Result<Map<String, Object>> detail(@PathVariable Long id) {
        try {
            return Result.success(workflowService.detail(id));
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    /** 修改交底状态；若目标为定稿待报则校验申请包并同步 P 表 */
    @PostMapping("/disclosures/{id}/status")
    public Result<?> changeStatus(@PathVariable Long id, @RequestBody StatusChangeDTO dto) {
        try {
            if (dto != null && ("12".equals(dto.getToStatus())
                    || "定稿待报".equals(dto.getToStatus()))) {
                return Result.success(workflowService.markPendingReportAndSync(id, dto));
            }
            return Result.success(workflowService.changeStatus(id, dto));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return Result.fail(e.getMessage());
        }
    }

    /** 明确：定稿待报 + 同步申请专利表 */
    @PostMapping("/disclosures/{id}/pending-report")
    public Result<Map<String, Object>> pendingReport(@PathVariable Long id,
                                                     @RequestBody(required = false) StatusChangeDTO dto) {
        try {
            return Result.success(workflowService.markPendingReportAndSync(id, dto));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return Result.fail(e.getMessage());
        }
    }

    // ---------- 附件 ----------

    /**
     * 上传交底附件
     * @param bizType DISCLOSURE_DOC（交底书 Word） / DISCLOSURE_OTHER
     */
    @PostMapping("/disclosures/{id}/attachments")
    public Result<DisclosureAttachment> uploadAttachment(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "bizType", defaultValue = "DISCLOSURE_OTHER") String bizType,
            @RequestParam(value = "uploadUserId", required = false) Long uploadUserId,
            @RequestParam(value = "uploadUserName", required = false) String uploadUserName) {
        try {
            return Result.success(workflowService.uploadAttachment(id, bizType, file, uploadUserId, uploadUserName));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return Result.fail(e.getMessage());
        }
    }

    @GetMapping("/disclosures/{id}/attachments")
    public Result<List<DisclosureAttachment>> listAttachments(@PathVariable Long id) {
        return Result.success(workflowService.listAttachments(id));
    }

    @DeleteMapping("/attachments/{attachmentId}")
    public Result<?> deleteAttachment(@PathVariable Long attachmentId) {
        try {
            workflowService.deleteAttachment(attachmentId);
            return Result.success(null);
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    // ---------- 申请包（XML / 五书 分条目） ----------

    /**
     * 上传申请包
     * @param packageType XML_PACKAGE 或 FIVE_BOOKS_WORD
     */
    @PostMapping("/disclosures/{id}/packages")
    public Result<ApplicationPackage> uploadPackage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam("packageType") String packageType,
            @RequestParam(value = "uploadUserId", required = false) Long uploadUserId,
            @RequestParam(value = "uploadUserName", required = false) String uploadUserName) {
        try {
            return Result.success(workflowService.uploadPackage(id, packageType, file, uploadUserId, uploadUserName));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return Result.fail(e.getMessage());
        }
    }

    @GetMapping("/disclosures/{id}/packages")
    public Result<List<ApplicationPackage>> listPackages(@PathVariable Long id) {
        return Result.success(workflowService.listPackages(id));
    }

    @PostMapping("/packages/{packageId}/confirm")
    public Result<ApplicationPackage> confirmPackage(
            @PathVariable Long packageId,
            @RequestParam(value = "confirmUserId", required = false) Long confirmUserId,
            @RequestParam(value = "confirmUserName", required = false) String confirmUserName) {
        try {
            return Result.success(workflowService.confirmPackage(packageId, confirmUserId, confirmUserName));
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    // ---------- 邮件 ----------

    @GetMapping("/mail-templates")
    public Result<List<MailTemplate>> mailTemplates() {
        return Result.success(workflowService.listMailTemplates());
    }

    @GetMapping("/mail-templates/{code}/preview")
    public Result<MailTemplate> previewTemplate(@PathVariable("code") String code,
                                                @RequestParam Long disclosureId) {
        try {
            return Result.success(workflowService.previewTemplate(code, disclosureId));
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    /** 读模板可改内容，附件 ID 可增删后发送 */
    @PostMapping("/mail/send")
    public Result<MailSendLog> sendMail(@RequestBody MailSendDTO dto) {
        try {
            return Result.success(workflowService.sendMail(dto));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return Result.fail(e.getMessage());
        }
    }

    @GetMapping("/disclosures/{id}/mail-logs")
    public Result<List<MailSendLog>> mailLogs(@PathVariable Long id) {
        return Result.success(workflowService.listMailLogs(id));
    }

    // ---------- 缴费 / 开票 / 状态日志 ----------

    @GetMapping("/disclosures/{id}/fees")
    public Result<List<FeePayment>> fees(@PathVariable Long id) {
        return Result.success(workflowService.listFees(id));
    }

    @GetMapping("/disclosures/{id}/invoices")
    public Result<List<Invoice>> invoices(@PathVariable Long id) {
        return Result.success(workflowService.listInvoices(id));
    }

    @GetMapping("/disclosures/{id}/status-logs")
    public Result<List<DisclosureStatusLog>> statusLogs(@PathVariable Long id) {
        return Result.success(workflowService.listStatusLogs(id));
    }
}
