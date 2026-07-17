package com.chen.intellectualproperty.service.impl;

import com.chen.intellectualproperty.mapper.*;
import com.chen.intellectualproperty.model.dto.DisclosureCopyDTO;
import com.chen.intellectualproperty.model.dto.DisclosureCreateDTO;
import com.chen.intellectualproperty.model.dto.MailSendDTO;
import com.chen.intellectualproperty.model.dto.StatusChangeDTO;
import com.chen.intellectualproperty.model.entity.*;
import com.chen.intellectualproperty.model.enums.PatentStatus;
import com.chen.intellectualproperty.model.query.PatentDisclosureQuery;
import com.chen.intellectualproperty.service.DisclosureWorkflowService;
import com.chen.intellectualproperty.service.GenerateNoService;
import com.chen.intellectualproperty.service.PatentNewApplicationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DisclosureWorkflowServiceImpl implements DisclosureWorkflowService {

    public static final String BIZ_DISCLOSURE_DOC = "DISCLOSURE_DOC";
    public static final String BIZ_DISCLOSURE_OTHER = "DISCLOSURE_OTHER";
    public static final String PKG_XML = "XML_PACKAGE";
    public static final String PKG_FIVE_BOOKS = "FIVE_BOOKS_WORD";

    private final PatentDisclosureMapper patentDisclosureMapper;
    private final DisclosureAttachmentMapper disclosureAttachmentMapper;
    private final FeePaymentMapper feePaymentMapper;
    private final InvoiceMapper invoiceMapper;
    private final ApplicationPackageMapper applicationPackageMapper;
    private final DisclosureStatusLogMapper disclosureStatusLogMapper;
    private final MailTemplateMapper mailTemplateMapper;
    private final MailSendLogMapper mailSendLogMapper;
    private final MailSendAttachmentMapper mailSendAttachmentMapper;
    private final GenerateNoService generateNoService;
    private final PatentNewApplicationService patentNewApplicationService;
    private final MailService mailService;

    @Value("${app.upload.dir}")
    private String uploadDir;

    private Path uploadPath;

    @PostConstruct
    public void init() throws IOException {
        uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);
    }

    @Override
    @Transactional
    public PatentDisclosure createDisclosure(DisclosureCreateDTO dto) {
        if (dto == null || dto.getDisclosure() == null) {
            throw new IllegalArgumentException("交底信息不能为空");
        }
        PatentDisclosure record = dto.getDisclosure();
        boolean auto = dto.getAutoGenerateNo() == null || Boolean.TRUE.equals(dto.getAutoGenerateNo());
        fillNumbers(record, auto);
        if (!StringUtils.hasText(record.getPatentStatus())) {
            record.setPatentStatus(PatentStatus.RECEIVE_DISCLOSURE_DOC.codeAsString());
        }
        if (!StringUtils.hasText(record.getNoGenerateMode())) {
            record.setNoGenerateMode(auto ? "AUTO" : "MANUAL");
        }
        if (record.getSyncedToPatent() == null) {
            record.setSyncedToPatent(0);
        }
        patentDisclosureMapper.insert(record);
        syncFeeAndInvoice(record);
        writeStatusLog(record.getId(), null, record.getPatentStatus(),
                record.getEntryUserId(), record.getEntryUserName(), "录入交底");
        return patentDisclosureMapper.selectById(record.getId());
    }

    @Override
    @Transactional
    public PatentDisclosure copyDisclosure(DisclosureCopyDTO dto) {
        if (dto == null || dto.getSourceId() == null) {
            throw new IllegalArgumentException("sourceId 不能为空");
        }
        PatentDisclosure source = patentDisclosureMapper.selectById(dto.getSourceId());
        if (source == null) {
            throw new IllegalArgumentException("源交底不存在: " + dto.getSourceId());
        }
        boolean auto = dto.getAutoGenerateNo() == null || Boolean.TRUE.equals(dto.getAutoGenerateNo());
        PatentDisclosure copy = new PatentDisclosure();
        copy.setRequirement(source.getRequirement());
        copy.setDisclosureName(source.getDisclosureName());
        copy.setApplicant(source.getApplicant());
        copy.setInventor(source.getInventor());
        copy.setPatentType(source.getPatentType());
        copy.setInvitedToGroup(source.getInvitedToGroup());
        copy.setContactPerson(source.getContactPerson());
        copy.setManager(source.getManager());
        copy.setAgent(source.getAgent());
        copy.setSponsor(source.getSponsor());
        copy.setSponsorUserId(source.getSponsorUserId());
        copy.setDisclosureDate(source.getDisclosureDate());
        copy.setDisclosureDays(source.getDisclosureDays());
        copy.setRemark(source.getRemark());
        copy.setContactInfo(source.getContactInfo());
        copy.setContactEmail(source.getContactEmail());
        copy.setContactPhone(source.getContactPhone());
        copy.setPatentStatus(PatentStatus.RECEIVE_DISCLOSURE_DOC.codeAsString());
        copy.setCopyFromId(source.getId());
        copy.setSyncedToPatent(0);
        fillNumbers(copy, auto);
        copy.setNoGenerateMode(auto ? "AUTO" : "MANUAL");
        patentDisclosureMapper.insert(copy);
        syncFeeAndInvoice(copy);
        writeStatusLog(copy.getId(), null, copy.getPatentStatus(), null, null,
                "复制自交底#" + source.getId());

        if (dto.getCopyAttachments() == null || Boolean.TRUE.equals(dto.getCopyAttachments())) {
            List<DisclosureAttachment> attachments =
                    disclosureAttachmentMapper.selectByDisclosureId(source.getId());
            for (DisclosureAttachment a : attachments) {
                DisclosureAttachment na = new DisclosureAttachment();
                na.setDisclosureId(copy.getId());
                na.setInternalNo(copy.getInternalNo());
                na.setBizType(a.getBizType());
                na.setFileName(a.getFileName());
                na.setFileExt(a.getFileExt());
                na.setFilePath(a.getFilePath());
                na.setFileUrl(a.getFileUrl());
                na.setFileSize(a.getFileSize());
                na.setContentType(a.getContentType());
                na.setIsRequired(a.getIsRequired());
                na.setSortNo(a.getSortNo());
                na.setDeleted(0);
                disclosureAttachmentMapper.insert(na);
            }
        }
        return patentDisclosureMapper.selectById(copy.getId());
    }

    @Override
    @Transactional
    public PatentDisclosure updateDisclosure(Long id, PatentDisclosure record) {
        PatentDisclosure exist = requireDisclosure(id);
        record.setId(id);
        // 不允许通过普通更新篡改同步标记
        record.setSyncedToPatent(exist.getSyncedToPatent());
        record.setPatentApplicationId(exist.getPatentApplicationId());
        patentDisclosureMapper.updateById(record);
        return patentDisclosureMapper.selectById(id);
    }

    @Override
    public List<PatentDisclosure> search(PatentDisclosureQuery query) {
        if (query == null) {
            query = new PatentDisclosureQuery();
        }
        return patentDisclosureMapper.findListByParam(query);
    }

    @Override
    public List<PatentDisclosure> listBySponsorUserId(Long sponsorUserId) {
        PatentDisclosureQuery q = new PatentDisclosureQuery();
        q.setSponsorUserId(sponsorUserId);
        return patentDisclosureMapper.findListByParam(q);
    }

    @Override
    @Transactional
    public PatentDisclosure changeStatus(Long id, StatusChangeDTO dto) {
        if (dto == null || !StringUtils.hasText(dto.getToStatus())) {
            throw new IllegalArgumentException("toStatus 不能为空");
        }
        PatentDisclosure exist = requireDisclosure(id);
        String from = exist.getPatentStatus();
        String to = normalizeStatus(dto.getToStatus());

        if (PatentStatus.isFinalizedPendingReport(to)) {
            return (PatentDisclosure) markPendingReportAndSync(id, dto).get("disclosure");
        }

        PatentDisclosure patch = new PatentDisclosure();
        patch.setId(id);
        patch.setPatentStatus(to);
        if (PatentStatus.fromCode(to).map(s -> s == PatentStatus.FINALIZED).orElse(false)
                || "定稿".equals(dto.getToStatus()) || "11".equals(to)) {
            patch.setFinalizedAt(new Date());
        }
        patentDisclosureMapper.updateById(patch);
        writeStatusLog(id, from, to, dto.getOperatorUserId(), dto.getOperatorName(), dto.getRemark());
        return patentDisclosureMapper.selectById(id);
    }

    @Override
    @Transactional
    public DisclosureAttachment uploadAttachment(Long disclosureId, String bizType, MultipartFile file,
                                                 Long uploadUserId, String uploadUserName) {
        PatentDisclosure d = requireDisclosure(disclosureId);
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }
        String type = StringUtils.hasText(bizType) ? bizType.trim() : BIZ_DISCLOSURE_OTHER;
        String original = file.getOriginalFilename() == null ? "file" : file.getOriginalFilename();
        String ext = extensionOf(original);

        if (BIZ_DISCLOSURE_DOC.equals(type)) {
            if (!isWordExt(ext)) {
                throw new IllegalArgumentException("交底书必须是 Word 文件（.doc/.docx）");
            }
        }

        StoredFile stored = storeFile(file, original, ext);

        DisclosureAttachment att = new DisclosureAttachment();
        att.setDisclosureId(disclosureId);
        att.setInternalNo(d.getInternalNo());
        att.setBizType(type);
        att.setFileName(original);
        att.setFileExt(ext);
        att.setFilePath(stored.diskName());
        att.setFileUrl(stored.url());
        att.setFileSize(file.getSize());
        att.setContentType(file.getContentType());
        att.setIsRequired(BIZ_DISCLOSURE_DOC.equals(type) ? 1 : 0);
        att.setSortNo(0);
        att.setUploadUserId(uploadUserId);
        att.setUploadUserName(uploadUserName);
        att.setDeleted(0);
        disclosureAttachmentMapper.insert(att);
        return disclosureAttachmentMapper.selectById(att.getId());
    }

    @Override
    public List<DisclosureAttachment> listAttachments(Long disclosureId) {
        return disclosureAttachmentMapper.selectByDisclosureId(disclosureId);
    }

    @Override
    @Transactional
    public void deleteAttachment(Long attachmentId) {
        DisclosureAttachment att = disclosureAttachmentMapper.selectById(attachmentId);
        if (att == null) {
            throw new IllegalArgumentException("附件不存在");
        }
        disclosureAttachmentMapper.logicalDelete(attachmentId);
    }

    @Override
    @Transactional
    public ApplicationPackage uploadPackage(Long disclosureId, String packageType, MultipartFile file,
                                            Long uploadUserId, String uploadUserName) {
        PatentDisclosure d = requireDisclosure(disclosureId);
        if (!PatentStatus.isFinalizedOrAfter(d.getPatentStatus())
                && !Objects.equals(d.getPatentStatus(), PatentStatus.FINALIZED.codeAsString())
                && !"定稿".equals(d.getPatentStatus())
                && !"11".equals(d.getPatentStatus())) {
            // 允许 定稿 及之后；若仅“待定稿”不允许
            if (!isAtLeastFinalized(d.getPatentStatus())) {
                throw new IllegalStateException("仅定稿及之后状态可上传申请包，当前状态: " + d.getPatentStatus());
            }
        }
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }
        String type = packageType == null ? "" : packageType.trim();
        if (!PKG_XML.equals(type) && !PKG_FIVE_BOOKS.equals(type)) {
            throw new IllegalArgumentException("packageType 只能是 XML_PACKAGE 或 FIVE_BOOKS_WORD");
        }
        String original = file.getOriginalFilename() == null ? "package" : file.getOriginalFilename();
        String ext = extensionOf(original);
        if (PKG_FIVE_BOOKS.equals(type) && !isWordExt(ext)) {
            throw new IllegalArgumentException("五书申请文件须为 Word（.doc/.docx）");
        }

        ApplicationPackage current = applicationPackageMapper.selectCurrent(disclosureId, type);
        int version = current == null ? 1 : (current.getVersionNo() == null ? 1 : current.getVersionNo() + 1);
        applicationPackageMapper.markNotCurrent(disclosureId, type);

        StoredFile stored = storeFile(file, original, ext);
        ApplicationPackage pkg = new ApplicationPackage();
        pkg.setDisclosureId(disclosureId);
        pkg.setInternalNo(d.getInternalNo());
        pkg.setPackageType(type);
        pkg.setFileName(original);
        pkg.setFileExt(ext);
        pkg.setFilePath(stored.diskName());
        pkg.setFileUrl(stored.url());
        pkg.setFileSize(file.getSize());
        pkg.setContentType(file.getContentType());
        pkg.setVersionNo(version);
        pkg.setIsCurrent(1);
        pkg.setUploadUserId(uploadUserId);
        pkg.setUploadUserName(uploadUserName);
        pkg.setConfirmStatus("UNCONFIRMED");
        applicationPackageMapper.insert(pkg);
        return applicationPackageMapper.selectById(pkg.getId());
    }

    @Override
    public List<ApplicationPackage> listPackages(Long disclosureId) {
        return applicationPackageMapper.selectByDisclosureId(disclosureId);
    }

    @Override
    @Transactional
    public ApplicationPackage confirmPackage(Long packageId, Long confirmUserId, String confirmUserName) {
        ApplicationPackage pkg = applicationPackageMapper.selectById(packageId);
        if (pkg == null) {
            throw new IllegalArgumentException("申请包不存在");
        }
        ApplicationPackage patch = new ApplicationPackage();
        patch.setId(packageId);
        patch.setConfirmStatus("CONFIRMED");
        patch.setConfirmUserId(confirmUserId);
        patch.setConfirmUserName(confirmUserName);
        patch.setConfirmTime(new Date());
        applicationPackageMapper.updateById(patch);
        return applicationPackageMapper.selectById(packageId);
    }

    @Override
    @Transactional
    public Map<String, Object> markPendingReportAndSync(Long disclosureId, StatusChangeDTO dto) {
        PatentDisclosure d = requireDisclosure(disclosureId);
        ApplicationPackage xml = applicationPackageMapper.selectCurrent(disclosureId, PKG_XML);
        ApplicationPackage five = applicationPackageMapper.selectCurrent(disclosureId, PKG_FIVE_BOOKS);
        if (xml == null || five == null) {
            throw new IllegalStateException("定稿待报前须同时具备当前有效的 XML_PACKAGE 与 FIVE_BOOKS_WORD");
        }

        String from = d.getPatentStatus();
        String to = PatentStatus.FINALIZED_PENDING_REPORT.codeAsString();

        PatentDisclosure patch = new PatentDisclosure();
        patch.setId(disclosureId);
        patch.setPatentStatus(to);
        patch.setPendingReportAt(new Date());
        if (d.getFinalizedAt() == null) {
            patch.setFinalizedAt(new Date());
        }

        PatentNewApplication patent = null;
        if (d.getSyncedToPatent() == null || d.getSyncedToPatent() == 0) {
            patent = new PatentNewApplication();
            patent.setInternalNo(d.getInternalNo());
            patent.setPatentName(d.getDisclosureName());
            patent.setPatentType(d.getPatentType());
            patent.setApplicant(d.getApplicant());
            patent.setInventor(d.getInventor());
            patent.setSponsor(d.getSponsor());
            patent.setAgent(d.getAgent());
            patentNewApplicationService.insert(patent);
            // insert 后无返回 id：重新查或依赖 useGeneratedKeys
            // PatentNewApplicationMapper insert 是否 useGeneratedKeys？检查...
            patch.setSyncedToPatent(1);
            if (patent.getId() != null) {
                patch.setPatentApplicationId(patent.getId());
            }
        }

        patentDisclosureMapper.updateById(patch);
        writeStatusLog(disclosureId, from, to,
                dto == null ? null : dto.getOperatorUserId(),
                dto == null ? null : dto.getOperatorName(),
                dto == null ? "定稿待报并同步申请专利表" : dto.getRemark());

        // 标记两包可提交
        if (xml.getId() != null) {
            ApplicationPackage p1 = new ApplicationPackage();
            p1.setId(xml.getId());
            p1.setConfirmStatus("CONFIRMED");
            p1.setConfirmTime(new Date());
            if (dto != null) {
                p1.setConfirmUserId(dto.getOperatorUserId());
                p1.setConfirmUserName(dto.getOperatorName());
            }
            applicationPackageMapper.updateById(p1);
        }
        if (five.getId() != null) {
            ApplicationPackage p2 = new ApplicationPackage();
            p2.setId(five.getId());
            p2.setConfirmStatus("CONFIRMED");
            p2.setConfirmTime(new Date());
            if (dto != null) {
                p2.setConfirmUserId(dto.getOperatorUserId());
                p2.setConfirmUserName(dto.getOperatorName());
            }
            applicationPackageMapper.updateById(p2);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("disclosure", patentDisclosureMapper.selectById(disclosureId));
        result.put("patent", patent);
        result.put("xmlPackage", applicationPackageMapper.selectCurrent(disclosureId, PKG_XML));
        result.put("fiveBooksPackage", applicationPackageMapper.selectCurrent(disclosureId, PKG_FIVE_BOOKS));
        return result;
    }

    @Override
    public List<MailTemplate> listMailTemplates() {
        return mailTemplateMapper.selectEnabled();
    }

    @Override
    public MailTemplate previewTemplate(String templateCode, Long disclosureId) {
        MailTemplate tpl = mailTemplateMapper.selectByCode(templateCode);
        if (tpl == null) {
            throw new IllegalArgumentException("模板不存在: " + templateCode);
        }
        PatentDisclosure d = requireDisclosure(disclosureId);
        MailTemplate preview = new MailTemplate();
        preview.setId(tpl.getId());
        preview.setTemplateCode(tpl.getTemplateCode());
        preview.setTemplateName(tpl.getTemplateName());
        preview.setDefaultAttachTypes(tpl.getDefaultAttachTypes());
        preview.setSubject(render(tpl.getSubject(), d));
        preview.setContent(render(tpl.getContent(), d));
        preview.setEnabled(tpl.getEnabled());
        return preview;
    }

    @Override
    @Transactional
    public MailSendLog sendMail(MailSendDTO dto) {
        if (dto == null || dto.getDisclosureId() == null) {
            throw new IllegalArgumentException("disclosureId 不能为空");
        }
        PatentDisclosure d = requireDisclosure(dto.getDisclosureId());

        String subject = dto.getSubject();
        String content = dto.getContent();
        Long templateId = null;
        String templateCode = dto.getTemplateCode();
        if (StringUtils.hasText(templateCode)) {
            MailTemplate tpl = mailTemplateMapper.selectByCode(templateCode);
            if (tpl != null) {
                templateId = tpl.getId();
                if (!StringUtils.hasText(subject)) {
                    subject = render(tpl.getSubject(), d);
                }
                if (!StringUtils.hasText(content)) {
                    content = render(tpl.getContent(), d);
                }
            }
        }
        if (!StringUtils.hasText(subject) || !StringUtils.hasText(content)) {
            throw new IllegalArgumentException("邮件主题和正文不能为空");
        }
        String to = StringUtils.hasText(dto.getToEmails()) ? dto.getToEmails() : d.getContactEmail();
        if (!StringUtils.hasText(to)) {
            throw new IllegalArgumentException("收件人不能为空");
        }

        List<DisclosureAttachment> attachments = resolveMailAttachments(dto, d);

        MailSendLog logEntity = new MailSendLog();
        logEntity.setDisclosureId(d.getId());
        logEntity.setInternalNo(d.getInternalNo());
        logEntity.setTemplateId(templateId);
        logEntity.setTemplateCode(templateCode);
        logEntity.setToEmails(to);
        logEntity.setCcEmails(dto.getCcEmails());
        logEntity.setSubject(subject);
        logEntity.setContent(content);
        logEntity.setSendStatus("PENDING");
        logEntity.setSenderUserId(dto.getSenderUserId());
        logEntity.setSenderName(dto.getSenderName());
        mailSendLogMapper.insert(logEntity);

        for (DisclosureAttachment a : attachments) {
            MailSendAttachment ma = new MailSendAttachment();
            ma.setMailSendLogId(logEntity.getId());
            ma.setDisclosureAttachmentId(a.getId());
            ma.setFileName(a.getFileName());
            ma.setFilePath(a.getFilePath());
            ma.setFileUrl(a.getFileUrl());
            ma.setFileSize(a.getFileSize());
            mailSendAttachmentMapper.insert(ma);
        }

        try {
            List<Path> paths = new ArrayList<>();
            List<String> names = new ArrayList<>();
            for (DisclosureAttachment a : attachments) {
                Path p = uploadPath.resolve(a.getFilePath()).normalize();
                if (Files.exists(p)) {
                    paths.add(p);
                    names.add(a.getFileName());
                }
            }
            String from = mailService.sendMailWithFiles(to, dto.getCcEmails(), subject, content, paths, names);
            MailSendLog ok = new MailSendLog();
            ok.setId(logEntity.getId());
            ok.setSendStatus("SUCCESS");
            ok.setFromEmail(from);
            ok.setSentAt(new Date());
            mailSendLogMapper.updateById(ok);
        } catch (Exception e) {
            log.error("交底邮件发送失败 disclosureId={}", d.getId(), e);
            MailSendLog fail = new MailSendLog();
            fail.setId(logEntity.getId());
            fail.setSendStatus("FAILED");
            fail.setErrorMessage(e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage());
            mailSendLogMapper.updateById(fail);
            throw new IllegalStateException("邮件发送失败: " + e.getMessage(), e);
        }
        return mailSendLogMapper.selectById(logEntity.getId());
    }

    @Override
    public List<MailSendLog> listMailLogs(Long disclosureId) {
        return mailSendLogMapper.selectByDisclosureId(disclosureId);
    }

    @Override
    public List<FeePayment> listFees(Long disclosureId) {
        return feePaymentMapper.selectByDisclosureId(disclosureId);
    }

    @Override
    public List<Invoice> listInvoices(Long disclosureId) {
        return invoiceMapper.selectByDisclosureId(disclosureId);
    }

    @Override
    public List<DisclosureStatusLog> listStatusLogs(Long disclosureId) {
        return disclosureStatusLogMapper.selectByDisclosureId(disclosureId);
    }

    @Override
    public Map<String, Object> detail(Long disclosureId) {
        PatentDisclosure d = requireDisclosure(disclosureId);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("disclosure", d);
        map.put("attachments", listAttachments(disclosureId));
        map.put("packages", applicationPackageMapper.selectCurrentByDisclosureId(disclosureId));
        map.put("fees", listFees(disclosureId));
        map.put("invoices", listInvoices(disclosureId));
        map.put("statusLogs", listStatusLogs(disclosureId));
        map.put("mailLogs", listMailLogs(disclosureId));
        return map;
    }

    // ---------------- private helpers ----------------

    private void fillNumbers(PatentDisclosure record, boolean auto) {
        if (auto) {
            if (!StringUtils.hasText(record.getTempNo())) {
                record.setTempNo(generateNoService.generateNo("patent_disclosure", "temp_no", "T"));
            }
            if (!StringUtils.hasText(record.getInternalNo())) {
                record.setInternalNo(generateNoService.generateNo("patent_disclosure", "internal_no", "P"));
            }
        } else {
            if (!StringUtils.hasText(record.getTempNo()) || !StringUtils.hasText(record.getInternalNo())) {
                throw new IllegalArgumentException("手动编号时 tempNo 与 internalNo 均必填");
            }
        }
    }

    private void syncFeeAndInvoice(PatentDisclosure d) {
        FeePayment fee = new FeePayment();
        fee.setDisclosureId(d.getId());
        fee.setInternalNo(d.getInternalNo());
        fee.setTempNo(d.getTempNo());
        fee.setDisclosureName(d.getDisclosureName());
        fee.setApplicant(d.getApplicant());
        fee.setFeeType("DEFAULT");
        fee.setPaymentStatus("PENDING");
        fee.setSource("DISCLOSURE_SYNC");
        feePaymentMapper.insert(fee);

        Invoice inv = new Invoice();
        inv.setDisclosureId(d.getId());
        inv.setInternalNo(d.getInternalNo());
        inv.setTempNo(d.getTempNo());
        inv.setDisclosureName(d.getDisclosureName());
        inv.setApplicant(d.getApplicant());
        inv.setInvoiceTitle(d.getApplicant());
        inv.setInvoiceStatus("PENDING");
        inv.setSource("DISCLOSURE_SYNC");
        invoiceMapper.insert(inv);
    }

    private void writeStatusLog(Long disclosureId, String from, String to,
                                Long operatorUserId, String operatorName, String remark) {
        DisclosureStatusLog logRow = new DisclosureStatusLog();
        logRow.setDisclosureId(disclosureId);
        logRow.setFromStatus(from);
        logRow.setToStatus(to);
        logRow.setOperatorUserId(operatorUserId);
        logRow.setOperatorName(operatorName);
        logRow.setRemark(remark);
        disclosureStatusLogMapper.insert(logRow);
    }

    private PatentDisclosure requireDisclosure(Long id) {
        PatentDisclosure d = patentDisclosureMapper.selectById(id);
        if (d == null) {
            throw new IllegalArgumentException("交底不存在: " + id);
        }
        return d;
    }

    private String normalizeStatus(String raw) {
        return PatentStatus.fromCode(raw)
                .map(PatentStatus::codeAsString)
                .orElse(raw.trim());
    }

    private boolean isAtLeastFinalized(String status) {
        Optional<PatentStatus> ps = PatentStatus.fromCode(status);
        if (ps.isPresent()) {
            return ps.get().getStatus() >= PatentStatus.FINALIZED.getStatus();
        }
        return "定稿".equals(status) || "定稿待报".equals(status) || "11".equals(status) || "12".equals(status);
    }

    private List<DisclosureAttachment> resolveMailAttachments(MailSendDTO dto, PatentDisclosure d) {
        if (dto.getAttachmentIds() != null && !dto.getAttachmentIds().isEmpty()) {
            return disclosureAttachmentMapper.selectByIds(dto.getAttachmentIds());
        }
        // 默认附带交底书
        return disclosureAttachmentMapper.selectByDisclosureId(d.getId()).stream()
                .filter(a -> BIZ_DISCLOSURE_DOC.equals(a.getBizType()))
                .collect(Collectors.toList());
    }

    private String render(String template, PatentDisclosure d) {
        if (template == null) return "";
        return template
                .replace("${disclosureName}", nullToEmpty(d.getDisclosureName()))
                .replace("${internalNo}", nullToEmpty(d.getInternalNo()))
                .replace("${tempNo}", nullToEmpty(d.getTempNo()))
                .replace("${contactPerson}", nullToEmpty(d.getContactPerson()))
                .replace("${sponsor}", nullToEmpty(d.getSponsor()))
                .replace("${applicant}", nullToEmpty(d.getApplicant()))
                .replace("${agent}", nullToEmpty(d.getAgent()));
    }

    private String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    private String extensionOf(String filename) {
        int i = filename.lastIndexOf('.');
        return i >= 0 ? filename.substring(i + 1).toLowerCase(Locale.ROOT) : "";
    }

    private boolean isWordExt(String ext) {
        return "doc".equalsIgnoreCase(ext) || "docx".equalsIgnoreCase(ext);
    }

    private StoredFile storeFile(MultipartFile file, String original, String ext) {
        try {
            String diskName = UUID.randomUUID() + (ext.isEmpty() ? "" : "." + ext);
            Path target = uploadPath.resolve(diskName);
            file.transferTo(target.toFile());
            String encoded = URLEncoder.encode(original, StandardCharsets.UTF_8);
            String url = "/files/" + diskName + "?name=" + encoded;
            return new StoredFile(diskName, url);
        } catch (IOException e) {
            throw new IllegalStateException("文件保存失败: " + e.getMessage(), e);
        }
    }

    private record StoredFile(String diskName, String url) {}
}
