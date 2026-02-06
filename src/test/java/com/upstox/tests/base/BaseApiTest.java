package com.upstox.tests.base;

import com.upstox.tests.config.Config;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeMethod;

public abstract class BaseApiTest {

  protected RequestSpecification req;

  @BeforeMethod
  public void setup() {
    System.out.println(">> BaseApiTest.setup() ran. Building req...");
    RestAssured.baseURI = Config.baseUrl();

    // Build a reusable "request template"
    req = new RequestSpecBuilder()
        .setAccept(ContentType.JSON)
        .setContentType(ContentType.JSON)
        .addHeader("Authorization", "Bearer " + Config.accessToken())
        // Log request details BUT avoid headers (token lives there)
        .log(LogDetail.METHOD)
        .log(LogDetail.URI)
        .log(LogDetail.BODY)
        .build();

    if (req == null) {
      throw new IllegalStateException("RequestSpecification 'req' was not initialized");
    }
  }
}
