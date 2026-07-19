package com.chen.intellectualproperty.model.dto;

import lombok.Data;

@Data
public class EmployeeTypeSaveDTO {
    private Long id;
    private String typeCode;
    private String typeName;
    private String description;
    private Integer needLogin;
    private Integer enabled;
    private Integer sortNo;
}
