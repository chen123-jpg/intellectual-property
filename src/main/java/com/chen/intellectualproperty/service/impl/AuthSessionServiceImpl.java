package com.chen.intellectualproperty.service.impl;

import com.chen.intellectualproperty.exception.BusinessException;
import com.chen.intellectualproperty.model.entity.User;
import com.chen.intellectualproperty.model.enums.EmployeeRoleCode;
import com.chen.intellectualproperty.model.vo.SessionUserVO;
import com.chen.intellectualproperty.service.AuthSessionService;
import com.chen.intellectualproperty.service.EmployeeService;
import com.chen.intellectualproperty.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * 基于 token 解析当前会话，并提供角色校验。
 */
@Service
@RequiredArgsConstructor
public class AuthSessionServiceImpl implements AuthSessionService {

    private final UserService userService;
    private final EmployeeService employeeService;

    @Override
    public SessionUserVO requireSession(String token) {
        if (!StringUtils.hasText(token)) {
            throw new BusinessException("请先登录");
        }
        User user = userService.getUserByToken(token);
        return employeeService.buildSession(user.getId(), user.getEmail(), user.getNickName(), token);
    }

    @Override
    public SessionUserVO requireActiveEmployee(String token) {
        SessionUserVO s = requireSession(token);
        if (s.getEmployeeId() == null) {
            throw new BusinessException("当前账号未绑定员工档案，请联系管理员在「员工管理」中配置类型");
        }
        if ("DISABLED".equalsIgnoreCase(s.getEmployeeStatus())) {
            throw new BusinessException("员工账号已停用");
        }
        return s;
    }

    @Override
    public SessionUserVO requireAnyRole(String token, String... roles) {
        SessionUserVO s = requireActiveEmployee(token);
        if (!EmployeeRoleCode.hasAny(s.roleSet(), roles)) {
            throw new BusinessException("权限不足，需要角色: " + String.join("/", roles));
        }
        return s;
    }

    @Override
    public boolean hasAnyRole(SessionUserVO s, String... roles) {
        return s != null && EmployeeRoleCode.hasAny(s.roleSet(), roles);
    }

    @Override
    public void requireSponsorOf(SessionUserVO s, Long sponsorUserId) {
        if (s == null) {
            throw new BusinessException("请先登录");
        }
        if (EmployeeRoleCode.isAdmin(s.roleSet())) {
            return;
        }
        if (!EmployeeRoleCode.hasAny(s.roleSet(), EmployeeRoleCode.SPONSOR)) {
            throw new BusinessException("仅主办人可操作");
        }
        if (sponsorUserId == null || s.getUserId() == null
                || !sponsorUserId.equals(s.getUserId().longValue())) {
            throw new BusinessException("只能操作自己主办的交底");
        }
    }

    @Override
    public Set<String> rolesOf(SessionUserVO s) {
        return s == null ? Set.of() : s.roleSet();
    }
}
