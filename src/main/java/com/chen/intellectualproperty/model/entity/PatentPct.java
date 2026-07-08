package com.chen.intellectualproperty.model.entity;

import com.chen.intellectualproperty.annotation.ExcelField;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * PCT国际申请表
 */
@Data
public class PatentPct {

    private Long id;

    @ExcelField(value = "序号", order = 1)
    private Integer seqNo;

    @ExcelField(value = "PCT内部编号", order = 2)
    private String pctInternalNo;

    @ExcelField(value = "在先内部编号", order = 3)
    private String priorInternalNo;

    @ExcelField(value = "在先申请号", order = 4)
    private String priorApplicationNo;

    @ExcelField(value = "在先申请日", order = 5, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date priorApplicationDate;

    @ExcelField(value = "PCT申请日", order = 6, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pctApplicationDate;

    @ExcelField(value = "申请名称", order = 7)
    private String applicationName;

    @ExcelField(value = "申请号（PCT号）", order = 8)
    private String applicationNo;

    @ExcelField(value = "申请人", order = 9)
    private String applicant;

    @ExcelField(value = "发明人", order = 10)
    private String inventor;

    @ExcelField(value = "主办人", order = 11)
    private String sponsor;

    @ExcelField(value = "委托书代理人", order = 12)
    private String agent;

    @ExcelField(value = "状态", order = 13)
    private String status;

    @ExcelField(value = "发文日", order = 14, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date issueDate;

    @ExcelField(value = "初检结论", order = 15)
    private String preliminaryConclusion;

    @ExcelField(value = "备注", order = 16)
    private String remark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
