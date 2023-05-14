package com.common.seq.common.auth;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.common.seq.common.Constants.JWTException;
import com.common.seq.common.Constants.JWTType;

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
    public static final String REFRESH_TOKEN_HEADER = "Refresh-token";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String accessToken = resolveToken((HttpServletRequest)request, AUTHORIZATION_HEADER);
        String refreshToken = resolveToken((HttpServletRequest)request, REFRESH_TOKEN_HEADER);
        String result = checkAccessToken(request, accessToken, refreshToken);
        
        if (JWTException.EXPIRED_JWT.getJwtException().equals(result)) {
            checkRefreshToken(request, accessToken, refreshToken);
        } else {
            request.setAttribute("TokenVaildResult", result);
        }
        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request, String type) {
        return request.getHeader(type);
    }

    private String checkAccessToken(ServletRequest request, String accessToken, String refreshToken) {
        String result = validateToken(accessToken);
        
        if ("SUCCESS".equals(result)) {
            // 토큰에 이상이 없으면 
            // UsernamePasswordAuthenticationToken 을 생성해서 SecurityContextHolder 에 등록 
            // UsernamePasswordAuthenticationToken AbstractAuthenticationToken 상속 받고 Authentication 구현 
            Authentication authentication = jwtProvider.getAuthentication(accessToken);

            // 인증 완료 인증 객체가 있기 때문에 뒤에 필터에서 추가 인증은 없다 
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "SUCCESS";
        } 
        return result;
    }

    private void checkRefreshToken(ServletRequest request, String accessToken, String refreshToken) {
            
        String result = validateToken(refreshToken);

        if ("SUCCESS".equals(result)) {
            vaildateAccessRefreshTokenMatch(request, accessToken, refreshToken);
        } else {
            request.setAttribute("TokenVaildResult", "[Refresh] " + result);    
        }
    }

    private void vaildateAccessRefreshTokenMatch(ServletRequest request, String accessToken, String refreshToken) {
        // access token과 refresh token이 db에 저장되어있는 값과 같으면  
        // refreshtoken의 정보로 accesstoken 재발급 
        if (jwtProvider.vaildateAccessRefreshTokenMatch(accessToken, refreshToken) ) {

            // refresh token의 username, role을 authentication 형태로 가져온다
            Authentication authentication = jwtProvider.getAuthentication(refreshToken);
            
            // 가져온 정보를 이용해 access token 발급
            String newAccessToken = jwtProvider.createToken(authentication, JWTType.ACCESS_TOKEN);

            // refreshtoken accesstoken 매칭 update 
            jwtProvider.updateRefreshToken(newAccessToken, refreshToken);

            // 요청자에게 돌려 줄 new access token
            request.setAttribute("newAccessToken", newAccessToken); 

        } else {
            // db에 저장된 access token과 refresh token의 매칭이 맞지 않을 때 
            request.setAttribute("TokenVaildResult", JWTException.UNMATCHED_REFRESH_ACCESS_JWT.getJwtException());    
        }
    }

    private String validateToken(String token) {
        
        if ( token != null ) { 
            return jwtProvider.validateToken(token);
         
        } else {
            return JWTException.NOTFOUND_JWT.getJwtException();
        }
    }
}
