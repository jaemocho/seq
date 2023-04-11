package com.common.seq.common.auth;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.common.seq.common.Constants.JWTException;
import com.common.seq.common.Constants.JWTType;
import com.common.seq.data.dao.RefreshTokenDAO;
import com.common.seq.data.entity.RefreshToken;
import com.common.seq.data.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class JWTProvider {
    
    protected static final String AUTHORITIES_KEY = "auth";

    private final RefreshTokenDAO refreshTokenDAO;

    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.access-token-validity-in-seconds}")
    private long accessTokenValidityInseconds;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityInseconds;

    protected Key key;

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }
    

    // 토큰 생성
    public String createToken(Authentication authentication, JWTType type) {
        
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();

        long tokenValidityInseconds = 0L;

        if (JWTType.ACCESS_TOKEN.getJwtType().equals(type.getJwtType())) {
            tokenValidityInseconds = accessTokenValidityInseconds;
        } else  {
            tokenValidityInseconds = refreshTokenValidityInseconds;
        } 

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidityInseconds)) // set Expire Time
                .signWith(key, SignatureAlgorithm.HS256)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                        .parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();


        // Collection<? extends GrantedAuthority> authorities =
        // Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
        //         .map(SimpleGrantedAuthority::new)
        //         .collect(Collectors.toList());

        // 디비를 거치지 않고 토큰에서 값을 꺼내 바로 시큐리티 유저 객체를 만들어 Authentication을 만들어 반환하기에 유저네임, 권한 외 정보는 알 수 없다.
        User user = User.builder()
                        .email(claims.getSubject())
                        .build();

        return new UsernamePasswordAuthenticationToken(user, token, null);
    }

    public String validateToken(String token) {
        String result = "SUCCESS";
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
            return result;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
             result = JWTException.MALFORMED_JWT.getJwtException();
        } catch (ExpiredJwtException e) {
             result = JWTException.EXPIRED_JWT.getJwtException();
        } catch (UnsupportedJwtException e) {
             result = JWTException.UNSUPPORTED_JWT.getJwtException();
        } catch (IllegalArgumentException e) {
             result = JWTException.ILLEGALARGUMENT.getJwtException();
        }

        log.info("[JWTProvider vaildateToken] {}", result);
             
        return result;
    }

    public Boolean vaildateAccessRefreshTokenMatch(String accessToken, String refreshToken) {
        RefreshToken refreshTokenEntity = refreshTokenDAO.getRefreshToken(refreshToken);
        
        if ( refreshTokenEntity != null ) {
            if(refreshTokenEntity.getAccessToken().equals(accessToken) 
            && refreshTokenEntity.getRefreshToken().equals(refreshToken)) {
                return true;
        }
        }
        return false;
    }

    @Transactional
    public void updateRefreshToken(String newAccessToken, String refreshToken) {
        
        RefreshToken refreshTokenEntity = refreshTokenDAO.getRefreshToken(refreshToken);
        
        refreshTokenEntity.setAccessToken(newAccessToken);
    }
}
