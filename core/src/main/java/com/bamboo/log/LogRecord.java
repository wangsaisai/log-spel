package com.bamboo.log;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LogRecord {

  // 日志关联的对象id
  String associateIdExpr();

  // product, component, component_version...
  LogBizEnum type();

  // todo 是否设成枚举
  // 操作类型：add, update, get, delete等
  String operation();

  // 额外记录的信息。若执行异常，则记录异常信息
  String extraExpr() default "";

  // 执行是否成功
  String resultExpr() default "";
}
