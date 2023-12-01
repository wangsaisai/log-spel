package com.bamboo.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(scanBasePackages = {"com.bamboo.log"})
@MapperScan("com.bamboo.log.mapper")
public class LogApplication {

  public static void main(String[] args) {
    SpringApplication.run(LogApplication.class, args);
  }

  // 支持跨域
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");
      }
    };
  }
}
