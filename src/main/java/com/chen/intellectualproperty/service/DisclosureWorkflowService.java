package com.chen.intellectualproperty.service;

import com.chen.intellectualproperty.model.dto.DisclosureCopyDTO;
import com.chen.intellectualproperty.model.dto.DisclosureCreateDTO;
import com.chen.intellectualproperty.model.dto.MailSendDTO;
import com.chen.intellectualproperty.model.dto.StatusChangeDTO;
import com.chen.intellectualproperty.model.entity.*;
import com.chen.intellectualproperty.model.query.PatentDisclosureQuery;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 交底流程业务：录入同步、复制、状态、附件、申请包、邮件、定稿待报同步 P 表。
 */
public interface DisclosureWorkflowService {

    PatentDisclosure createDisclosure(DisclosureCreateDTO dto);

    /**
     * 录入交底并强制上传交底书（Word），可附带其他附件。
     * 同事务：主表 + 缴费/开票 + 附件。
     */
    PatentDisclosure createDisclosureWithAttachments(DisclosureCreateDTO dto,
                                                     MultipartFile disclosureDoc,
                                                     MultipartFile[] otherFiles,
                                                     Long uploadUserId,
                                                     String uploadUserName);

    PatentDisclosure copyDisclosure(DisclosureCopyDTO dto);

    PatentDisclosure updateDisclosure(Long id, PatentDisclosure record);

    List<PatentDisclosure> search(PatentDisclosureQuery query);

    List<PatentDisclosure> listBySponsorUserId(Long sponsorUserId);

    PatentDisclosure changeStatus(Long id, StatusChangeDTO dto);

    DisclosureAttachment uploadAttachment(Long disclosureId, String bizType, MultipartFile file,
                                          Long uploadUserId, String uploadUserName);

    List<DisclosureAttachment> listAttachments(Long disclosureId);

    void deleteAttachment(Long attachmentId);

    ApplicationPackage uploadPackage(Long disclosureId, String packageType, MultipartFile file,
                                     Long uploadUserId, String uploadUserName);

    List<ApplicationPackage> listPackages(Long disclosureId);

    ApplicationPackage confirmPackage(Long packageId, Long confirmUserId, String confirmUserName);

    /** 流程确认后进入定稿待报并同步申请专利表（幂等） */
    Map<String, Object> markPendingReportAndSync(Long disclosureId, StatusChangeDTO dto);

    List<MailTemplate> listMailTemplates();

    MailTemplate previewTemplate(String templateCode, Long disclosureId);

    MailSendLog sendMail(MailSendDTO dto);

    List<MailSendLog> listMailLogs(Long disclosureId);

    List<FeePayment> listFees(Long disclosureId);

    List<Invoice> listInvoices(Long disclosureId);

    List<DisclosureStatusLog> listStatusLogs(Long disclosureId);

    Map<String, Object> detail(Long disclosureId);
}
