package com.upstox.tests.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class AllureExecutorWriter {

  private AllureExecutorWriter() {}

  public static void write() {
    String resultsDir = System.getProperty("allure.results.directory", "target/allure-results");
    Path resultsPath = Path.of(resultsDir);

    try {
      Files.createDirectories(resultsPath);

      String serverUrl = env("GITHUB_SERVER_URL");
      String repo = env("GITHUB_REPOSITORY");
      String runId = env("GITHUB_RUN_ID");
      String runNumber = env("GITHUB_RUN_NUMBER");
      String sha = env("GITHUB_SHA");
      String branch = env("GITHUB_REF_NAME");

      // Example: https://github.com/<org>/<repo>/actions/runs/<runId>
      String buildUrl = serverUrl + "/" + repo + "/actions/runs/" + runId;

      String json = "{\n" +
          "  \"name\": \"GitHub Actions\",\n" +
          "  \"type\": \"github\",\n" +
          "  \"buildName\": \"upstox-api-tests\",\n" +
          "  \"buildOrder\": \"" + escape(runNumber) + "\",\n" +
          "  \"buildUrl\": \"" + escape(buildUrl) + "\",\n" +
          "  \"reportUrl\": \"\",\n" +
          "  \"reportName\": \"Allure Report\",\n" +
          "  \"url\": \"" + escape(buildUrl) + "\",\n" +
          "  \"branch\": \"" + escape(branch) + "\",\n" +
          "  \"commit\": \"" + escape(sha) + "\"\n" +
          "}";

      Files.writeString(resultsPath.resolve("executor.json"), json);

    } catch (IOException e) {
      throw new RuntimeException("Failed to write Allure executor.json", e);
    }
  }

  private static String env(String key) {
    String v = System.getenv(key);
    return v == null ? "" : v;
  }

  private static String escape(String s) {
    if (s == null) return "";
    return s.replace("\\", "\\\\").replace("\"", "\\\"");
  }
}
