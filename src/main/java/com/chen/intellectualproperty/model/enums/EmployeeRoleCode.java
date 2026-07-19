package com.chen.intellectualproperty.model.enums;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

/**
 * 员工类型编码（与 employee_type.type_code 一致，可扩展字典表）。
 */
public final class EmployeeRoleCode {

    private EmployeeRoleCode() {}

    /** 录入人员 */
    public static final String ENTRY = "ENTRY";
    /** 主办人 */
    public static final String SPONSOR = "SPONSOR";
    /** 流程人员 */
    public static final String PROCESS = "PROCESS";
    /** 联系人（可无登录） */
    public static final String CONTACT = "CONTACT";
    /** 管理员 */
    public static final String ADMIN = "ADMIN";

    public static final Set<String> ALL_BUILTIN = Set.of(ENTRY, SPONSOR, PROCESS, CONTACT, ADMIN);

    public static Optional<String> normalize(String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(code.trim().toUpperCase());
    }

    public static boolean isAdmin(Set<String> roles) {
        return roles != null && roles.contains(ADMIN);
    }

    public static boolean hasAny(Set<String> roles, String... codes) {
        if (roles == null || roles.isEmpty() || codes == null) {
            return false;
        }
        if (roles.contains(ADMIN)) {
            return true;
        }
        return Arrays.stream(codes).anyMatch(roles::contains);
    }
}
