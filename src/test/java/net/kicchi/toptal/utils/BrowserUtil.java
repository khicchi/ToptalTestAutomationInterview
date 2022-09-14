package net.kicchi.toptal.utils;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * My precious browser utilities
 * growing day by day
 */
public class BrowserUtil {
    /**
     * return a list of string that contains the text (retrieved by getText method)
     * from the input list of elements
     * @param list of WebElements to fetch their texts
     * @return list of string
     */
    public static List<String> getElementsText(List<WebElement> list) {
        if (list == null)
            return null;

        return list.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    /**
     * waits for backgrounds processes on the browser to complete
     * @param timeOutInSeconds
     */
    public static void waitForPageToLoad(long timeOutInSeconds) {
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver)
                        .executeScript("return document.readyState").equals("complete");
            }
        };
        try {
            WebDriverWait wait = new WebDriverWait(DriverUtil.getDriver(), timeOutInSeconds);
            wait.until(expectation);
        } catch (Throwable error) {
            error.printStackTrace();
        }
    }

    /**
     * Waits for element to be not stale
     * @param element
     */
    public static void waitForStaleElement(WebElement element) {
        int y = 0;
        while (y <= 15) {
            if (y == 1)
                try {
                    element.isDisplayed();
                    break;
                } catch (StaleElementReferenceException st) {
                    y++;
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (WebDriverException we) {
                    y++;
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        }
    }


    /**
     * Clicks on an element using JavaScript
     * @param element
     */
    public static void clickWithJS(WebElement element) {
        ((JavascriptExecutor) DriverUtil.getDriver())
                .executeScript("arguments[0].scrollIntoView(true);", element);
        ((JavascriptExecutor) DriverUtil.getDriver())
                .executeScript("arguments[0].click();", element);
    }


    /**
     * Scrolls down to an element using JavaScript
     * @param element
     */
    public static void scrollToElement(WebElement element) {
        ((JavascriptExecutor) DriverUtil.getDriver())
                .executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Performs double click action on an element
     * @param element
     */
    public static void doubleClick(WebElement element) {
        new Actions(DriverUtil.getDriver()).doubleClick(element).build().perform();
    }

    /**
     * Changes the HTML attribute of a Web Element to the given value using JavaScript
     * @param element
     * @param attributeName
     * @param attributeValue
     */
    public static void setAttribute(WebElement element, String attributeName, String attributeValue) {
        ((JavascriptExecutor) DriverUtil.getDriver())
                .executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);",
                        element, attributeName, attributeValue);
    }

    /**
     * Highlighs an element by changing its background and border color
     * @param element
     */
    public static void highlight(WebElement element) {
        try {
            ((JavascriptExecutor) DriverUtil.getDriver())
                    .executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');",
                            element);
            Thread.sleep(1000);
            ((JavascriptExecutor) DriverUtil.getDriver()).
                    executeScript("arguments[0].removeAttribute('style', 'background: yellow; border: 2px solid red;');", element);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void turnOffImplicitWaits() {
        DriverUtil.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
    }

    public static void turnOnImplicitWaits() {
        DriverUtil.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
    }
}
