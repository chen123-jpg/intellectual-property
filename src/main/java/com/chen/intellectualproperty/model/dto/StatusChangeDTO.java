package com.chen.intellectualproperty.model.dto;

import lombok.Data;

@Data
public class StatusChangeDTO {
    /** 目标状态：码如 11 或文案如 定稿 */
    private String toStatus;
    private String remark;
    private Long operatorUserId;
    private String operatorName;
}
