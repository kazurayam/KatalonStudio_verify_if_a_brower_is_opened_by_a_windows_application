# KatalonStudio_verify_if_a_brower_is_opened_by_a_windows_application

## Problem to solve

There was a post in the Katalo Forum:

- https://forum.katalon.com/t/verify-browser-is-opened-by-windows-application/40596 by mdu_kay7

Let me quote the full post:

>I’m testing a windows application (WPF). The app has a button that opens a web browser and navigates to a specific Url. I’m having a hard time verifying that the web browser is opened and the has navigated to the correct url. Katalon doesn’t seem to recognize the web browser. I’ve tried a few options:
>1. com.kms.katalon.core.configuration.RunConfiguration.storeDriver
>2. driver.switchTo().window(winHandle)
>3. WebDriver driver = DriverFactory.getWebDriver()
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

With the Chrome has been already opened by the above script, I ran the test case. I saw the browser navigated to "YouTube". I got the following output in the console.

```
2025-01-05 09:48:09.105 INFO  c.k.katalon.core.main.TestCaseExecutor   - --------------------
2025-01-05 09:48:09.108 INFO  c.k.katalon.core.main.TestCaseExecutor   - START Test Cases/interactWithAnExistingBrowserSession
2025-01-05 09:48:09.843 DEBUG t.interactWithAnExistingBrowserSession   - 1: System.setProperty("webdriver.chrome.driver", getChromeDriverPath())
2025-01-05 09:48:09.924 DEBUG t.interactWithAnExistingBrowserSession   - 2: options = new org.openqa.selenium.chrome.ChromeOptions()
2025-01-05 09:48:09.974 DEBUG t.interactWithAnExistingBrowserSession   - 3: options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222")
2025-01-05 09:48:09.986 DEBUG t.interactWithAnExistingBrowserSession   - 4: driver = new org.openqa.selenium.chrome.ChromeDriver(options)
Starting ChromeDriver 131.0.6778.204 (52183f9e99a61056f9b78535f53d256f1516f2a0-refs/branch-heads/6778_155@{#7}) on port 32465
Only local connections are allowed.
Please see https://chromedriver.chromium.org/security-considerations for suggestions on keeping ChromeDriver safe.
ChromeDriver was started successfully on port 32465.
1月 05, 2025 9:48:11 午前 org.openqa.selenium.remote.ProtocolHandshake createSession
情報: Detected dialect: W3C
2025-01-05 09:48:11.736 DEBUG t.interactWithAnExistingBrowserSession   - 5: changeWebDriver(driver)
2025-01-05 09:48:11.750 INFO  c.k.k.core.webui.driver.DriverFactory    - sessionId = 8930f9634147bc735b1979dfe7c9bece
2025-01-05 09:48:11.777 INFO  c.k.k.core.webui.driver.DriverFactory    - browser = Chrome 131.0.0.0
2025-01-05 09:48:11.780 INFO  c.k.k.core.webui.driver.DriverFactory    - platform = Mac OS X
2025-01-05 09:48:11.782 INFO  c.k.k.core.webui.driver.DriverFactory    - seleniumVersion = 3.141.59
2025-01-05 09:48:11.799 INFO  c.k.k.core.webui.driver.DriverFactory    - proxyInformation = ProxyInformation { proxyOption=NO_PROXY, proxyServerType=HTTP, username=, password=********, proxyServerAddress=, proxyServerPort=0, executionList="", isApplyToDesiredCapabilities=true }
2025-01-05 09:48:11.834 DEBUG t.interactWithAnExistingBrowserSession   - 6: openBrowser("https://www.youtube.com/")
2025-01-05 09:48:12.124 WARN  c.k.k.core.webui.driver.DriverFactory    - A browser is already opened. Closing browser and opening a new one
2025-01-05 09:48:12.145 INFO  c.k.k.core.webui.driver.DriverFactory    - Starting 'Chrome' driver
1月 05, 2025 9:48:12 午前 org.openqa.selenium.remote.DesiredCapabilities chrome
情報: Using `new ChromeOptions()` is preferred to `DesiredCapabilities.chrome()`
2025-01-05 09:48:12.164 INFO  c.k.k.core.webui.driver.DriverFactory    - Action delay is set to 0 milliseconds
Starting ChromeDriver 131.0.6778.204 (52183f9e99a61056f9b78535f53d256f1516f2a0-refs/branch-heads/6778_155@{#7}) on port 26396
Only local connections are allowed.
Please see https://chromedriver.chromium.org/security-considerations for suggestions on keeping ChromeDriver safe.
ChromeDriver was started successfully on port 26396.
1月 05, 2025 9:48:14 午前 org.openqa.selenium.remote.ProtocolHandshake createSession
情報: Detected dialect: W3C
2025-01-05 09:48:14.051 INFO  c.k.k.core.webui.driver.DriverFactory    - sessionId = 667b8d6cd1f7e6075930884bb3e4c421
2025-01-05 09:48:14.261 INFO  c.k.k.core.webui.driver.DriverFactory    - browser = Chrome 131.0.0.0
2025-01-05 09:48:14.272 INFO  c.k.k.core.webui.driver.DriverFactory    - platform = Mac OS X
2025-01-05 09:48:14.293 INFO  c.k.k.core.webui.driver.DriverFactory    - seleniumVersion = 3.141.59
2025-01-05 09:48:14.325 INFO  c.k.k.core.webui.driver.DriverFactory    - proxyInformation = ProxyInformation { proxyOption=NO_PROXY, proxyServerType=HTTP, username=, password=********, proxyServerAddress=, proxyServerPort=0, executionList="", isApplyToDesiredCapabilities=true }
2025-01-05 09:48:20.605 DEBUG t.interactWithAnExistingBrowserSession   - 7: title = getWindowTitle()
2025-01-05 09:48:21.027 DEBUG t.interactWithAnExistingBrowserSession   - 8: assert title.contains("YouTube")
2025-01-05 09:48:21.083 DEBUG t.interactWithAnExistingBrowserSession   - 9: closeBrowser()
2025-01-05 09:48:21.607 INFO  c.k.katalon.core.main.TestCaseExecutor   - END Test Cases/interactWithAnExistingBrowserSession
```

The test case ran successfully.

### Conclusion: What mdu_kay7 needs to do

He has to change his windows app. The app have to start Chrome with `--remote-debugging-port=xxxx` as my shell script does. Without the port is opened by Chrome process, external processes like Katalon's test case script will never be able to communiate with the Chrome process.


