package com.chen.intellectualproperty.model.dto;

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
}
