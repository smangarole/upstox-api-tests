package com.upstox.tests.config;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class AllureEnvironmentWriter {

  private AllureEnvironmentWriter() {}

  public static void write() {
  String resultsDir = System.getProperty(
      "allure.results.directory",
      "target/allure-results"
  );

  Path resultsPath = Path.of(resultsDir);
  Path out = resultsPath.resolve("environment.properties");

    try {
      // ✅ ensure directory exists
      Files.createDirectories(resultsPath);

      Properties p = new Properties();
      p.setProperty("baseUrl", Config.baseUrl());
      p.setProperty("runGroups", System.getProperty("groups", "all"));
      p.setProperty("javaVersion", System.getProperty("java.version"));
      p.setProperty("os", System.getProperty("os.name") + " " + System.getProperty("os.version"));

      try (OutputStream os = Files.newOutputStream(out)) {
        p.store(os, "Allure environment");
      }

    } catch (IOException e) {
      throw new RuntimeException(
          "Failed to write Allure environment.properties to: " + out,
          e
      );
    }
  }
}