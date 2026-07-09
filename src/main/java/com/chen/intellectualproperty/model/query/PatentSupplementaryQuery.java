package com.chen.intellectualproperty.model.query;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * 补漏专利表 查询对象
 *
 * @author 
 */
@Data
public class PatentSupplementaryQuery {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 序号
     */
    private Integer seqNo;

    /**
     * 申请号/专利号
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
     * 通知书（状态下子列）
     */
    private String notification;

    /**
     * 发文日（状态下子列）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date issueDate;

    /**
     * 费减（状态下子列）
     */
    private String feeReduction;

    /**
     * 备注
     */
    private String remark;

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

    /**
     * 排序参数（例如："create_time DESC"、"name ASC, age DESC"）
     */
    private String orderBy;

}
