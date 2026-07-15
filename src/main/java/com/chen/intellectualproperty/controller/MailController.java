package com.chen.intellectualproperty.controller;

import com.chen.intellectualproperty.model.dto.MailRequest;
import com.chen.intellectualproperty.service.impl.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMail(@ModelAttribute MailRequest request) {
        try {
            mailService.sendMail(request.getTo(), request.getSubject(),
                    request.getText(), request.getAttachments());
            return ResponseEntity.ok(Collections.singletonMap("message", "发送成功"));
        } catch (MessagingException | IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Collections.singletonMap("error", "发送失败：" + e.getMessage()));
        }
    }
}