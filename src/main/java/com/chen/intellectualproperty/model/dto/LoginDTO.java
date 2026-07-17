package com.chen.intellectualproperty.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginDTO {
    @NotEmpty String checkCodeKey;
    @NotEmpty String checkCode;
    @NotEmpty @Email String email;
    @NotEmpty String password;
}
