package com.upstox.tests.config;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class AuthRedactionFilter implements Filter {

  @Override
  public Response filter(FilterableRequestSpecification req,
                         FilterableResponseSpecification res,
                         FilterContext ctx) {

    if (req.getHeaders().hasHeaderWithName("Authorization")) {
      // overwrite instead of removing
      req.header("Authorization", "Bearer ***REDACTED***");
    }

    return ctx.next(req, res);
  }
}
