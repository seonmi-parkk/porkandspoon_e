package kr.co.porkandspoon.config;

import org.springframework.context.annotation.Configuration;
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
}
