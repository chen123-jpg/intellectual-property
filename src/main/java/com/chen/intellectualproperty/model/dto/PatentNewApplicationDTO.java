package com.chen.intellectualproperty.model.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * 专利新申请表 DTO
 *
 * @author 
 */
@Data
public class PatentNewApplicationDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 内部编号
     */
    private String internalNo;

    /**
     * 发明创造名称
     */
    private String patentName;

    /**
     * 申请号/专利号
     */
    private String applicationNo;

    /**
     * 申请人
     */
    private String applicant;

    /**
     * 发明人
     */
    private String inventor;

    /**
     * 主办人
     */
    private String sponsor;

    /**
     * 委托书代理人
     */
    private String agent;

    /**
     * 申请日
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applicationDate;

    /**
     * 通知书（状态子列）
     */
    private String notification;

    /**
     * 发文日（状态子列）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date issueDate;

    /**
     * 非正标-预审标（状态子列）
     */
    private String preExamMark;

    /**
     * 缴费止期（费用子列）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date paymentDeadline;

    /**
     * 费用金额（费用子列）
     */
    private Double feeAmount;

    /**
     * 缴费时间（费用子列）
     */
    private String paymentDate;

    /**
     * 序号
     */
    private Integer seqNo;

    /**
     * 类型（发明/实用新型/外观设计）
     */
    private String patentType;

    /**
     * DAS码
     */
    private String dasCode;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
