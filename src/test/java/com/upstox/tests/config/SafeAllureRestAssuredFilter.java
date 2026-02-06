package com.upstox.tests.config;

import io.qameta.allure.Allure;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.internal.filter.FilterContextImpl;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import java.nio.charset.StandardCharsets;

public class SafeAllureRestAssuredFilter implements Filter {

  @Override
  public Response filter(FilterableRequestSpecification req,
                         FilterableResponseSpecification res,
                         FilterContext ctx) {

    // Execute the real request first (do NOT modify it)
    Response response = ctx.next(req, res);

    // Build a sanitized request dump for Allure (no Authorization value)
    StringBuilder sb = new StringBuilder();
    sb.append(req.getMethod()).append(" ").append(req.getURI()).append("\n\n");
    sb.append("Headers:\n");

    req.getHeaders().forEach(h -> {
      final String name = String.valueOf(h.getName());
      final String value = String.valueOf(h.getValue());

      if ("Authorization".equalsIgnoreCase(name)) {
        sb.append("Authorization: Bearer ***REDACTED***\n");
      } else {
        sb.append(name).append(": ").append(value).append("\n");
      }
    });

    Object bodyObj = req.getBody();
    if (bodyObj != null) {
      sb.append("\nBody:\n").append(String.valueOf(bodyObj)).append("\n");
    }

    // Attach sanitized request + raw response to Allure
    Allure.addAttachment("HTTP Request (sanitized)", "text/plain",
        sb.toString(), ".txt");

    Allure.addAttachment("HTTP Response", "application/json",
        response.asPrettyString(), ".json");

    return response;
  }
}