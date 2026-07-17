package com.chen.intellectualproperty.controller;

import com.chen.intellectualproperty.exception.BusinessException;
import com.chen.intellectualproperty.model.constants.Constants;
import com.chen.intellectualproperty.model.dto.Result;
import com.chen.intellectualproperty.model.vo.UserVO;
import com.chen.intellectualproperty.redis.RedisUtils;
import com.chen.intellectualproperty.service.UserService;
import com.wf.captcha.ArithmeticCaptcha;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final RedisUtils redisUtils;
    private final UserService userService;

    /**
     * 生成图形验证码
     */
    @GetMapping("/checkCode")
    public Result checkCode(@RequestParam(required = false) String oldCheckCodeKey) {
        if (oldCheckCodeKey != null) {
            redisUtils.del(Constants.REDIS_KEY_CHECK_CODE + oldCheckCodeKey);
        }
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(100, 42);
        String code = captcha.text();
        String checkCodeKey = UUID.randomUUID().toString();
        log.info("验证码: {}, key: {}", code, checkCodeKey);
        redisUtils.set(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey, code, Constants.REDIS_TIME_1MIN);

        Map<String, String> result = new HashMap<>();
        result.put("checkCode", captcha.toBase64());
        result.put("checkCodeKey", checkCodeKey);
        return Result.success(result);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result register(@RequestParam @NotEmpty String checkCodeKey,
                           @RequestParam @NotEmpty String checkCode,
                           @RequestParam @NotEmpty @Email String email,
                           @RequestParam @NotEmpty String password,
                           @RequestParam @NotEmpty String nickName) {
        log.info("注册请求 - email: {}, nickName: {}", email, nickName);
        try {
            validateCheckCode(checkCodeKey, checkCode);
            userService.register(email, password, nickName);
            log.info("注册成功 - email: {}", email);
            return Result.successMsg("注册成功");
        } catch (BusinessException e) {
            log.warn("注册失败 - {}", e.getMessage());
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            log.error("注册失败 - 系统异常", e);
            return Result.fail("系统异常，请稍后重试");
        } finally {
            redisUtils.del(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey);
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result login(@RequestParam @NotEmpty String checkCodeKey,
                        @RequestParam @NotEmpty String checkCode,
                        @RequestParam @NotEmpty String email,
                        @RequestParam @NotEmpty String password) {
        log.info("登录请求 - email: {}", email);
        try {
            validateCheckCode(checkCodeKey, checkCode);
            UserVO userVO = userService.login(email, password);
            log.info("登录成功 - email: {}", email);
            return Result.success(userVO);
        } catch (BusinessException e) {
            log.warn("登录失败 - {}", e.getMessage());
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            log.error("登录失败 - 系统异常", e);
            return Result.fail("系统异常，请稍后重试");
        } finally {
            redisUtils.del(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey);
        }
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/me")
    public Result me(@RequestParam @NotEmpty String token) {
        try {
            UserVO userVO = userService.getUserInfoByToken(token);
            return Result.success(userVO);
        } catch (BusinessException e) {
            return Result.fail(e.getMessage());
        }
    }

    private void validateCheckCode(String checkCodeKey, String checkCode) {
        String storedCode = (String) redisUtils.get(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey);
        if (storedCode == null) {
            throw new BusinessException("验证码不存在或已过期");
        }
        if (!checkCode.equalsIgnoreCase(storedCode)) {
            throw new BusinessException("验证码不匹配");
        }
    }
}
