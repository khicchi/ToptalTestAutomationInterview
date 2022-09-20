package net.kicchi.toptal.suite.api;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.kicchi.toptal.modals.Pet;
import net.kicchi.toptal.modals.PetStoreServerMessage;
import net.kicchi.toptal.modals.User;
import net.kicchi.toptal.utils.ConfigurationReaderUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UserTests {
  RequestSpecification requestSpecification = given().header(new Header("api_key", "special-key"));
  Response response;
  private String baseApiUrl = ConfigurationReaderUtil.getConfiguration().getApiBaseURL() + "/user";

  @Test
  public void getUserByUserName(){
    String userName = "kicchi";
    response = requestSpecification.pathParam("username", userName).get(baseApiUrl+ "/{username}");
    Assert.assertEquals(response.as(User.class).getUsername(), userName);
  }

  @Test
  public void createUserTest(){
    response = requestSpecification.contentType(ContentType.JSON).body(User.getSampleUser()).post(baseApiUrl);
    Assert.assertEquals(response.then().extract().jsonPath().getInt("code"), 200);
  }

  @Test
  public void userLoginTest(){
    response = requestSpecification
        .queryParam("username", User.getSampleUser().getUsername())
        .queryParam("password", User.getSampleUser().getPassword())
        .get(baseApiUrl + "/login");

    Assert.assertEquals(response.getStatusCode(), 200);
    System.out.println(response.as(PetStoreServerMessage.class).extractSessionId());
  }

  @Test
  public void userLogOutTest(){
    response = requestSpecification.get(baseApiUrl + "/logout");

    Assert.assertEquals(response.getStatusCode(), 200);
  }

  @Test
  public void createUserWithArray(){
    response = requestSpecification.contentType(ContentType.JSON).accept(ContentType.JSON).body(User.getSampleUserArray()).log().body().post(baseApiUrl + "/createWithArray");

    Assert.assertEquals(response.statusCode(), 200);
  }

  @Test
  public void createUserWithList(){
    response = requestSpecification.contentType(ContentType.JSON).body(User.getSampleUserList()).log().body().post(baseApiUrl + "/createWithList");

    Assert.assertEquals(response.statusCode(), 200);
  }

  @Test
  public void updateUser(){
    User user = User.getSampleUser();
    user.setPhone("5555555");
    response = given().spec(requestSpecification).contentType(ContentType.JSON).pathParam("username", user.getUsername()).body(user).put(baseApiUrl + "/{username}");

    Assert.assertEquals(response.statusCode(), 200);
  }

  @Test
  public void deleteUser(){
    response = given().spec(requestSpecification).pathParam("username", User.getSampleUser2().getUsername()).delete(baseApiUrl + "/{username}");
    Assert.assertEquals(response.statusCode(), 200);
  }
}
