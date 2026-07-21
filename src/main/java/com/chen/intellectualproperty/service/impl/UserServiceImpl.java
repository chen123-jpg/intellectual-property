package com.chen.intellectualproperty.service.impl;

import com.chen.intellectualproperty.exception.BusinessException;
import com.chen.intellectualproperty.mapper.UserMapper;
import com.chen.intellectualproperty.model.entity.Employee;
import com.chen.intellectualproperty.model.entity.User;
import com.chen.intellectualproperty.model.enums.EmployeeRoleCode;
import com.chen.intellectualproperty.model.enums.MailServerConfig;
import com.chen.intellectualproperty.model.vo.SessionUserVO;
import com.chen.intellectualproperty.model.vo.UserVO;
import com.chen.intellectualproperty.redis.RedisUtils;
import com.chen.intellectualproperty.service.EmployeeService;
import com.chen.intellectualproperty.service.UserService;
import com.chen.intellectualproperty.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.chen.intellectualproperty.model.constants.Constants.REDIS_KEY_TOKEN;
import static com.chen.intellectualproperty.model.constants.Constants.REDIS_TIME_1DAY;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final RedisUtils redisUtils;
    private final EmployeeService employeeService;

    @Override
    @Transactional
    public void register(String email, String password, String nickName, String authCode, List<String> roleCodes) {
        User exist = userMapper.findByEmail(email);
        if (exist != null) {
            throw new BusinessException("邮箱已注册");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(PasswordUtils.encode(password));
        user.setNickName(nickName);
        user.setAuthCode(authCode != null && !authCode.isBlank() ? authCode : "N/A");

        MailServerConfig mailConfig = MailServerConfig.fromEmail(email);
        if (mailConfig != null) {
            user.setSmtpHost(mailConfig.getHost());
            user.setSmtpPort(mailConfig.getPort());
        } else {
            user.setSmtpHost("");
            user.setSmtpPort(0);
        }
        userMapper.insert(user);

        // 同步创建员工档案 + 类型
        List<String> codes = roleCodes;
        if (codes == null || codes.isEmpty()) {
            codes = List.of(EmployeeRoleCode.ENTRY);
        }
        try {
            employeeService.ensureEmployeeForUser(user.getId(), nickName, email, codes);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public UserVO login(String email, String password) {
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        String storedPwd = user.getPassword();
        if (storedPwd == null) {
            throw new BusinessException("该账号密码未设置，请联系管理员");
        }
        if (!PasswordUtils.matches(password, storedPwd)) {
            throw new BusinessException("密码错误");
        }
        String token = UUID.randomUUID().toString();
        log.info("login - 存储token到Redis, key: {}, userId: {}", REDIS_KEY_TOKEN + token, user.getId().toString());
        redisUtils.set(REDIS_KEY_TOKEN + token, user.getId().toString(), REDIS_TIME_1DAY);

        // 历史账号无员工档案时自动补 ENTRY，避免完全无法进入系统
        try {
            Employee emp = employeeService.getByUserId(user.getId());
            if (emp == null) {
                employeeService.ensureEmployeeForUser(
                        user.getId(), user.getNickName(), user.getEmail(), List.of(EmployeeRoleCode.ENTRY));
            }
        } catch (Exception e) {
            log.warn("登录时补建员工档案失败 userId={}: {}", user.getId(), e.getMessage());
        }

        return toUserVO(user, token);
    }

    @Override
    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    @Override
    public User findById(Integer id) {
        return userMapper.findById(id);
    }

    @Override
    public User getUserByToken(String token) {
        String redisKey = REDIS_KEY_TOKEN + token;
        Object userIdObj = redisUtils.get(redisKey);
        if (userIdObj == null) {
            throw new BusinessException("未登录或登录已过期");
        }
        User user = userMapper.findById(Integer.parseInt(userIdObj.toString()));
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return user;
    }

    @Override
    public UserVO getUserInfoByToken(String token) {
        User user = getUserByToken(token);
        return toUserVO(user, token);
    }

    private UserVO toUserVO(User user, String token) {
        SessionUserVO session = employeeService.buildSession(
                user.getId(), user.getEmail(), user.getNickName(), token);
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setEmail(user.getEmail());
        vo.setNickName(user.getNickName());
        vo.setToken(token);
        vo.setEmployeeId(session.getEmployeeId());
        vo.setEmployeeName(session.getEmployeeName());
        vo.setEmpNo(session.getEmpNo());
        vo.setDepartment(session.getDepartment());
        vo.setEmployeeStatus(session.getEmployeeStatus());
        vo.setRoles(session.getRoles());
        vo.setRoleNames(session.getRoleNames());
        return vo;
    }
}
