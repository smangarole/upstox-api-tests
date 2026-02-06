package com.upstox.tests;

import com.upstox.tests.client.UpstoxApiClient;
import com.upstox.tests.config.RequestSpecFactory;
import org.testng.annotations.Test;
import io.qameta.allure.*;
import static org.hamcrest.Matchers.*;

@Epic("Upstox API")
@Feature("User")
public class UserProfileAuthNegativeTest {

  @Test(groups = {"negative"})
  @Story("Unauthorized access is blocked")
  @Severity(SeverityLevel.NORMAL)
  @Description("Calls /user/profile without token and expects 401 with an error payload.")
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
