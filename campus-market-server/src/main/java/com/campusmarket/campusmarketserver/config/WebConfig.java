package com.campusmarket.campusmarketserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final AdminInterceptor adminInterceptor;

    public WebConfig(LoginInterceptor loginInterceptor, AdminInterceptor adminInterceptor) {
        this.loginInterceptor = loginInterceptor;
        this.adminInterceptor = adminInterceptor;
    }

    @Value("${campus.upload.dir:./data/uploads}")
    private String uploadDir;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/user/register",
                        "/api/user/login",
                        "/api/product/list",
                        "/api/product/detail/**",
                        "/api/category/list",
                        "/uploads/**"
                );
        // 管理后台：先过登录拦截器，再过管理员校验
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/api/admin/**");
    }

    /** 把上传目录映射成静态资源，浏览器直接访问 /uploads/xxx.jpg */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }
}
