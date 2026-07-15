package com.chen.intellectualproperty.service;


import com.chen.intellectualproperty.model.entity.User;
import com.chen.intellectualproperty.mapper.UserMapper;
import com.chen.intellectualproperty.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final PasswordUtils passwordEncoder;

    public void register(String email, String password, String authCode,
                         String smtpHost, Integer smtpPort) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setAuthCode(authCode);
        user.setSmtpHost(smtpHost);
        user.setSmtpPort(smtpPort);
        userMapper.insert(user);
    }

    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }
}