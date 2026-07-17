package com.chen.intellectualproperty.model.query;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

@Data
public class PatentDisclosureQuery {

    private Long id;
    private String tempNo;
    private String internalNo;
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
    private String sponsor;
    private Long sponsorUserId;
    private Long entryUserId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date disclosureDate;

    /** 交底日起 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date disclosureDateFrom;

    /** 交底日止 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date disclosureDateTo;

    private Integer disclosureDays;
    private String remark;
    private String contactInfo;
    private String contactEmail;
    private Integer syncedToPatent;

    /** 名称模糊 */
    private String disclosureNameLike;
    /** 关键词：匹配编号/名称/申请人/联系人 */
    private String keyword;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private String orderBy;
}
