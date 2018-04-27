package com.endava.service_system.admin;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class SeleniumTest extends TestCase {
    private WebDriver driver;
    private static final String HOST="http://localhost:8080";
    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "C:/chromedriver.exe");
        this.driver = new ChromeDriver();
        //driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }
    @Test
    public void testLoginWithoutCredentials() throws Exception {
        this.driver.get(HOST+"/login");
        WebElement loginButton=driver.findElement(By.id("btnLogin"));
        loginButton.click();
        WebDriverWait webDriverWait=new WebDriverWait(driver,3);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error")));
        WebElement badCredentials=driver.findElement(By.id("error"));
        assertNotNull(badCredentials);
        String textError=badCredentials.getText().toLowerCase();
        assertTrue(textError.contains("bad")&&textError.contains("credentials"));
    }

    @Test
    public void testLoginWithAcceptedStatusAndAdminCredentials() throws Exception {
        this.driver.get(HOST+"/login");
        WebElement loginForm=driver.findElement(By.id("username"));
        loginForm.sendKeys("admin");
        WebElement passwordForm=driver.findElement(By.id("password"));
        passwordForm.sendKeys("1qa2ws3ed");
        WebElement loginButton=driver.findElement(By.id("btnLogin"));
        loginButton.click();
        WebDriverWait webDriverWait=new WebDriverWait(driver,3);
        webDriverWait.until(ExpectedConditions.titleContains("Admin"));
    }

    @Test
    public void testLoginWithAcceptedStatusAndCompanyCredentials() throws Exception {
        this.driver.get(HOST+"/login");
        WebElement loginForm=driver.findElement(By.id("username"));
        loginForm.sendKeys("company3");
        WebElement passwordForm=driver.findElement(By.id("password"));
        passwordForm.sendKeys("1qa2ws3ed");
        WebElement loginButton=driver.findElement(By.id("btnLogin"));
        loginButton.click();
        WebDriverWait webDriverWait=new WebDriverWait(driver,3);
        webDriverWait.until(ExpectedConditions.titleContains("Company"));
        //TODO refator when company profile page will be done
    }

    @After
    public void tearDown() throws Exception {
        this.driver.quit();
    }

}
