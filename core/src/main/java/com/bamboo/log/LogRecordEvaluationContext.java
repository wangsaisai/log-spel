package com.bamboo.log;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;

import org.apache.commons.collections4.MapUtils;

import java.lang.reflect.Method;
import java.util.Map;

public class LogRecordEvaluationContext extends MethodBasedEvaluationContext {

  public LogRecordEvaluationContext(
      Object rootObject,
      Method method,
      Object[] arguments,
      ParameterNameDiscoverer parameterNameDiscoverer,
      Object ret,
      Exception e) {
    super(rootObject, method, arguments, parameterNameDiscoverer);

    this.setVariable("_ret", ret);
    this.setVariable("_e", e);

    Map<String, Object> variables = ThreadLocalLogVariable.getVariables();
    if (MapUtils.isNotEmpty(variables)) {
      variables.forEach(this::setVariable);
    }
  }
}
