package com.chen.intellectualproperty.entity;

import com.chen.intellectualproperty.annotation.ExcelField;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * 专利新申请表
 */
@Data
public class PatentNewApplication {

    private Long id;

    @ExcelField(value = "序号", order = 1)
    private Integer seqNo;

    @ExcelField(value = "内部编号", order = 2)
    private String internalNo;

    @ExcelField(value = "发明创造名称", order = 3)
    private String patentName;

    @ExcelField(value = "申请号/专利号", order = 4)
    private String applicationNo;

    @ExcelField(value = "类型", order = 5)
    private String patentType;

    @ExcelField(value = "申请人", order = 6)
    private String applicant;

    @ExcelField(value = "发明人", order = 7)
    private String inventor;

    @ExcelField(value = "主办人", order = 8)
    private String sponsor;

    @ExcelField(value = "委托书代理人", order = 9)
    private String agent;

    @ExcelField(value = "申请日", order = 10, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applicationDate;

    @ExcelField(value = "通知书", order = 11)
    private String notification;

    @ExcelField(value = "发文日", order = 12, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date issueDate;

    @ExcelField(value = "非正标-预审标", order = 13)
    private String preExamMark;

    @ExcelField(value = "缴费止期", order = 14, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date paymentDeadline;

    @ExcelField(value = "费用金额", order = 15)
    private Double feeAmount;

    @ExcelField(value = "缴费时间", order = 16)
    private String paymentDate;

    @ExcelField(value = "DAS码", order = 17)
    private String dasCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
