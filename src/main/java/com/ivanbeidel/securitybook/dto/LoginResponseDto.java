package com.ivanbeidel.securitybook.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {
    public String email;
    public String jwtToken;
    public String refreshToken;
}
