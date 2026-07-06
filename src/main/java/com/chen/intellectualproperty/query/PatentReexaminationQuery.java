package com.chen.intellectualproperty.query;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * 复审无效专利表 查询对象
 *
 * @author 
 */
@Data
public class PatentReexaminationQuery {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 序号
     */
    private Integer seqNo;

    /**
     * 类型（发明/实用新型/外观设计）
     */
    private String patentType;

    /**
     * 分类（复审/无效）
     */
    private String category;

    /**
     * 案件编号
     */
    private String caseNo;

    /**
     * 内部编号
     */
    private String internalNo;

    /**
     * 申请号
     */
    private String applicationNo;

    /**
     * 专利名称
     */
    private String patentName;

    /**
     * 申请人
     */
    private String applicant;

    /**
     * 主办人
     */
    private String sponsor;

    /**
     * 委托书代理人
     */
    private String agent;

    /**
     * 通知书
     */
    private String notification;

    /**
     * 发文日
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date issueDate;

    /**
     * 提交日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submitDate;

    /**
     * 25.6.12查询
     */
    private String queryInfo;

    /**
     * 规费
     */
    private Double officialFee;

    /**
     * 缴费时间
     */
    private String paymentDate;

    /**
     * 附注1
     */
    private String note1;

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
