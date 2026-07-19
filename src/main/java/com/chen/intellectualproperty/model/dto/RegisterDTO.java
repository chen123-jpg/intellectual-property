package com.chen.intellectualproperty.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RegisterDTO {
    @NotEmpty String checkCodeKey;
    @NotEmpty String checkCode;
    @NotEmpty @Email String email;
    @NotEmpty String password;
    @NotEmpty String nickName;
    String authCode;
    /**
     * 注册时选择的员工类型编码（可多选），如 ENTRY/SPONSOR/PROCESS。
     * 为空时默认 ENTRY。
     */
    java.util.List<String> roleCodes;
}
