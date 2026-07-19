package com.chen.intellectualproperty.model.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录/当前用户视图（含员工类型，兼容旧字段 id/nickName/token）。
 */
@Data
public class UserVO {
    /** 登录账号 user.id */
    private Integer id;
    private String email;
    private String nickName;
    private String token;

    private Long employeeId;
    private String employeeName;
    private String empNo;
    private String department;
    private String employeeStatus;

    /** 员工类型编码列表 */
    private List<String> roles = new ArrayList<>();
    /** 员工类型名称列表 */
    private List<String> roleNames = new ArrayList<>();
}
