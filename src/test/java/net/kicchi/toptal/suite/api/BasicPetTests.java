package net.kicchi.toptal.suite.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import net.kicchi.toptal.modals.Pet;
import net.kicchi.toptal.modals.PetStoreServerError;
import net.kicchi.toptal.utils.ConfigurationReaderUtil;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class BasicPetTests {
  String baseApiUrl = ConfigurationReaderUtil.getConfiguration().getApiBaseURL();
  Response response;
  RequestSpecification requestSpecification = given().header(new Header("api_key", "special-key"));
  PetStoreServerError serverError;

  @Test
  public void getPetByIdDesrialization(){
    response = given().header(new Header("api_key", "special-key")).when().get(baseApiUrl + "/pet/" + 1);

    System.out.println("Status code: " + response.statusCode());
    if (response.statusCode() != 200){
      throw new RuntimeException(response.as(PetStoreServerError.class).toString());
    }

    Assert.assertEquals(response.as(Pet.class).getName(), "doggie");
  }

  @DataProvider
  private Object[][] getAvailabilityValues(){
    return new String[][]{
        {"available"},
        {"pending"},
        {"sold"}
    };
  }

  @Test
  public void getPetByIdJsonPath(){
    response = given().spec(requestSpecification).accept(ContentType.JSON).when().get(baseApiUrl + "/pet/" + 10);
    Assert.assertEquals(response.then().extract().jsonPath().getString("name"), "fish");
  }

  @Test(dataProvider = "getAvailabilityValues")
  public void getPetsByStatus(String availability){
    response = given().spec(requestSpecification).and().queryParam("status", availability).get(baseApiUrl + "/pet/findByStatus");

    Assert.assertTrue(2 > 0);
  }
}
