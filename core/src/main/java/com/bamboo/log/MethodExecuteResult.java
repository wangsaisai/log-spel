package com.bamboo.log;

import lombok.Data;

import java.lang.reflect.Method;

@Data
public class MethodExecuteResult {

  private final Method method;
  private final Object[] args;
  private final Class<?> targetClass;

  private Object result;
  private Exception exception;

  public MethodExecuteResult(Method method, Object[] args, Class<?> targetClass) {
    this.method = method;
    this.args = args;
    this.targetClass = targetClass;
  }
}
