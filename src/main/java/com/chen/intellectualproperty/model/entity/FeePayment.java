package com.chen.intellectualproperty.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class FeePayment {
    private Long id;
    private Long disclosureId;
    private String internalNo;
    private String tempNo;
    private String disclosureName;
    private String applicant;
    private String feeType;
    private BigDecimal feeAmount;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date paymentDeadline;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date paymentDate;
    private String paymentStatus;
    private String payer;
    private String remark;
    private String source;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
