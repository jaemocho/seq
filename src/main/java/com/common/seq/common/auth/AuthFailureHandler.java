package com.common.seq.common.auth;

import java.io.IOException;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler{
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        
        String msg = "Invaild Email or Password";

        if (exception instanceof UsernameNotFoundException) {
            msg = "UsernameNotFoundException account";
        }else if (exception instanceof DisabledException) {
            msg = "DisabledException account";
        } else if (exception instanceof CredentialsExpiredException) {
            msg = "CredentialsExpiredException account";
        } else if (exception instanceof BadCredentialsException) {
            msg = "BadCredentialsException account";
        } else if (exception instanceof AccountExpiredException) {
            msg = "AccountException account";
        } else if (exception instanceof LockedException) {
            msg = "LockedException account";
        }
        
        setDefaultFailureUrl("/login?error=true&excption"+msg);


        super.onAuthenticationFailure(request, response, exception);
    }
}
