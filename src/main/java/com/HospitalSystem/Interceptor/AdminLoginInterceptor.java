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
public class AdminLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String URI = request.getRequestURI();
        if (URI.contains("login")) {
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("Admin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return false;
        }
        else {
            return true;
        }

    }
}
