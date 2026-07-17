package com.chen.intellectualproperty.mapper;

import com.chen.intellectualproperty.model.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO user(email, password, nick_name, auth_code, smtp_host, smtp_port) " +
            "VALUES(#{email}, #{password}, #{nickName}, #{authCode}, #{smtpHost}, #{smtpPort})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Select("SELECT * FROM user WHERE email = #{email}")
    User findByEmail(String email);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Integer id);
}