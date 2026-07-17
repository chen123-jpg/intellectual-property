package com.chen.intellectualproperty.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ApplicationPackage {
    private Long id;
    private Long disclosureId;
    private String internalNo;
    /** XML_PACKAGE / FIVE_BOOKS_WORD */
    private String packageType;
    private String fileName;
    private String fileExt;
    private String filePath;
    private String fileUrl;
    private Long fileSize;
    private String contentType;
    private Integer versionNo;
    private Integer isCurrent;
    private Long uploadUserId;
    private String uploadUserName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date uploadTime;
    private String confirmStatus;
    private Long confirmUserId;
    private String confirmUserName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date confirmTime;
    private String remark;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
