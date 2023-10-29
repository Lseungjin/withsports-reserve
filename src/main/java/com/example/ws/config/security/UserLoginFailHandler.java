package com.example.ws.config.security;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
public class UserLoginFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMsg="에러 없음!";

        if (exception instanceof AuthenticationServiceException) {
            errorMsg="존재하지 않는 사용자입니다.";

        } else if(exception instanceof BadCredentialsException) {
            errorMsg="아이디 또는 비밀번호가 틀립니다.";

        } else if(exception instanceof LockedException) {
            errorMsg="잠긴 계정입니다..";

        } else if(exception instanceof DisabledException) {
            errorMsg="비활성화된 계정입니다..";

        } else if(exception instanceof AccountExpiredException) {
            errorMsg="만료된 계정입니다..";

        } else if(exception instanceof CredentialsExpiredException) {
            errorMsg="비밀번호가 만료되었습니다.";
        }


        // 로그인 페이지로 다시 포워딩
        RequestDispatcher dispatcher = request.getRequestDispatcher("/loginFail?errorMsg="+errorMsg);
        dispatcher.forward(request, response);
    }
}
