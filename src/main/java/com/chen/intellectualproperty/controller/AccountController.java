package com.chen.intellectualproperty.controller;

import com.chen.intellectualproperty.exception.BusinessException;
import com.chen.intellectualproperty.model.constants.Constants;
import com.chen.intellectualproperty.model.dto.LoginDTO;
import com.chen.intellectualproperty.model.dto.RegisterDTO;
import com.chen.intellectualproperty.model.dto.Result;
import com.chen.intellectualproperty.model.vo.UserVO;
import com.chen.intellectualproperty.redis.RedisUtils;
import com.chen.intellectualproperty.service.UserService;
import com.wf.captcha.ArithmeticCaptcha;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public Result register(@RequestBody @Valid RegisterDTO dto) {
        log.info("注册请求 - email: {}, nickName: {}", dto.getEmail(), dto.getNickName());
        try {
            validateCheckCode(dto.getCheckCodeKey(), dto.getCheckCode());
            userService.register(dto.getEmail(), dto.getPassword(), dto.getNickName());
            log.info("注册成功 - email: {}", dto.getEmail());
            return Result.successMsg("注册成功");
        } catch (BusinessException e) {
            log.warn("注册失败 - {}", e.getMessage());
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            log.error("注册失败 - 系统异常", e);
            return Result.fail("系统异常，请稍后重试");
        } finally {
            redisUtils.del(Constants.REDIS_KEY_CHECK_CODE + dto.getCheckCodeKey());
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody @Valid LoginDTO dto) {
        log.info("登录请求 - email: {}", dto.getEmail());
        try {
            validateCheckCode(dto.getCheckCodeKey(), dto.getCheckCode());
            UserVO userVO = userService.login(dto.getEmail(), dto.getPassword());
            log.info("登录成功 - email: {}", dto.getEmail());
            return Result.success(userVO);
        } catch (BusinessException e) {
            log.warn("登录失败 - {}", e.getMessage());
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            log.error("登录失败 - 系统异常", e);
            return Result.fail("系统异常，请稍后重试");
        } finally {
            redisUtils.del(Constants.REDIS_KEY_CHECK_CODE + dto.getCheckCodeKey());
        }
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/me")
    public Result me(@RequestParam(required = false) String token) {
        if (token == null || token.isEmpty()) {
            return Result.fail("请先登录");
        }
        try {
            UserVO userVO = userService.getUserInfoByToken(token);
            return Result.success(userVO);
        } catch (BusinessException e) {
            return Result.fail(e.getMessage());
        }
    }

    private void validateCheckCode(String checkCodeKey, String checkCode) {
        Object storedObj = redisUtils.get(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey);
        if (storedObj == null) {
            throw new BusinessException("验证码不存在或已过期");
        }
        if (!checkCode.equalsIgnoreCase(storedObj.toString())) {
            throw new BusinessException("验证码不匹配");
        }
    }
}
