package kr.co.porkandspoon.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/photo/**")
                .addResourceLocations("file:/Users/ckpark/Desktop/dev/goodee/photo/");
        registry.addResourceHandler("/photoTem/**")
                .addResourceLocations("file:/Users/ckpark/Desktop/dev/goodee/photoTem/");
    }

    // ngringer cors 설정
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8300", "http://127.0.0.1:8300")
                .allowedMethods("*")
                .allowCredentials(true);
    }
}
