package com.HospitalSystem.Config;

import com.HospitalSystem.Interceptor.AdminLoginInterceptor;
import com.HospitalSystem.Interceptor.DoctorLoginInterceptor;
import com.HospitalSystem.Interceptor.PatientLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Autowired
    private AdminLoginInterceptor adminLoginInterceptor;

    @Autowired
    private DoctorLoginInterceptor doctorLoginInterceptor;

    @Autowired
    private PatientLoginInterceptor patientLoginInterceptor;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/index");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/*.css").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/*.js").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/*.ttf").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/*.jpg").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/*.png").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/*.woff").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/*.woff2").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/*.svg").addResourceLocations("classpath:/static/");
    }

    @Bean
    public HandlerInterceptor getPatientLoginInterceptor() {
        return new PatientLoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(adminLoginInterceptor).addPathPatterns("/admin")
//                .addPathPatterns("/admin/adminNav")
//                .addPathPatterns("/admin/schedule")
//                .addPathPatterns("/admin/doctorinfo")
//                .addPathPatterns("/admin/department");
//
        registry.addInterceptor(doctorLoginInterceptor).addPathPatterns("/doctor")
                .addPathPatterns("/doctor/**");

        registry.addInterceptor(getPatientLoginInterceptor()).addPathPatterns("/patient")
//                .addPathPatterns("/patient/patientHeader")
//                .addPathPatterns("/patient/patientNav")
//                .addPathPatterns("/patient/home")
//                .addPathPatterns("/patient/edit")
//                .addPathPatterns("/patient/record")
//                .addPathPatterns("/patient/registration");
                .addPathPatterns("/patient/**");

    }




}
