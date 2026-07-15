package com.chen.intellectualproperty.model.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String email;
    private String password;
    private String authCode;
    private String smtpHost;    // 自定义SMTP服务器（可选）
    private Integer smtpPort;   // 自定义SMTP端口（可选）
}
