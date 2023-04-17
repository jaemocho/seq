package com.common.seq.web.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.common.seq.data.entity.User;

public class WithAuthUserSecurityContextFactory implements WithSecurityContextFactory<WithAuthUser> {

    @Override
    public SecurityContext createSecurityContext(WithAuthUser annotation) {
        String email = annotation.email();
        
        User authUser = User.builder()
                         .email(email)
                         .build();

        UsernamePasswordAuthenticationToken token = 
          new UsernamePasswordAuthenticationToken(authUser, "password", null);
        SecurityContext context = SecurityContextHolder.getContext();

        context.setAuthentication(token);
        
        return context;
    }
    
}
