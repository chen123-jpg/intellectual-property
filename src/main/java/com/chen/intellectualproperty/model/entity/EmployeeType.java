package com.chen.intellectualproperty.model.entity;

import lombok.Data;

import java.util.Date;

@Data
public class EmployeeType {
    private Long id;
    private String typeCode;
    private String typeName;
    private String description;
    /** 是否需要登录账号 */
    private Integer needLogin;
    private Integer isSystem;
    private Integer enabled;
    private Integer sortNo;
    private Date createTime;
    private Date updateTime;
}
