package com.chen.intellectualproperty.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * 中间著变专利表（有重复） DTO
 *
 * @author 
 */
@Data
public class PatentIntermediateChangeDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 序号
     */
    private Integer seqNo;

    /**
     * 内部编号
     */
    private String internalNo;

    /**
     * 业务类型（转让/转我所/著录变更等）
     */
    private String businessType;

    /**
     * 申请号
     */
    private String applicationNo;

    /**
     * 发明创造名称
     */
    private String patentName;

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
     * 状态
     */
    private String status;

    /**
     * 发文日
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date issueDate;

    /**
     * 非正标-费减情况
     */
    private String feeReductionInfo;

    /**
     * 提交日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submitDate;

    /**
     * 缴费止期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date paymentDeadline;

    /**
     * 费用金额
     */
    private Double feeAmount;

    /**
     * 缴费状态
     */
    private String paymentStatus;

    /**
     * 备注1
     */
    private String remark1;

    /**
     * 备注2
     */
    private String remark2;

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
