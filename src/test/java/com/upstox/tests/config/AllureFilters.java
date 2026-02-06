package com.upstox.tests.config;

import io.qameta.allure.restassured.AllureRestAssured;

public final class AllureFilters {
  private AllureFilters() {}

  public static AllureRestAssured allure() {
    return new AllureRestAssured();
  }
}
