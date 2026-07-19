package com.chen.intellectualproperty.model.entity;

import lombok.Data;

import java.util.Date;

@Data
public class EmployeeTypeRel {
    private Long id;
    private Long employeeId;
    private String typeCode;
    private Date createTime;
}
