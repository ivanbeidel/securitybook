package com.ivanbeidel.securitybook.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
