package net.kicchi.toptal.suite.api;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import net.kicchi.toptal.modals.Order;
import net.kicchi.toptal.utils.ConfigurationReaderUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

public class StoreTests {
  RequestSpecification requestSpecification = given().header(new Header("api_key", "special-key"));
  Response response;
  String baseApiUrl = ConfigurationReaderUtil.getConfiguration().getApiBaseURL() + "/store";

  @Test
  public void getInventoryTest(){
    response = given().spec(requestSpecification).get(baseApiUrl + "/inventory");
    HashMap<String, Integer> inventories = response.as(HashMap.class);
    Assert.assertFalse(inventories.isEmpty());
  }

  @Test
  public void createOrderTest(){
    response = given().spec(requestSpecification).contentType(ContentType.JSON).body(Order.getSampleOrder()).post(baseApiUrl + "/order");
    System.out.println(response.prettyPrint());
    Assert.assertEquals(response.statusCode(), 200);
  }

  @Test
  public void findPurchaseOrderById(){
    long orderId = 1767623249526899811L;
    response = given().spec(requestSpecification).pathParam("orderId", orderId).get(baseApiUrl + "/order/{orderId}");
    System.out.println(response.prettyPrint());
    Assert.assertEquals(response.as(Order.class).getId(), orderId);
  }

  @Test
  public void deletePurchaseOrderByIdTest(){
    long orderId = 1767623249526899811L;
    response = given().spec(requestSpecification).pathParam("orderId", orderId).delete(baseApiUrl + "/order/{orderId}");
    Assert.assertEquals(response.statusCode(), 200);
  }
}
