package com.upstox.tests;

import com.upstox.tests.client.UpstoxApiClient;
import com.upstox.tests.config.RequestSpecFactory;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.qameta.allure.*;
import static org.hamcrest.Matchers.*;

@Epic("Upstox API")
@Feature("User")
public class UserProfileTest {

  @Test(groups = {"smoke"})
  @Story("Fetch user profile")
  @Severity(SeverityLevel.CRITICAL)
  @Description("Fetches the authenticated user's profile and validates key fields.")
  public void shouldFetchUserProfile() {
    UpstoxApiClient api =
        new UpstoxApiClient(RequestSpecFactory.authorized());

    Response response = api.getUserProfile();

    response.then()
        .log().ifValidationFails()
        .statusCode(200)
        .body("status", anyOf(equalTo("success"), equalTo("SUCCESS")))
        .body("data.user_id", notNullValue())
        .body("data.user_name", not(isEmptyOrNullString()));

    JsonPath jp = response.jsonPath();
    String userId = jp.getString("data.user_id");

    Assert.assertNotNull(userId);
  }
}
