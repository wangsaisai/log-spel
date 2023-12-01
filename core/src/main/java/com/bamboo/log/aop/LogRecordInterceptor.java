package com.bamboo.log.aop;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.expression.EvaluationContext;

import com.bamboo.log.LogRecord;
import com.bamboo.log.LogRecordExpressionEvaluator;
import com.bamboo.log.LogRecordSaver;
import com.bamboo.log.MethodExecuteResult;
import com.bamboo.log.ThreadLocalLogVariable;
import com.bamboo.log.other.LogRecordEntity;
import com.bamboo.log.other.ThreadLocalOperator;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Slf4j
public class LogRecordInterceptor
    implements MethodInterceptor, BeanFactoryAware, SmartInitializingSingleton {

  private BeanFactory beanFactory;

  private LogRecordSaver logRecordSaver;

  private final LogRecordExpressionEvaluator expressionEvaluator =
      new LogRecordExpressionEvaluator();

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Method method = invocation.getMethod();
    return execute(invocation, invocation.getThis(), method, invocation.getArguments());
  }

  private Object execute(MethodInvocation invoker, Object target, Method method, Object[] args)
      throws Throwable {
    // 代理不拦截
    if (AopUtils.isAopProxy(target)) {
      return invoker.proceed();
    }

    Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
    Object ret = null;
    MethodExecuteResult methodExecuteResult = new MethodExecuteResult(method, args, targetClass);

    LocalDateTime startTime = LocalDateTime.now();
    try {
      ret = invoker.proceed();
      methodExecuteResult.setResult(ret);
    } catch (Exception e) {
      methodExecuteResult.setException(e);
    }
    LocalDateTime endTime = LocalDateTime.now();

    LogRecord op = AnnotatedElementUtils.findMergedAnnotation(method, LogRecord.class);
    try {
      saveLogRecord(methodExecuteResult, op, startTime, endTime);
    } catch (Exception t) {
      log.error("save log record exception", t);
    }

    ThreadLocalLogVariable.clear();
    if (methodExecuteResult.getException() != null) {
      throw methodExecuteResult.getException();
    }
    return ret;
  }

  private void saveLogRecord(
      MethodExecuteResult methodExecuteResult,
      LogRecord op,
      LocalDateTime startTime,
      LocalDateTime endTime) {
    EvaluationContext evaluationContext =
        expressionEvaluator.createEvaluationContext(methodExecuteResult);
    AnnotatedElementKey annotatedElementKey =
        new AnnotatedElementKey(
            methodExecuteResult.getMethod(), methodExecuteResult.getTargetClass());

    String associateId =
        expressionEvaluator.parseExpression(
            op.associateIdExpr(), annotatedElementKey, evaluationContext, String.class);

    boolean execResult = true;
    String extra = "";
    if (methodExecuteResult.getException() == null) {
      if (StringUtils.isNotBlank(op.resultExpr())) {
        execResult =
            expressionEvaluator.parseExpression(
                op.resultExpr(), annotatedElementKey, evaluationContext, Boolean.class);
      }
      if (StringUtils.isNotBlank(op.extraExpr())) {
        extra =
            expressionEvaluator.parseExpression(
                op.extraExpr(), annotatedElementKey, evaluationContext, String.class);
      }
    } else {
      execResult = false;
      extra = methodExecuteResult.getException().toString();
    }

    String operator = ThreadLocalOperator.getOperator();
    operator = StringUtils.isBlank(operator) ? "-" : operator;

    LogRecordEntity logRecord =
        LogRecordEntity.builder()
            .associateId(associateId)
            .logType(op.type())
            .operation(op.operation())
            .extra(extra)
            .execResult(execResult)
            .operator(operator)
            .startTime(startTime)
            .endTime(endTime)
            .build();
    logRecordSaver.save(logRecord);
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }

  @Override
  public void afterSingletonsInstantiated() {
    this.logRecordSaver = beanFactory.getBean(LogRecordSaver.class);
  }
}
