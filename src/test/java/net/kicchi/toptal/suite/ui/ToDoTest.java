package net.kicchi.toptal.suite.ui;

import java.time.Duration;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.kicchi.toptal.pages.ToDoPage;
import net.kicchi.toptal.utils.BrowserUtil;
import net.kicchi.toptal.utils.ConfigurationReaderUtil;
import net.kicchi.toptal.utils.DriverUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Log4j2
public class ToDoTest extends BaseWebTest{
  private WebDriver driver;
  private ToDoPage toDoPage;
  private String newAddedToDoName = "homework must be done";
  private List<WebElement> todoList;

  @Override
  @BeforeMethod
  protected void setupBeforeMethod(){
    super.setupBeforeMethod();
    driver = DriverUtil.getDriver();
    driver.get(ConfigurationReaderUtil.getConfiguration().getMainPageUrl());
    waitUntilTodoInputVisible();
    toDoPage = new ToDoPage();
  }

  @Test
  public void newToDoAdditionTest(){
    /*
    Given user is on the main todos page
    When user clicks on the new todo text box
    And user writes "homework must be done" as a new todo
    And user presses enter key
    Then user should see "homework must be done" in todo list
   */

    log.info("When user clicks on the new todo text box");
    toDoPage.getInputToDo().click();

    log.info("And user writes \"homework must be done\" as a new todo");
    toDoPage.getInputToDo().sendKeys(newAddedToDoName);

    log.info("And user presses enter key");
    toDoPage.getInputToDo().sendKeys(Keys.ENTER);

    log.info("Then user should see \"homework must be done\" in todo list");
    todoList = driver.findElements(By.cssSelector("ul.todo-list li label"));
    Assert.assertTrue(todoList.stream().anyMatch(l -> l.getText().trim().equalsIgnoreCase(newAddedToDoName)), "New added todo not found");
  }

  private void waitUntilTodoInputVisible(){
    WebDriverWait wait = new WebDriverWait(DriverUtil.getDriver(), Duration.ofSeconds(10));
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".new-todo")));
  }

  @Test
  public void deleteToDoTest(){
    /*
    Given user is on the main todos page
    When user clicks on the new todo text box
    And user writes "homework must be done" as a new todo
    And user presses enter key
    And user clicks on the cross icon at the right-end of the "homework must be done"
    Then user should not see the "homework must be done"
   */
    //Given user is on the main todos page

    log.info("When user clicks on the new todo text box");
    toDoPage.getInputToDo().click();

    log.info("And user writes \"homework must be done\" as a new todo");
    toDoPage.getInputToDo().sendKeys(newAddedToDoName);

    log.info("And user presses enter key");
    toDoPage.getInputToDo().sendKeys(Keys.ENTER);

    todoList = driver.findElements(By.cssSelector("ul.todo-list li label"));
    deleteTodo(newAddedToDoName);

    todoList = driver.findElements(By.cssSelector("ul.todo-list li label"));
    Assert.assertTrue(todoList.stream().noneMatch(l -> l.getText().trim().equalsIgnoreCase(newAddedToDoName)), "New added todo not found");
  }

  private void deleteTodo(String todoToDelete){
    todoList = driver.findElements(By.cssSelector("ul.todo-list li label"));
    int index = 0;
    for (int i = 0; i < todoList.size(); i++) {
      if (todoList.get(i).getText().trim().equalsIgnoreCase(todoToDelete)){
        index = i;
        break;
      }
    }

    new Actions(driver).moveToElement(todoList.get(index)).build().perform();
    WebElement crossButton = driver.findElement(By.xpath(String.format("//ul[@class='todo-list']//li[%s]//button[@class='destroy']", index + 1)));
    new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format("//ul[@class='todo-list']//li[%s]//button[@class='destroy']", index + 1))));
    crossButton.click();
  }

  @DataProvider
  private Object[][] getMultipleTodos(){
    return new String[][]{
        new String[]{"maths exam reminder", "go to dentist", "take the pills"}
    };
  }

  @Test(dataProvider = "getMultipleTodos")
  public void createMultipleTodosTest(String[] todos){
    /*
        Given user is on the main todos page
        When user creates following todos
          | maths exam reminder |
          | go to dentist       |
          | take the pills      |
        Then user should see all created todos on the todo list
     */

    for (String todo : todos) {
      createTodo(todo);
    }

    todoList = driver.findElements(By.cssSelector("ul.todo-list li label"));

    for (String todo : todos) {
      Assert.assertTrue(todoList.stream().anyMatch(webElement -> webElement.getText().trim().equalsIgnoreCase(todo)));
    }
  }

  private void createTodo(String todoName){
    toDoPage.getInputToDo().sendKeys(todoName + "\n");
  }

  @DataProvider
  private Object[][] getTodosToCreateAndDelete(){
    return new String[][]{
        new String[]{"maths exam reminder", "go to dentist", "take the pills"},
        new String[]{"as"}
    };
  }

  @Test(dataProvider = "getMultipleTodos")
  public void deleteMultipleTodosTest(String[] todos){
    /*
    Given user is on the main todos page
     When user creates following todos
      | maths exam reminder |
      | go to dentist       |
      | take the pills      |
    And user deletes following todos
      | maths exam reminder |
      | go to dentist       |
    Then user should not see the deleted todos
     */
    for (String todo : todos) {
      createTodo(todo);
    }

    for (int i = 0; i <= 1; i++) {
      deleteTodo(todos[i]);
    }

    todoList = driver.findElements(By.cssSelector("ul.todo-list li label"));
    for (int i = 0; i <= 1 ; i++) {
      int finalI = i;
      Assert.assertTrue(todoList.stream().noneMatch(webElement -> webElement.getText().trim().equalsIgnoreCase(todos[finalI])));
    }
  }

  @Test
  public void editTodoTest(){
    /*
    Given user is on the main todos page
    When user clicks on the new todo text box
    And user writes "<new_title>" as a new todo
    * user presses enter key
    * user double clicks over the "<new_title>" todo
    And user changes "<new_title>" title to "<updated_title>"
    And user presses enter key on edit box
    Then user should see "<updated_title>" in todo list
    Examples:
      | new_title             | updated_title         |
      | homework must be done | arrange video meeting |
     */
    String currentName = "homework must be done";
    String updatedName = "arrange video meeting";

    createTodo(currentName);
    editTodo(currentName, updatedName);
    todoList = driver.findElements(By.cssSelector("ul.todo-list li label"));
    Assert.assertTrue(todoList.stream().anyMatch(webElement -> webElement.getText().trim().equalsIgnoreCase(updatedName)));
  }

  private void editTodo(String currentName, String updatedName){
    todoList = driver.findElements(By.cssSelector("ul.todo-list li label"));

    for (int i = 0; i < todoList.size(); i++) {
      if (todoList.get(i).getText().trim().equalsIgnoreCase(currentName)){
        new Actions(driver).doubleClick(driver.findElement(By.xpath(String.format("//ul[@class='todo-list']//li[%s]//label", i + 1)))).perform();
        WebElement element = driver.findElement(By.xpath(String.format("//ul[@class='todo-list']//li[%s]//input[@class='edit']", i + 1)));
        for (int j = 0; j < currentName.length(); j++) {
          element.sendKeys(Keys.BACK_SPACE);
        }
        element.sendKeys(updatedName + "\n");
        break;
      }
    }
  }

  @Test
  public void editMultipleTodos(){
    /*
      Given user is on the main todos page
      When user creates following todos
        | maths exam reminder |
        | go to dentist       |
        | take the pills      |
      And user edits following todos
        | maths exam reminder | basketball match at 20 PM |
        | go to dentist | go to hospital |
      Then user should see following todos in todo list
        | basketball match at 20 PM |
        | go to hospital            |
        | take the pills            |
     */

    String[] todosToCreate = new String[]{"maths exam reminder", "go to dentist", "take the pills"};
    String[] todosToUpdate = new String[]{"basketball match at 20 PM", "go to hospital"};

    for (String create : todosToCreate) {
      createTodo(create);
    }

    for (int i = 0; i < todosToUpdate.length; i++) {
      editTodo(todosToCreate[i], todosToUpdate[i]);
    }

    todoList = driver.findElements(By.cssSelector("ul.todo-list li label"));

    for (int i = 0; i < todosToUpdate.length; i++) {
      int finalI = i;
      Assert.assertTrue(todoList.stream().anyMatch(webElement -> webElement.getText().trim().equalsIgnoreCase(todosToUpdate[finalI])));
    }
  }

  @Test
  public void noToDoInvisiblesTest(){
    /*
      Given user is on the main todos page
      Then user should not see left item count and filter panel
     */
    Assert.assertThrows(NoSuchElementException.class,() -> driver.findElement(By.cssSelector("span.todo-count")));
    Assert.assertThrows(NoSuchElementException.class,() -> driver.findElement(By.cssSelector("ul.filters")));
  }

  @Test
  public void leftItemCountOnItemCreationTest(){
    /*
      Given user is on the main todos page
      When user clicks on the new todo text box
      And user creates "buy a present to your son" as a new todo
      Then user should see 1 as left item count
      And user creates "do not forget car insurance" as a new todo
      Then user should see 2 as left item count
     */

    createTodo("buy a present to your son");
    createTodo("do not forget car insurance");

    Assert.assertEquals(2, toDoPage.getLeftToDoCount(), "Left todo count not matched");
  }

  @Test
  public void leftItemCountUpdateByDeletionTest(){
    /*
      Given user is on the main todos page
      When user clicks on the new todo text box
      And user creates "buy a present to your son" as a new todo
      And user creates "do not forget car insurance" as a new todo
      Then user should see 2 as left item count
      And user deletes following todos
        |buy a present to your son|
      Then user should see 1 as left item count
      And user deletes following todos
        |do not forget car insurance|
      Then user should not see left item count and filter panel
     */

    String todo1 = "buy a present to your son";
    String todo2 = "do not forget car insurance";

    createTodo(todo1);
    createTodo(todo2);
    Assert.assertEquals(2, toDoPage.getLeftToDoCount(), "Left todo count not matched");

    deleteTodo(todo1);
    Assert.assertEquals(1, toDoPage.getLeftToDoCount(), "Left todo count not matched");

    deleteTodo(todo2);
    Assert.assertThrows(NoSuchElementException.class,() -> driver.findElement(By.cssSelector("span.todo-count")));
    Assert.assertThrows(NoSuchElementException.class,() -> driver.findElement(By.cssSelector("ul.filters")));
  }

  @Test
  public void leftItemCountUpdateByTodoCompletion(){
    /*
      Given user is on the main todos page
      When user clicks on the new todo text box
      And user creates "<firstTodo>" as a new todo
      Then user should see 1 as left item count
      And user checks completed checkbox of the "<firstTodo>"
      Then user should see 0 as left item count
      And user creates "<secondTodo>" as a new todo
      And user creates "<thirdTodo>" as a new todo
      And user creates "<fourthTodo>" as a new todo
      Then user should see 3 as left item count
      And user checks completed checkbox of the "<thirdTodo>"
      Then user should see 2 as left item count
      And user checks completed checkbox of the "<secondTodo>"
      Then user should see 1 as left item count
      Examples:
      |firstTodo|secondTodo|thirdTodo|fourthTodo|
      |buy a present to your son|connect to recruiter|attend IT webinar|finish the task|
     */
    String todo1 = "buy a present to your son";
    String todo2 = "connect to recruiter";
    String todo3 = "IT webinar";
    String todo4 = "finish the task";

    createTodo(todo1);
    Assert.assertEquals(1, toDoPage.getLeftToDoCount(), "Left todo count not matched");

    completeToDo(todo1);
    Assert.assertEquals(0, toDoPage.getLeftToDoCount(), "Left todo count not matched");

    createTodo(todo2);
    createTodo(todo3);
    createTodo(todo4);
    Assert.assertEquals(3, toDoPage.getLeftToDoCount(), "Left todo count not matched");

    completeToDo(todo3);
    Assert.assertEquals(2, toDoPage.getLeftToDoCount(), "Left todo count not matched");

    completeToDo(todo4);
    Assert.assertEquals(1, toDoPage.getLeftToDoCount(), "Left todo count not matched");
  }

  private void completeToDo(String todoName){
    driver.findElement(By.xpath(String.format("//label[.='%s']/preceding-sibling::input", todoName))).click();
  }

  @Test
  public void completedTabTest(){
    /*
      Given user is on the main todos page
      And user creates "update regression suite" as a new todo
      When user checks completed checkbox of the "update regression suite"
      And user navigates to the "Completed" tab
      Then user should see the "update regression suite" in Completed tab
     */
    String todoName = "update regression suite";

    createTodo(todoName);
    completeToDo(todoName);

    navigateToTab(TabName.COMPLETED);

    todoList = driver.findElements(By.cssSelector("ul.todo-list li label"));
    Assert.assertTrue(todoList.stream().anyMatch(webElement -> webElement.getText().trim().equalsIgnoreCase(todoName)));
  }

  private void navigateToTab(TabName tabName){
    String locatorOfTab = String.format(".//a[.='%s']", tabName.getTabName());
    toDoPage.getFilterPanel().findElement(By.xpath(locatorOfTab)).click();
    new WebDriverWait(driver, Duration.ofSeconds(5)).until(webDriver -> toDoPage.getFilterPanel().findElement(By.xpath(locatorOfTab)).getAttribute("class").equals("selected"));
  }

  @Test
  public void completedTodoActiveTabTest(){
    /*
      Given user is on the main todos page
      And user creates "update regression suite" as a new todo
      When user checks completed checkbox of the "update regression suite"
      And user navigates to the "Active" tab
      Then user should not see the "update regression suite" in Active tab
      */
    String todoName = "update regression suite";

    createTodo(todoName);
    completeToDo(todoName);
    navigateToTab(TabName.ACTIVE);

    todoList = driver.findElements(By.cssSelector("ul.todo-list li label"));
    Assert.assertTrue(todoList.stream().noneMatch(webElement -> webElement.getText().trim().equalsIgnoreCase(todoName)));
  }

  @Test
  public void allTabShowsAllTodosTest(){
    /*
      And user creates "update regression suite" as a new todo
      And user creates "reschedule scrum grooming meeting" as a new todo
      And user checks completed checkbox of the "update regression suite"
      Then user should see the following todos in the All tab
      |update regression suite          |
      |reschedule scrum grooming meeting|
      And user navigates to the "Active" tab
      And user navigates to the "All" tab
      Then user should see the following todos in the All tab
        |update regression suite          |
        |reschedule scrum grooming meeting|
      And user navigates to the "Completed" tab
      And user navigates to the "All" tab
      Then user should see the following todos in the All tab
        |update regression suite          |
        |reschedule scrum grooming meeting|
      And user checks completed checkbox of the "reschedule scrum grooming meeting"
      Then user should see the following todos in the All tab
        |update regression suite          |
        |reschedule scrum grooming meeting|
     */
    String todo1 = "update regression suite";
    String todo2 = "reschedule scrum grooming meeting";

    createTodo(todo1);
    createTodo(todo2);
    completeToDo(todo1);

    navigateToTab(TabName.ALL);
    todoList = driver.findElements(By.cssSelector("ul.todo-list li label"));
    Assert.assertTrue(todoList.stream().anyMatch(webElement -> webElement.getText().trim().equalsIgnoreCase(todo1)));
    Assert.assertTrue(todoList.stream().anyMatch(webElement -> webElement.getText().trim().equalsIgnoreCase(todo2)));

    navigateToTab(TabName.ACTIVE);
    navigateToTab(TabName.ALL);
    todoList = driver.findElements(By.cssSelector("ul.todo-list li label"));
    Assert.assertTrue(todoList.stream().anyMatch(webElement -> webElement.getText().trim().equalsIgnoreCase(todo1)));
    Assert.assertTrue(todoList.stream().anyMatch(webElement -> webElement.getText().trim().equalsIgnoreCase(todo2)));

    navigateToTab(TabName.COMPLETED);
    navigateToTab(TabName.ALL);
    todoList = driver.findElements(By.cssSelector("ul.todo-list li label"));
    Assert.assertTrue(todoList.stream().anyMatch(webElement -> webElement.getText().trim().equalsIgnoreCase(todo1)));
    Assert.assertTrue(todoList.stream().anyMatch(webElement -> webElement.getText().trim().equalsIgnoreCase(todo2)));

    completeToDo(todo2);
    navigateToTab(TabName.ALL);
    todoList = driver.findElements(By.cssSelector("ul.todo-list li label"));
    Assert.assertTrue(todoList.stream().anyMatch(webElement -> webElement.getText().trim().equalsIgnoreCase(todo1)));
    Assert.assertTrue(todoList.stream().anyMatch(webElement -> webElement.getText().trim().equalsIgnoreCase(todo2)));
  }
}

@Getter
@AllArgsConstructor
enum TabName{
  ALL("All"), ACTIVE("Active"), COMPLETED("Completed");

  private String tabName;
}
