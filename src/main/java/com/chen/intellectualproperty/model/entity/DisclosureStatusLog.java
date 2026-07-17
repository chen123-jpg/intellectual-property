package com.chen.intellectualproperty.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DisclosureStatusLog {
    private Long id;
    private Long disclosureId;
    private String fromStatus;
    private String toStatus;
    private Long operatorUserId;
    private String operatorName;
    private String remark;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
