package com.chen.intellectualproperty.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class EmployeeSaveDTO {
    private Long id;
    private Integer userId;
    private String empNo;
    private String name;
    private String email;
    private String phone;
    private String department;
    private String status;
    private String remark;
    /** 类型编码列表，如 ENTRY, SPONSOR */
    private List<String> typeCodes;
}
