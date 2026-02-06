package com.upstox.tests.config;

import org.testng.annotations.BeforeSuite;

public class TestSuiteHooks {

  @BeforeSuite(alwaysRun = true)
  public void beforeSuite() {
    TestRunStore.clear();
    AllureEnvironmentWriter.write();
    AllureExecutorWriter.write();
  }
}
