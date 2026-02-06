package com.upstox.tests.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class TestRunStore {

  private static final Map<String, Object> STORE = new ConcurrentHashMap<>();

  private TestRunStore() {}

  public static void put(String key, Object value) {
    STORE.put(key, value);
  }

  @SuppressWarnings("unchecked")
  public static <T> T get(String key, Class<T> type) {
    Object val = STORE.get(key);
    if (val == null) {
      return null;
    }
    return (T) val;
  }

  public static void clear() {
    STORE.clear();
  }
}
