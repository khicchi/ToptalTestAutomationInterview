package net.kicchi.toptal.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.net.URL;
import lombok.NoArgsConstructor;
import net.kicchi.toptal.enums.BrowserType;
import net.kicchi.toptal.exception.AutomationException;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;


/**
 * Capable of parallel browser execution by using the driverPool
 */
@NoArgsConstructor
public class DriverUtil {

    private static InheritableThreadLocal<WebDriver> driverPool = new InheritableThreadLocal<>();

    @NonNull
    public static WebDriver getDriver() {
        try {
            if (driverPool.get() == null) {
                BrowserType browserType = null;
                if (System.getProperty("browser") != null)
                    browserType = BrowserType.getByName(System.getProperty("browser"));
                else
                    browserType = ConfigurationReaderUtil.getConfiguration().getBrowserType();

                switch (browserType) {
                    case CHROME:
                        WebDriverManager.chromedriver().setup();
                        driverPool.set(new ChromeDriver());
                        break;
                    case CHROME_HEADLESS:
                        WebDriverManager.chromedriver().setup();
                        driverPool.set(new ChromeDriver(new ChromeOptions().setHeadless(true)));
                        break;
                    case FIREFOX:
                        WebDriverManager.firefoxdriver().setup();
                        driverPool.set(new FirefoxDriver());
                        break;
                    case FIREFOX_HEADLESS:
                        WebDriverManager.firefoxdriver().setup();
                        driverPool.set(new FirefoxDriver(new FirefoxOptions().setHeadless(true)));
                        break;
                    case INTERNET_EXPLORER:
                        if (!System.getProperty("os.name").toLowerCase().contains("windows"))
                            throw new AutomationException("Your OS doesn't support Internet Explorer");
                        WebDriverManager.iedriver().setup();
                        driverPool.set(new InternetExplorerDriver());
                        break;
                    case EDGE:
                        if (!System.getProperty("os.name").toLowerCase().contains("windows"))
                            throw new AutomationException("Your OS doesn't support Edge");
                        WebDriverManager.edgedriver().setup();
                        driverPool.set(new EdgeDriver());
                        break;
                    case SAFARI:
                        if (!System.getProperty("os.name").toLowerCase().contains("mac"))
                            throw new AutomationException("Your OS doesn't support Safari");
                        WebDriverManager.getInstance(SafariDriver.class).setup();
                        driverPool.set(new SafariDriver());
                        break;
                    case CHROME_REMOTE:
                        ChromeOptions chromeOptions = new ChromeOptions();
                        chromeOptions.setCapability("platform", Platform.ANY);
                        driverPool.set(new RemoteWebDriver(new URL(ConfigurationReaderUtil.getConfiguration().getRemoteGridUrl()), chromeOptions));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return driverPool.get();
    }

    public static void closeDriver() {
        driverPool.get().quit();
        driverPool.remove();
    }
}