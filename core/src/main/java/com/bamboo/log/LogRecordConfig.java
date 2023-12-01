package com.bamboo.log;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

import com.bamboo.log.aop.BeanFactoryLogRecordAdvisor;
import com.bamboo.log.aop.LogRecordInterceptor;

@Configuration
public class LogRecordConfig {

  @Bean
  @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
  public LogRecordInterceptor logRecordInterceptor() {
    return new LogRecordInterceptor();
  }

  @Bean
  @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
  public BeanFactoryLogRecordAdvisor logRecordAdvisor() {
    BeanFactoryLogRecordAdvisor advisor = new BeanFactoryLogRecordAdvisor();
    advisor.setAdvice(logRecordInterceptor());
    return advisor;
  }
}
