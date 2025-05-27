package br.com.eliel.gestao_vagas.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.com.eliel.gestao_vagas.security.interceptors.UserActiveInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private UserActiveInterceptor userActiveInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userActiveInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/auth/**", "/swagger-ui/**", "/v3/api-docs/**");
    }
} 