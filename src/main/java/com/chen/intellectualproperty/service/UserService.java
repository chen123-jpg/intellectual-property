package com.chen.intellectualproperty.service;

import com.chen.intellectualproperty.exception.BusinessException;
import com.chen.intellectualproperty.mapper.UserMapper;
import com.chen.intellectualproperty.model.entity.User;
import com.chen.intellectualproperty.model.vo.UserVO;
import com.chen.intellectualproperty.redis.RedisUtils;
import com.chen.intellectualproperty.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.chen.intellectualproperty.model.constants.Constants.REDIS_KEY_TOKEN;
import static com.chen.intellectualproperty.model.constants.Constants.REDIS_TIME_1DAY;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final RedisUtils redisUtils;

    public void register(String email, String password, String nickName) {
        User exist = userMapper.findByEmail(email);
        if (exist != null) {
            throw new BusinessException("邮箱已注册");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(PasswordUtils.encode(password));
        user.setNickName(nickName);
        userMapper.insert(user);
    }

    public UserVO login(String email, String password) {
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (!PasswordUtils.matches(password, user.getPassword())) {
            throw new BusinessException("密码错误");
        }
        String token = UUID.randomUUID().toString();
        redisUtils.set(REDIS_KEY_TOKEN + token, user.getId().toString(), REDIS_TIME_1DAY);

        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setEmail(user.getEmail());
        vo.setNickName(user.getNickName());
        vo.setToken(token);
        return vo;
    }

    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    public User findById(Integer id) {
        return userMapper.findById(id);
    }

    public User getUserByToken(String token) {
        String userIdStr = (String) redisUtils.get(REDIS_KEY_TOKEN + token);
        if (userIdStr == null) {
            throw new BusinessException("未登录或登录已过期");
        }
        User user = userMapper.findById(Integer.parseInt(userIdStr));
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return user;
    }

    public UserVO getUserInfoByToken(String token) {
        String userIdStr = (String) redisUtils.get(REDIS_KEY_TOKEN + token);
        if (userIdStr == null) {
            throw new BusinessException("未登录或登录已过期");
        }
        User user = userMapper.findById(Integer.parseInt(userIdStr));
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setEmail(user.getEmail());
        vo.setNickName(user.getNickName());
        vo.setToken(token);
        return vo;
    }
}
