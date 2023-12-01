package com.bamboo.log.other;

public class ThreadLocalOperator {

  private ThreadLocalOperator() {}

  private static final ThreadLocal<String> operatorThreadLocal = new ThreadLocal<>();

  public static void setOperator(String operator) {
    operatorThreadLocal.set(operator);
  }

  public static String getOperator() {
    return operatorThreadLocal.get();
  }

  public static void clear() {
    operatorThreadLocal.remove();
  }
}
