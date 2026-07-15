package com.chen.intellectualproperty.model.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Data
public class MailRequest {
    private String to;
    private String subject;
    private String text;
    private List<MultipartFile> attachments;
}
