package com.chen.intellectualproperty.controller;

import com.chen.intellectualproperty.security.CustomUserDetails;
import com.chen.intellectualproperty.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String email,
                                      @RequestParam String password,
                                      @RequestParam String authCode,
                                      @RequestParam(required = false) String smtpHost,
                                      @RequestParam(required = false) Integer smtpPort) {
        if (userService.findByEmail(email) != null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "邮箱已注册"));
        }
        userService.register(email, password, authCode, smtpHost, smtpPort);
        return ResponseEntity.ok(Collections.singletonMap("message", "注册成功"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> currentUser(@AuthenticationPrincipal CustomUserDetails user) {
        Map<String, Object> info = new HashMap<>();
        info.put("id", user.getId());
        info.put("email", user.getEmail());
        // 不返回授权码等敏感信息
        return ResponseEntity.ok(info);
    }
}