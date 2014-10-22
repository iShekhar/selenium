/*
Copyright 2007-2009 Selenium committers
Copyright 2012 Software Freedom Conservancy

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package org.openqa.selenium.v1;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;

import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class FastWebDriverBackedSeleniumTest {

  @Test
  public void openPrefixARelativeURLWithTheBaseURL() {
    final WebDriverBackedSelenium selenium;
    final WebDriver driver = mock(WebDriverWithJs.class);

    selenium = new WebDriverBackedSelenium(driver, "http://a.base.url:3000");
    selenium.open("a/relative/path");

    verify(driver).get("http://a.base.url:3000/a/relative/path");
  }

  @Test
  public void openPrefixARelativeURLWithTheBaseURLEvenWhenItStartsWithASlash() {
    final WebDriverBackedSelenium selenium;
    final WebDriver driver = mock(WebDriverWithJs.class);

    selenium = new WebDriverBackedSelenium(driver, "http://a.base.url:3000");
    selenium.open("/relative/path/starting_with_a_slash");

    verify(driver).get("http://a.base.url:3000/relative/path/starting_with_a_slash");
  }

  @Test
  public void openDoesNotPrefixAURLIncludingHttpProtocol() {
    final WebDriverBackedSelenium selenium;
    final WebDriver driver = mock(WebDriverWithJs.class);

    selenium = new WebDriverBackedSelenium(driver, "http://a.base.url:3000");
    selenium.open("http://a.url/with/protocol.info");

    verify(driver).get("http://a.url/with/protocol.info");
  }

  @Test
  public void openDoesNotPrefixAURLIncludingHttpsProtocol() {
    final WebDriverBackedSelenium selenium;
    final WebDriver driver = mock(WebDriverWithJs.class);

    selenium = new WebDriverBackedSelenium(driver, "http://a.base.url:3000");
    selenium.open("https://a.url/with/protocol.info");

    verify(driver).get("https://a.url/with/protocol.info");
  }

  public static interface WebDriverWithJs extends WebDriver, JavascriptExecutor {
    // empty
  }
}
