package com.ivanbeidel.securitybook.configuration;

import ch.qos.logback.core.util.StringUtil;
import com.ivanbeidel.securitybook.service.jwt.AppUserService;
import com.ivanbeidel.securitybook.service.jwt.JwtSecurityService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtSecurityService jwtSecurityService;
    private final AppUserService appUserService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if(StringUtils.isEmpty(header)
        || !org.apache.commons.lang3.StringUtils.startsWith(header, "Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = header.substring(7);
        String email = jwtSecurityService.extractUsername(jwt);

        if(StringUtils.isNoneEmpty(email)
            &&SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = appUserService
                    .getDetailsService()
                    .loadUserByUsername(email);

            if(jwtSecurityService.validateToken(jwt, userDetails)){
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(token);
                SecurityContextHolder.setContext(securityContext);
            }
        }
        filterChain.doFilter(request, response);
    }
}
