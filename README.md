# KatalonStudio_verify_if_a_brower_is_opened_by_a_windows_application

## Problem to solve

There was a post in the Katalo Forum:

- https://forum.katalon.com/t/verify-browser-is-opened-by-windows-application/40596 by mdu_kay7

Let me quote the full post:

>I’m testing a windows application (WPF). The app has a button that opens a web browser and navigates to a specific Url. I’m having a hard time verifying that the web browser is opened and the has navigated to the correct url. Katalon doesn’t seem to recognize the web browser. I’ve tried a few options:
>
>1. com.kms.katalon.core.configuration.RunConfiguration.storeDriver
>2. driver.switchTo().window(winHandle)
>3. WebDriver driver = DriverFactory.getWebDriver()
>
>None of the option seem to work. Since there is no webdriver created. Is there a way to assign a webdriver to the web browser opened by the windows application?

## Solution

I made a search and found the following StackOverflow thread:

- https://stackoverflow.com/questions/8344776/can-selenium-interact-with-an-existing-browser-session

Especially the post by Ashmed Ashour at Nov 23, 2021 looked promissing to me.

I tried to interprete his idea into a set of code in Katalon Studio, and got succeded.

## Description

### Environment

- macOS Sonoma 14.7.2
- Katalon Studio Free v9.0.1

### Launching Chrome with --remote-debugging-port opened

I made a bash shell script `<projectDir>/open_chrome_with_remotedebuggingport`

```
open -a "Google Chrome.app" --args --remote-debugging-port=9222 --user-data-dir=$HOME/tmp/temporary-chrome-profile
```

I did

```
$ chmod +x open_chrome_with_remotedebuggingport
```

When I run this shell script, I could see Chrome browser opened. It should have the debugger listening to the port #9222.

# Test Case script that connects to the Chrome externally opened

I made `Test Cases/interactWithAnExistingzBrowserSession`

```
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
```

This script will launch a WebDriver which tries to connect to the Chrome at the IP address 127.0.0.1 with port #9222.
The test case ran OK. The test case sucessfully verified that the Chrome is displaying YouTube.


### Conclusion: What mdu_kay7 needs to do?

mdu_kay7 has to change his windows app. The app have to start Chrome with `--remote-debugging-port=xxxx` as my shell script does. Without the port is opened by Chrome process, external processes like Katalon's test case script will never be able to communiate with the Chrome process.


