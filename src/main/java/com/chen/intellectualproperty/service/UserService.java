package com.chen.intellectualproperty.service;

import com.chen.intellectualproperty.model.entity.User;
import com.chen.intellectualproperty.model.vo.UserVO;

import java.util.List;

public interface UserService {

    void register(String email, String password, String nickName, String authCode, List<String> roleCodes);

    UserVO login(String email, String password);

    User findByEmail(String email);

    User findById(Integer id);

    User getUserByToken(String token);

    UserVO getUserInfoByToken(String token);
}
