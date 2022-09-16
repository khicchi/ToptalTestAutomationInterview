package net.kicchi.toptal.suite.api;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.List;
import java.util.Objects;
import net.kicchi.toptal.models.ToDo;
import net.kicchi.toptal.utils.ConfigurationReaderUtil;
import org.checkerframework.checker.units.qual.C;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BaseApiTest {
  protected final String baseAPIUrl = Objects.requireNonNull(ConfigurationReaderUtil.getConfiguration())
      .getApiBaseURL();
  private Response response;
  protected List<ToDo> toDoList;

  @Test
  public void getAllTodosTest(){
    /*
      When I send a get request
      Then I should be able to retrieve all todos
      And both completed and not completed todos must be retrieved
     */

    response = when().get(baseAPIUrl);
    Assert.assertEquals(response.getStatusCode(), 200);

    toDoList = response.then().extract().body().jsonPath().getList(".", ToDo.class);
    Assert.assertNotNull(toDoList, "returned todo list is null");
    Assert.assertTrue(toDoList.size() > 0, "returned todo list size is 0");
  }

  @Test
  public void gettingAToDoById(){
    /*
      When I send a get request with todo id 100
      Then I should be able to retrieve todo with id 100
     */
    int todoId = 100;
    response = given().queryParam("id", 100).when().get(baseAPIUrl);

    Assert.assertEquals(response.getStatusCode(), 200);
    Assert.assertEquals(response.contentType(), "application/json; charset=utf-8");

    ToDo todo = response.then().extract().body().jsonPath().getList(".", ToDo.class).get(0);
    Assert.assertEquals(todo.getId(), 100);
  }

  @Test
  public void gettingAToDoByUserId(){
    /*
      When I send a get request with user id 2
      Then I should be able to retrieve todo with user id 2
     */

    response = given().queryParam("userId", 2).when().get(baseAPIUrl);
    Assert.assertEquals(response.then().extract().jsonPath().getList(".", ToDo.class).get(0).getUserId(), 2);
  }

  @Test
  public void gettingTodosByStatus(){
    /*
      When I send a get request with user completed status "<status>"
      Then I should be able to retrieve todos with status "<status>"
      Examples:
      |status|
      |      true|
      |      false|
     */
    Assert.assertTrue(((List<ToDo>)given().queryParam("completed", false).when().get(baseAPIUrl).then().extract().jsonPath().getList(".", ToDo.class)).stream().allMatch(toDo -> !toDo.isCompleted()));
  }

  @Test
  public void gettingTodoByTitle(){
    /*
      When I send a get request with user completed title "<title>"
      Then I should be able to retrieve todos with title "<title>"
      Examples:
        |title|
        |accusamus eos facilis sint et aut voluptatem|
        |delectus aut autem|
     */
    String title = "accusamus eos facilis sint et aut voluptatem";
    Assert.assertEquals(given().queryParam("title", title).when().get(baseAPIUrl).then().extract().jsonPath().getList(".", ToDo.class).get(0).getTitle(), title);

    title = "delectus aut autem";
    Assert.assertEquals(given().queryParam("title", title).when().get(baseAPIUrl).then().extract().jsonPath().getList(".", ToDo.class).get(0).getTitle(), title);
  }

  @Test
  public void saveToDoTest(){
    /*
      Given a new todo with user id 1, title "demo meeting presentation"
      When todo is sent to api with post request
      Then the new todo should be created and returned with todo id
     */
    ToDo toDo = ToDo.builder().id(1).title("demo meeting presentation").build();
    response = given().body(toDo).and().baseUri(baseAPIUrl).and().contentType(ContentType.JSON).and().accept(ContentType.JSON).post();
    Assert.assertEquals(response.getStatusCode(), 201);
    Assert.assertTrue(response.as(ToDo.class).getId() > 0);
  }

  @Test
  public void updateToDoTest(){
    /*
      Given a todo with id 20 is fetched from api
      And todo's title is updated with "check jira sprint board"
      When updated todo is sent to api with put request
      Then the updated todo should be retrieved
     */
    ToDo toDo = given().baseUri(baseAPIUrl).queryParam("id", 20).accept(ContentType.JSON).get().then().extract().jsonPath().getList(".", ToDo.class).get(0);
    toDo.setTitle("check jira sprint board");
    response = given().contentType(ContentType.JSON).and().accept(ContentType.JSON).and().and().body(toDo).put(baseAPIUrl + "/" + 20);
    Assert.assertEquals(response.getStatusCode(), 200);
    Assert.assertEquals(response.as(ToDo.class).getTitle(), toDo.getTitle());
  }

  @Test
  public void updateToDoPartly(){
    /*
      When a todo with "<id>" updated "<field>" with "<newValue>" is sent to api with patch request
      Then the updated todo should be retrieved with "<field>" as "<newValue>"
      Examples:
        |id|field|newValue|
        | 21 |userId     |4        |
        | 31 |title     | check the bug one more time       |
        | 41 |completed     |  true      |
        | 51 |completed     |  false      |
     */
    ToDo toDo = given().baseUri(baseAPIUrl).queryParam("id", 21).accept(ContentType.JSON).get().then().extract().jsonPath().getList(".", ToDo.class).get(0);
    toDo.setUserId(4);
    response = given().contentType(ContentType.JSON).and().accept(ContentType.JSON).and().body(toDo).patch(baseAPIUrl + "/" + toDo.getId());
    Assert.assertEquals(response.getStatusCode(), 200);
    Assert.assertEquals(toDo.getUserId(), response.as(ToDo.class).getUserId());

    toDo = given().baseUri(baseAPIUrl).queryParam("id", 31).accept(ContentType.JSON).get().then().extract().jsonPath().getList(".", ToDo.class).get(0);
    String title = "check the bug one more time";
    toDo.setTitle(title);
    response = given().contentType(ContentType.JSON).and().accept(ContentType.JSON).and().body(toDo).patch(baseAPIUrl + "/" + toDo.getId());
    Assert.assertEquals(response.getStatusCode(), 200);
    Assert.assertEquals(response.as(ToDo.class).getTitle(), toDo.getTitle());

    toDo = given().baseUri(baseAPIUrl).queryParam("id", 41).accept(ContentType.JSON).get().then().extract().jsonPath().getList(".", ToDo.class).get(0);
    boolean completed = true;
    toDo.setCompleted(completed);
    response = given().contentType(ContentType.JSON).and().accept(ContentType.JSON).and().body(toDo).patch(baseAPIUrl + "/" + toDo.getId());
    Assert.assertEquals(response.getStatusCode(), 200);
    Assert.assertEquals(response.as(ToDo.class).isCompleted(), toDo.isCompleted());

    toDo = given().baseUri(baseAPIUrl).queryParam("id", 51).accept(ContentType.JSON).get().then().extract().jsonPath().getList(".", ToDo.class).get(0);
    completed = false;
    toDo.setCompleted(completed);
    response = given().contentType(ContentType.JSON).and().accept(ContentType.JSON).and().body(toDo).patch(baseAPIUrl + "/" + toDo.getId());
    Assert.assertEquals(response.getStatusCode(), 200);
    Assert.assertEquals(response.as(ToDo.class).isCompleted(), toDo.isCompleted());
  }

  @Test
  public void deleteToDoTest(){
    /*
      When one send a del request to api for the todo with id 75
      Then the response should be empty
      And the response status must be successful
     */
    response = when().delete(baseAPIUrl + "/" + 75);
    Assert.assertFalse(response.toString().contains("id"));
    Assert.assertEquals(response.getStatusCode(), 200);
  }
}
