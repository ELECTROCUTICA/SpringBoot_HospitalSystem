package com.HospitalSystem.Interceptor;
import com.HospitalSystem.Entity.Patient;
import com.HospitalSystem.Utils.JWTUtils;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class PatientLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String URI = request.getRequestURI();
        Cookie[] cookies = request.getCookies();
        String token = null;

        if (cookies == null) {
            return true;
        }
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals("patient_token")) {
                token = cookies[i].getValue();
                break;
            }
        }

        if (URI.contains("login") || URI.contains("register")) {            //已登录状态下不允许返回登录页面
            if (token != null && !token.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/patient/home");
                return false;
            }
            return true;
        }

        if (token == null ||token.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/patient/login");
            return false;
        }
        else {
            request.setAttribute("patient_token", token);
            return true;
        }

//        HttpSession session = request.getSession(false);
//        if (session == null || session.getAttribute("Patient") == null) {
//            response.sendRedirect(request.getContextPath() + "/patient/login");
//            return false;
//        }
//        else {
//            return true;
//        }


    }
}
