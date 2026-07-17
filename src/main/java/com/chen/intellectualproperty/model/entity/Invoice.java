package com.chen.intellectualproperty.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Invoice {
    private Long id;
    private Long disclosureId;
    private String internalNo;
    private String tempNo;
    private String disclosureName;
    private String applicant;
    private String invoiceTitle;
    private String taxNo;
    private String invoiceType;
    private BigDecimal invoiceAmount;
    private String invoiceStatus;
    private String invoiceNo;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date invoiceDate;
    private String remark;
    private String source;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
