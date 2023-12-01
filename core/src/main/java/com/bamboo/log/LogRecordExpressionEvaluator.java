package com.bamboo.log;

import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LogRecordExpressionEvaluator extends CachedExpressionEvaluator {

  // 缓存表达式，减少编译成本
  private final Map<ExpressionKey, Expression> expressionCache = new ConcurrentHashMap<>(1024);

  public <T> T parseExpression(
      String conditionExpression,
      AnnotatedElementKey elementKey,
      EvaluationContext evalContext,
      Class<T> resultType) {
    return getExpression(this.expressionCache, elementKey, conditionExpression)
        .getValue(evalContext, resultType);
  }

  /**
   * Create an {@link EvaluationContext}.
   *
   * @param method the method
   * @param args the method arguments
   * @param result the return value (can be {@code null}) or
   * @return the evaluation context
   */
  public EvaluationContext createEvaluationContext(
      Method method, Object[] args, Object result, Exception e) {
    return new LogRecordEvaluationContext(
        null, method, args, getParameterNameDiscoverer(), result, e);
  }

  public EvaluationContext createEvaluationContext(MethodExecuteResult methodExecuteResult) {
    return createEvaluationContext(
        methodExecuteResult.getMethod(),
        methodExecuteResult.getArgs(),
        methodExecuteResult.getResult(),
        methodExecuteResult.getException());
  }
}
