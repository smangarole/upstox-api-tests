package com.upstox.tests.client;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UpstoxApiClient {

  private final RequestSpecification spec;

  public UpstoxApiClient(RequestSpecification spec) {
    this.spec = spec;
  }

  public Response getUserProfile() {
    return given()
        .spec(spec)
    .when()
        .get("/user/profile");
  }
}