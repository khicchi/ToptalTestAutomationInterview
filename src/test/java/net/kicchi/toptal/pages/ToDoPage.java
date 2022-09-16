package net.kicchi.toptal.pages;

import lombok.Getter;
import net.kicchi.toptal.utils.DriverUtil;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Getter
public class ToDoPage {
  public ToDoPage(){
    PageFactory.initElements(DriverUtil.getDriver(), this);
  }

  @FindBy(css = ".new-todo")
  private WebElement inputToDo;

  @FindBy(css = "span.todo-count")
  private WebElement leftTodoSpan;

  @FindBy(css = "ul.filters")
  private WebElement filterPanel;

  @FindBy(css = "span.todo-count strong")
  private WebElement leftToDoCountElement;

  public int getLeftToDoCount(){
    return Integer.parseInt(leftToDoCountElement.getText().trim());
  }
}
