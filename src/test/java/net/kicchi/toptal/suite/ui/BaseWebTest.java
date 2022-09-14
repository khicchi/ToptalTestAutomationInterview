package net.kicchi.toptal.suite.ui;

import net.kicchi.toptal.utils.BrowserUtil;
import net.kicchi.toptal.utils.DriverUtil;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseWebTest {
  @BeforeMethod
  void setupBeforeMethod(){
    BrowserUtil.turnOnImplicitWaits();
    DriverUtil.getDriver().manage().window().maximize();
  }

  @AfterMethod
  void tearDownMethod() {
    DriverUtil.closeDriver();
  }
}
