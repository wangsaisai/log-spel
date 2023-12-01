package com.bamboo.log;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalLogVariable {

  private ThreadLocalLogVariable() {}

  private static final ThreadLocal<Map<String, Object>> variableThreadLocal = new ThreadLocal<>();

  public static void put(String name, Object value) {
    if (variableThreadLocal.get() == null) {
      variableThreadLocal.set(new HashMap<>());
    }
    variableThreadLocal.get().put(name, value);
  }

  public static Map<String, Object> getVariables() {
    return variableThreadLocal.get();
  }

  public static void clear() {
    variableThreadLocal.remove();
  }
}
