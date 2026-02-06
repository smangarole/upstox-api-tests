package com.upstox.tests.config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class RequestSpecFactory {

  private RequestSpecFactory() {}

  public static RequestSpecification authorized() {
    return new RequestSpecBuilder()
        .setBaseUri(Config.baseUrl())
        .setAccept(ContentType.JSON)
        .setContentType(ContentType.JSON)
        .addHeader("Authorization", "Bearer " + Config.accessToken())
        .log(LogDetail.METHOD)
        .log(LogDetail.URI)
        .build();
  }

  public static RequestSpecification unauthenticated() {
    return new RequestSpecBuilder()
        .setBaseUri(Config.baseUrl())
        .setAccept(ContentType.JSON)
        .setContentType(ContentType.JSON)
        .log(LogDetail.METHOD)
        .log(LogDetail.URI)
        .build();
  }
}
