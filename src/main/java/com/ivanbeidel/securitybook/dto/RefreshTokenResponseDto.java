package com.ivanbeidel.securitybook.dto;

import lombok.Data;

@Data
public class RefreshTokenResponseDto {
    private String jwtToken;
    private String refreshToken;
}
