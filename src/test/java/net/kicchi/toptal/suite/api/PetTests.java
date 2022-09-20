package net.kicchi.toptal.suite.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.File;
import net.kicchi.toptal.modals.Pet;
import net.kicchi.toptal.modals.PetStoreServerMessage;
import net.kicchi.toptal.utils.ConfigurationReaderUtil;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PetTests {
  String baseApiUrl = ConfigurationReaderUtil.getConfiguration().getApiBaseURL();
  Response response;
  RequestSpecification requestSpecification = given().header(new Header("api_key", "special-key"));
  PetStoreServerMessage serverError;

  @Test
  public void getPetByIdDesrialization(){
    response = given().header(new Header("api_key", "special-key")).when().get(baseApiUrl + "/pet/" + 1);

    System.out.println("Status code: " + response.statusCode());
    if (response.statusCode() != 200){
      throw new RuntimeException(response.as(PetStoreServerMessage.class).toString());
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

    Assert.assertTrue(response.then().extract().jsonPath().getList(".", Pet.class).stream().allMatch(pet -> pet.getStatus().equalsIgnoreCase(availability)));
  }

  @Test
  public void createPetTest(){
    response = given().spec(requestSpecification).body(Pet.getSamplePet()).contentType(ContentType.JSON).post(baseApiUrl + "/pet");
    Assert.assertEquals(response.getStatusCode(), 200);
    System.out.println(response.prettyPrint());
  }

  @Test
  public void deletePetTest(){
    response = given().spec(requestSpecification).delete(baseApiUrl + "/pet/" + 102);
    if (response.statusCode() != 400 && response.statusCode() != 404){
      Assert.assertEquals((response.as(PetStoreServerMessage.class)).getCode(), 200);
    }
  }

  @Test
  public void updatePetWithFormData(){
    response = given().spec(requestSpecification)
        .formParam("name", "saka")
        .formParam("status", "available")
        .pathParam("petId", 103)
        .post(baseApiUrl + "/pet/{petId}");

    Assert.assertEquals(response.getStatusCode(), 200);
  }

  @Test
  public void updatePetWithPut(){
    Pet pet = Pet.getSamplePet();
    pet.setId(103);
    given().spec(requestSpecification)
        .contentType(ContentType.JSON)
        .body(pet).put(baseApiUrl + "/pet").then().assertThat().statusCode(equalTo(200));
  }

  @Test
  public void uploadImageTest(){
    response = given().spec(requestSpecification)
        .contentType(ContentType.MULTIPART)
        .pathParam("petId", 103)
        .formParam("additionalMetadata", "kicchi")
        .multiPart("file", new File("/Users/huseyink/Desktop/1.png"))
        .post(baseApiUrl + "/pet/{petId}/uploadImage");

    Assert.assertEquals(response.then().extract().jsonPath().getString("code"), "200");
  }


}
