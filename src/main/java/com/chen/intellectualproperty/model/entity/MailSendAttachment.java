package com.chen.intellectualproperty.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class MailSendAttachment {
    private Long id;
    private Long mailSendLogId;
    private Long disclosureAttachmentId;
    private String fileName;
    private String filePath;
    private String fileUrl;
    private Long fileSize;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
