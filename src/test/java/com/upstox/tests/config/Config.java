package com.upstox.tests.config;

public final class Config {

  private Config() {}

  public static String baseUrl() {
    // Upstox base: https://api.upstox.com/v2
    return getEnvOrDefault("UPSTOX_BASE_URL", "https://api.upstox.com/v2");
  }

  public static String accessToken() {
    // Store ONLY the token string (without "Bearer ")
    return getEnv("UPSTOX_ACCESS_TOKEN");
  }

  private static String getEnv(String key) {
    String val = System.getenv(key);
    if (val == null || val.trim().isEmpty()) {
      throw new IllegalStateException(
          "Missing required environment variable: " + key +
          " (set it in your terminal/CI secrets)"
      );
    }
    return val.trim();
  }

  private static String getEnvOrDefault(String key, String defaultValue) {
    String val = System.getenv(key);
    if (val == null || val.trim().isEmpty()) return defaultValue;
    return val.trim();
  }
}
