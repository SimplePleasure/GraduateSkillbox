package com.graduate.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

@Configuration
@PropertySource("${classpath:application.yml}")
@EnableAutoConfiguration
public class ImgConfiguration {

    @Value("${project.uploads-max-size}")
    private int maxFileSize;

    @Bean
    public MultipartConfigElement getMultipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofKilobytes(maxFileSize));
        factory.setMaxRequestSize(DataSize.ofKilobytes(maxFileSize));
        return factory.createMultipartConfig();
    }
}
