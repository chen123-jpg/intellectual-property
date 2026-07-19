package com.chen.intellectualproperty.model.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 当前登录会话：账号 + 员工档案 + 类型编码。
 */
@Data
public class SessionUserVO {
    private Integer userId;
    private String email;
    private String nickName;
    private String token;

    private Long employeeId;
    private String employeeName;
    private String empNo;
    private String department;
    private String employeeStatus;

    /** 类型编码 */
    private List<String> roles = new ArrayList<>();
    /** 类型中文名 */
    private List<String> roleNames = new ArrayList<>();

    public Set<String> roleSet() {
        return roles == null ? new HashSet<>() : new HashSet<>(roles);
    }

    public boolean hasRole(String code) {
        return roleSet().contains(code);
    }

    public boolean isAdmin() {
        return hasRole("ADMIN");
    }
}
