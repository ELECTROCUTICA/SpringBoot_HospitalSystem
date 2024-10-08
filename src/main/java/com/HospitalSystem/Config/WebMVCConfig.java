package com.HospitalSystem.Config;

import com.HospitalSystem.Interceptor.AdminLoginInterceptor;
import com.HospitalSystem.Interceptor.DoctorLoginInterceptor;
import com.HospitalSystem.Interceptor.PatientLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
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
    public void addCorsMappings(CorsRegistry registry) {                  //针对vue 前后端分离的跨域问题解决方案
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "OPTIONS", "PUT", "DELETE")
                .allowedHeaders("Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method",
                        "Access-Control-Request-Headers", "Authorization", "Token")
                .exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
                .allowCredentials(true).maxAge(3600);
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


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminLoginInterceptor).addPathPatterns("/admin")
                .addPathPatterns("/admin/**");

        registry.addInterceptor(doctorLoginInterceptor).addPathPatterns("/doctor")
                .addPathPatterns("/doctor/**");

        registry.addInterceptor(patientLoginInterceptor).addPathPatterns("/patient")
                .addPathPatterns("/patient/**");

    }




}
