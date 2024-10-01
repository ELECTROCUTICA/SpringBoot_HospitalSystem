package com.HospitalSystem.Interceptor;
import com.HospitalSystem.Entity.Patient;
import com.HospitalSystem.Utils.JWTUtils;
import jakarta.servlet.http.Cookie;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class PatientLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String URI = request.getRequestURI();
        if (URI.contains("login") || URI.contains("register") || URI.contains("logout") || request.getMethod().equals("OPTIONS")) {
            return true;
        }

        String token = request.getHeader("Authorization");
        Patient patient = JWTUtils.getPatientFromToken(token);

        return patient.getId() != null;

    }
}
