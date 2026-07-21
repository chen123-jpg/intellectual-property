package com.chen.intellectualproperty.service;

import com.chen.intellectualproperty.model.vo.SessionUserVO;

import java.util.Set;

/**
 * 基于 token 解析当前会话，并提供角色校验。
 */
public interface AuthSessionService {

    SessionUserVO requireSession(String token);

    SessionUserVO requireActiveEmployee(String token);

    SessionUserVO requireAnyRole(String token, String... roles);

    boolean hasAnyRole(SessionUserVO s, String... roles);

    void requireSponsorOf(SessionUserVO s, Long sponsorUserId);

    Set<String> rolesOf(SessionUserVO s);
}
