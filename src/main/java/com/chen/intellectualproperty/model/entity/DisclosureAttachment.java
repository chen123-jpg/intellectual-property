package com.chen.intellectualproperty.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DisclosureAttachment {
    private Long id;
    private Long disclosureId;
    private String internalNo;
    /** DISCLOSURE_DOC / DISCLOSURE_OTHER / MAIL_EXTRA */
    private String bizType;
    private Long mailSendLogId;
    private String fileName;
    private String fileExt;
    private String filePath;
    private String fileUrl;
    private Long fileSize;
    private String contentType;
    private Integer isRequired;
    private Integer sortNo;
    private Long uploadUserId;
    private String uploadUserName;
    private Integer deleted;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
