import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

System.setProperty("webdriver.chrome.driver", DriverFactory.getChromeDriverPath());
ChromeOptions options = new ChromeOptions()
options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222")
WebDriver driver = new ChromeDriver(options)
DriverFactory.changeWebDriver(driver)

WebUI.openBrowser("https://www.youtube.com/")
String title = WebUI.getWindowTitle()
assert title.contains("YouTube")

WebUI.closeBrowser()