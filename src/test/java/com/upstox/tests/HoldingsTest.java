package com.upstox.tests;

import com.upstox.tests.client.UpstoxApiClient;
import com.upstox.tests.config.RequestSpecFactory;
import com.upstox.tests.config.TestRunStore;
import io.qameta.allure.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;

@Epic("Upstox API")
@Feature("Portfolio")
public class HoldingsTest {

  @Test(groups = {"holdings"})
  @Story("Fetch holdings")
  @Severity(SeverityLevel.CRITICAL)
  @Description("Fetches holdings and validates basic structure + sanity checks.")
  public void shouldFetchHoldings() {
    UpstoxApiClient api = new UpstoxApiClient(RequestSpecFactory.authorized());

    Response res = api.getHoldings();

    res.then()
        .statusCode(200)
        .body("status", anyOf(equalTo("success"), equalTo("SUCCESS")))
        .body("data", notNullValue());

    // store response into TestNG context via system property (simple approach for now)
    String json = res.asString();
    TestRunStore.put("HOLDINGS_JSON", json);

    // quick check: data array exists (even if empty)
    JsonPath jp = res.jsonPath();
    List<Map<String, Object>> rows = jp.getList("data");
    Assert.assertNotNull(rows, "Holdings data list should not be null");
  }

  @DataProvider(name = "holdingsRows")
  public Object[][] holdingsRows() {
    String json = TestRunStore.get("HOLDINGS_JSON", String.class);
    Assert.assertNotNull(json, "HOLDINGS_JSON not set. Run shouldFetchHoldings first.");

    JsonPath jp = new JsonPath(json);
    List<Map<String, Object>> rows = jp.getList("data");

    // If portfolio is empty, return 0 rows (test will effectively skip row validation)
    if (rows == null || rows.isEmpty()) {
      return new Object[0][0];
    }

    Object[][] data = new Object[rows.size()][1];
    for (int i = 0; i < rows.size(); i++) {
      data[i][0] = rows.get(i);
    }
    return data;
  }

  @Test(groups = {"holdings"}, dataProvider = "holdingsRows", dependsOnMethods = "shouldFetchHoldings")
  @Story("Validate each holding row")
  @Severity(SeverityLevel.NORMAL)
  @Description("Validates each holding row for required fields and numeric sanity checks.")
  public void eachHoldingShouldHaveValidCoreFields(Map<String, Object> row) {
    System.out.println(
      "Thread: " + Thread.currentThread().getName()
    );

    // These keys may vary; we assert "at least something meaningful exists"
    Assert.assertTrue(row.containsKey("instrument_token") || row.containsKey("instrumentToken"),
        "Holding should contain instrument token");

    // Common sanity checks (only if fields exist)
    assertNonNegativeIfPresent(row, "quantity");
    assertNonNegativeIfPresent(row, "avg_price");
    assertNonNegativeIfPresent(row, "last_price");
    assertNonNegativeIfPresent(row, "pnl");
  }

  private void assertNonNegativeIfPresent(Map<String, Object> row, String key) {
    if (!row.containsKey(key) || row.get(key) == null) return;
    double val = Double.parseDouble(String.valueOf(row.get(key)));
    Assert.assertTrue(val >= 0 || key.equals("pnl"), key + " should be non-negative (except pnl can be negative)");
  }
}
