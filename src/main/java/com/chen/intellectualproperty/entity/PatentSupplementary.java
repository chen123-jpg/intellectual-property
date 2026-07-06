package com.chen.intellectualproperty.entity;

import com.chen.intellectualproperty.annotation.ExcelField;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * 补漏专利表
 */
@Data
public class PatentSupplementary {

    private Long id;

    @ExcelField(value = "序号", order = 1)
    private Integer seqNo;

    @ExcelField(value = "申请号/专利号", order = 2)
    private String applicationNo;

    @ExcelField(value = "发明创造名称", order = 3)
    private String patentName;

    @ExcelField(value = "申请人", order = 4)
    private String applicant;

    @ExcelField(value = "发明人", order = 5)
    private String inventor;

    @ExcelField(value = "主办人", order = 6)
    private String sponsor;

    @ExcelField(value = "委托书代理人", order = 7)
    private String agent;

    @ExcelField(value = "申请日", order = 8, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applicationDate;

    @ExcelField(value = "通知书", order = 9)
    private String notification;

    @ExcelField(value = "发文日", order = 10, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date issueDate;

    @ExcelField(value = "费减", order = 11)
    private String feeReduction;

    @ExcelField(value = "备注", order = 12)
    private String remark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
