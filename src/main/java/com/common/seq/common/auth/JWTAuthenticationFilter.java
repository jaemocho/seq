package com.common.seq.common.auth;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.common.seq.common.Constants.JWTException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JWTAuthenticationFilter extends GenericFilterBean {

    private final JWTProvider jwtProvider;

    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String token = resolveToken((HttpServletRequest)request);


        // 토큰에 이상이 없으면 
        // UsernamePasswordAuthenticationToken 을 생성해서 SecurityContextHolder 에 등록 
        // UsernamePasswordAuthenticationToken AbstractAuthenticationToken 상속 받고 Authentication 구현 
        if ( token != null ) {
            String result = jwtProvider.validateToken(token);
            if ("SUCCESS".equals(result)) {
                Authentication authentication = jwtProvider.getAuthentication(token);

                // 인증 완료 인증 객체가 있기 때문에 뒤에 필터에서 추가 인증은 없다 
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                request.setAttribute("TokenVaildResult", result);    
            }
        } else {
            request.setAttribute("TokenVaildResult", JWTException.NOTFOUND_JWT.getJwtException());
        }

        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_HEADER);
    }
}
