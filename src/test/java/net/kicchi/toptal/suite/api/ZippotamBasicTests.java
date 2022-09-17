package net.kicchi.toptal.suite.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.kicchi.toptal.modals.Location;
import net.kicchi.toptal.utils.ConfigurationReaderUtil;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ZippotamBasicTests {
  RequestSpecification basRequestSpec = given().baseUri(ConfigurationReaderUtil.getConfiguration()
      .getApiBaseURL()).and();
  Response response;

  @Test
  public void getUSZipCodeTestWithJson(){
    //response = basRequestSpec.get("https://api.zippopotam.us/us/90210");
     response = given().pathParam("countryCode", "US").pathParam("postCode", "90210").when().get("http://api.zippopotam.us/{countryCode}/{postCode}");

    Location location = response.as(Location.class);
    Assert.assertEquals(location.getCountry(), "us");

  }

  @Test
  public void getUSZipCodeTestWithJsonPath(){
    given().pathParam("countryCode", "US").pathParam("postCode", "90210").when()
        .get("http://api.zippopotam.us/{countryCode}/{postCode}").then().assertThat().body("places[0].'place name'", equalTo("Beverly Hills"));
  }

  @Test
  public void getUSZipCodeTestWithJson2(){
    //response = basRequestSpec.get("https://api.zippopotam.us/us/90210");
    response = given().pathParam("country", "us").pathParam("state", "ma").pathParam("city", "belmont").when().get("http://api.zippopotam.us/{country}/{state}/{city}");

    Location location = response.as(Location.class);
    Assert.assertEquals(location.getPlaces().size(), 2);

  }

}
