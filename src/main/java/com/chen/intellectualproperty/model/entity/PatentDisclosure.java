package com.chen.intellectualproperty.model.entity;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * 专利交底信息表（T表）
 */
@Data
public class PatentDisclosure {

    private Long id;

    /** 临时编号（如 T2026001） */
    private String tempNo;

    /** 内部编号（与 P 表关联键） */
    private String internalNo;

    /** 交底状态码或文案 */
    private String patentStatus;

    private String requirement;

    private String disclosureName;

    private String applicant;

    private String inventor;

    private String patentType;

    private Boolean invitedToGroup;

    private String contactPerson;

    private String manager;

    private String agent;

    /** 主办人姓名 */
    private String sponsor;

    /** 主办人用户 ID */
    private Long sponsorUserId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date disclosureDate;

    private Integer disclosureDays;

    private String remark;

    private String contactInfo;

    private String contactEmail;

    private String contactPhone;

    private Long entryUserId;

    private String entryUserName;

    /** 复制来源交底 ID */
    private Long copyFromId;

    /** AUTO / MANUAL */
    private String noGenerateMode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date finalizedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pendingReportAt;

    /** 是否已同步申请专利表 */
    private Integer syncedToPatent;

    private Long patentApplicationId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
