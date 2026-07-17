package com.chen.intellectualproperty.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class MailSendLog {
    private Long id;
    private Long disclosureId;
    private String internalNo;
    private Long templateId;
    private String templateCode;
    private String fromEmail;
    private String toEmails;
    private String ccEmails;
    private String subject;
    private String content;
    private String sendStatus;
    private String errorMessage;
    private Long senderUserId;
    private String senderName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sentAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
