package com.chen.intellectualproperty.model.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String email;
    private String password;
    private String nickName;
    private String authCode;
    private String smtpHost;
    private Integer smtpPort;
}
