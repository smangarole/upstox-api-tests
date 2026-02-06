package com.upstox.tests;

import com.upstox.tests.client.UpstoxApiClient;
import com.upstox.tests.config.RequestSpecFactory;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class UserProfileAuthNegativeTest {

  @Test(groups = {"negative"})
  public void shouldReturn401WhenTokenIsMissing() {
    UpstoxApiClient api =
        new UpstoxApiClient(RequestSpecFactory.unauthenticated());

    api.getUserProfile()
        .then()
        .log().ifValidationFails()
        .statusCode(401)
        .body("status", equalTo("error"))
        .body("errors.size()", greaterThan(0));
  }
}
