package com.chen.intellectualproperty.model.entity;

import com.chen.intellectualproperty.annotation.ExcelField;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * 复审无效专利表
 */
@Data
public class PatentReexamination {

    private Long id;

    @ExcelField(value = "序号", order = 1)
    private Integer seqNo;

    @ExcelField(value = "类型", order = 2)
    private String patentType;

    @ExcelField(value = "分类", order = 3)
    private String category;

    @ExcelField(value = "案件编号", order = 4)
    private String caseNo;

    @ExcelField(value = "内部编号", order = 5)
    private String internalNo;

    @ExcelField(value = "申请号", order = 6)
    private String applicationNo;

    @ExcelField(value = "专利名称", order = 7)
    private String patentName;

    @ExcelField(value = "申请人", order = 8)
    private String applicant;

    @ExcelField(value = "主办人", order = 9)
    private String sponsor;

    @ExcelField(value = "委托书代理人", order = 10)
    private String agent;

    @ExcelField(value = "通知书", order = 11)
    private String notification;

    @ExcelField(value = "发文日", order = 12, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date issueDate;

    @ExcelField(value = "提交日期", order = 13, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submitDate;

    @ExcelField(value = "25.6.12查询", order = 14)
    private String queryInfo;

    @ExcelField(value = "规费", order = 15)
    private Double officialFee;

    @ExcelField(value = "缴费时间", order = 16)
    private String paymentDate;

    @ExcelField(value = "附注1", order = 17)
    private String note1;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
