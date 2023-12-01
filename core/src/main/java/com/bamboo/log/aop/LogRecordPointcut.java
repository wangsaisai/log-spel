package com.bamboo.log.aop;

import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.AnnotatedElementUtils;

import com.bamboo.log.LogRecord;

import java.lang.reflect.Method;

public class LogRecordPointcut extends StaticMethodMatcherPointcut {

  @Override
  public boolean matches(Method method, Class<?> targetClass) {
    return AnnotatedElementUtils.hasAnnotation(method, LogRecord.class);
  }
}
