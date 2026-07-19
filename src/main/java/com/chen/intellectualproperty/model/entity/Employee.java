package com.chen.intellectualproperty.model.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Employee {
    private Long id;
    /** 关联 user.id，可空（如外部联系人） */
    private Integer userId;
    private String empNo;
    private String name;
    private String email;
    private String phone;
    private String department;
    /** ACTIVE / DISABLED */
    private String status;
    private String remark;
    private Date createTime;
    private Date updateTime;

    /** 非表字段：类型编码列表 */
    private List<String> typeCodes;
    /** 非表字段：类型名称列表 */
    private List<String> typeNames;
}
