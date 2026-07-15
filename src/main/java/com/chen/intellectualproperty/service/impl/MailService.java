package com.chen.intellectualproperty.service.impl;

import com.chen.intellectualproperty.model.enums.MailServerConfig;
import com.chen.intellectualproperty.security.CustomUserDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Slf4j
@Service
public class MailService {

    public void sendMail(String to, String subject, String text,
                         List<MultipartFile> attachments) throws MessagingException, IOException {
        // 获取当前登录用户
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        String email = user.getEmail();
        String authCode = user.getAuthCode();
        String smtpHost = user.getSmtpHost();
        Integer smtpPort = user.getSmtpPort();

        // 创建邮件发送器
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        if (smtpHost != null && smtpPort != null) {
            // 使用用户自定义的 SMTP 配置
            mailSender.setHost(smtpHost);
            mailSender.setPort(smtpPort);
        } else {
            // 自动识别邮箱服务商
            MailServerConfig config = MailServerConfig.fromEmail(email);
            if (config == null) {
                throw new IllegalArgumentException("暂不支持该邮箱服务商: " + email + "，请在注册时填写自定义SMTP信息");
            }
            mailSender.setHost(config.getHost());
            mailSender.setPort(config.getPort());
        }

        mailSender.setUsername(email);
        mailSender.setPassword(authCode);

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        // 根据端口设置加密方式
        if (mailSender.getPort() == 587) {
            props.put("mail.smtp.starttls.enable", "true");
        } else if (mailSender.getPort() == 465) {
            props.put("mail.smtp.ssl.enable", "true");
        }
        mailSender.setJavaMailProperties(props);

        // 构建邮件
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(email);
        helper.setTo(to.split(","));
        helper.setSubject(subject);
        helper.setText(text, false);   // 如需 HTML 改为 true

        if (attachments != null && !attachments.isEmpty()) {
            for (MultipartFile file : attachments) {
                helper.addAttachment(file.getOriginalFilename(),
                        new ByteArrayResource(file.getBytes()), file.getContentType());
            }
        }

        mailSender.send(message);
        log.info("邮件已从 {} 发送至 {}", email, to);
    }
}