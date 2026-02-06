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
public class PositionsTest {

  @Test(groups = {"positions"})
  @Story("Fetch positions")
  @Severity(SeverityLevel.CRITICAL)
  @Description("Fetches positions and validates basic structure + sanity checks.")
  public void shouldFetchPositions() {
    UpstoxApiClient api = new UpstoxApiClient(RequestSpecFactory.authorized());

    Response res = api.getPositions();

    res.then()
        .statusCode(200)
        .body("status", anyOf(equalTo("success"), equalTo("SUCCESS")))
        .body("data", notNullValue());

    String json = res.asString();
    TestRunStore.put("POSITIONS_JSON", json);

    JsonPath jp = res.jsonPath();
    List<Map<String, Object>> rows = jp.getList("data");
    Assert.assertNotNull(rows, "Positions data list should not be null");
  }

  @DataProvider(name = "positionsRows")
  public Object[][] positionsRows() {
    String json = TestRunStore.get("POSITIONS_JSON", String.class);
    Assert.assertNotNull(json, "POSITIONS_JSON not set. Run shouldFetchPositions first.");

    JsonPath jp = new JsonPath(json);
    List<Map<String, Object>> rows = jp.getList("data");

    if (rows == null || rows.isEmpty()) {
      return new Object[0][0];
    }

    Object[][] data = new Object[rows.size()][1];
    for (int i = 0; i < rows.size(); i++) {
      data[i][0] = rows.get(i);
    }
    return data;
  }

  @Test(groups = {"positions"}, dataProvider = "positionsRows", dependsOnMethods = "shouldFetchPositions")
  @Story("Validate each position row")
  @Severity(SeverityLevel.NORMAL)
  @Description("Validates each position row for required fields and numeric sanity checks.")
  public void eachPositionShouldHaveValidCoreFields(Map<String, Object> row) {

    Assert.assertTrue(row.containsKey("instrument_token") || row.containsKey("instrumentToken"),
        "Position should contain instrument token");

    // Positions commonly have qty and pnl fields; allow pnl negative
    assertNumberIfPresent(row, "quantity");
    assertNumberIfPresent(row, "buy_quantity");
    assertNumberIfPresent(row, "sell_quantity");
    assertNumberIfPresent(row, "pnl");
  }

  private void assertNumberIfPresent(Map<String, Object> row, String key) {
    if (!row.containsKey(key) || row.get(key) == null) return;
    // just ensure parsable number (no NaN strings etc.)
    Double.parseDouble(String.valueOf(row.get(key)));
  }
}
