package com.chen.intellectualproperty.controller;

import com.chen.intellectualproperty.model.dto.MailRequest;
import com.chen.intellectualproperty.model.entity.User;
import com.chen.intellectualproperty.service.UserService;
import com.chen.intellectualproperty.service.impl.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;
    private final UserService userService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMail(@RequestParam String token, @ModelAttribute MailRequest request) {
        log.info("sendMail - 接收token: [{}], to: {}, subject: {}", token, request.getTo(), request.getSubject());
        try {
            User sender = userService.getUserByToken(token);
            mailService.sendMail(sender, request.getTo(), request.getSubject(),
                    request.getText(), request.getAttachments());
            return ResponseEntity.ok(Collections.singletonMap("message", "发送成功"));
        } catch (MessagingException | IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Collections.singletonMap("error", "发送失败：" + e.getMessage()));
        }
    }
}
