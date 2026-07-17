package com.chen.intellectualproperty.model.dto;

import lombok.Data;

@Data
public class DisclosureCopyDTO {
    /** 源交底 ID */
    private Long sourceId;
    /** 是否复制附件元数据（不复制磁盘文件内容时仍指向同一文件 URL） */
    private Boolean copyAttachments = true;
    /** true 自动编号 */
    private Boolean autoGenerateNo = true;
}
