package org.task;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

public class TaskTwo {
	public static String userName = "prexo.mis@dealsdray.com";
	public static String password = "prexo.mis@dealsdray.com";
	public static String projectPath = System.getProperty("user.dir");
	public static WebDriver driver;
	public static String browserName = "chrome";
	public static String url = "https://demo.dealsdray.com";

	public static void main(String[] args) throws Exception {
		driver = LaunchBrowser(browserName, url);
		takeScreenShot("LoginPage");
		login(userName, password);
		clickOnAddBulkOrders();
		takeScreenShot("OrdersPage");
		uploadFileAndClickOnValidateData();
		simpleAlert();
		takeScreenShot("AfterUploadedFile");
		takeScreenShotElement("//th[text()='S.NO']/ancestor::table", "Table");
		driver.quit();
	}

	public static WebDriver LaunchBrowser(String browserName, String url) throws Exception {
		switch (browserName.toLowerCase()) {
		case "chrome":
			System.setProperty("webdriver.chrome.driver", projectPath + "\\Driver\\chromedriver.exe");
			driver = new ChromeDriver();
			driver.get(url);
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			Thread.sleep(1000);
			break;
		case "firefox":
			System.setProperty("webdriver.gecko.driver", projectPath + "\\Driver\\geckodriver.exe");
			driver = new FirefoxDriver();
			driver.get(url);
			driver.manage().window().maximize();
			break;
		case "safari":
			System.setProperty("webdriver.safari.driver", projectPath + "\\Driver\\safaridriver.exe");
			driver = new SafariDriver();
			driver.get(url);
			driver.manage().window().maximize();
			break;
		default:
			System.out.println(browserName);
			break;
		}
		return driver;
	}

	public static void simpleAlert() {
		driver.switchTo().alert().accept();
	}

	public static void takeScreenShot(String screenShotName) throws Exception {
		TakesScreenshot screenshot = (TakesScreenshot) driver;
		File target = screenshot.getScreenshotAs(OutputType.FILE);
		File Source = new File(projectPath + "\\TaskTwoScreenShot\\" + screenShotName + ".png");
		FileUtils.copyFile(target, Source);
		Thread.sleep(3000);
	}
	
	public static void takeScreenShotElement(String locator, String screenShotName) throws Exception {
		File target = driver.findElement(By.xpath(locator)).getScreenshotAs(OutputType.FILE);
		File Source = new File(projectPath + "\\TaskTwoScreenShot\\" + screenShotName + ".png");
		FileUtils.copyFile(target, Source);
		Thread.sleep(3000);
	}

	public static void login(String userName, String password) throws Exception {
		driver.findElement(By.name("username")).sendKeys(userName);
		Thread.sleep(2000);
		driver.findElement(By.name("password")).sendKeys(password);
		Thread.sleep(2000);
		driver.findElement(By.xpath("//button[@type='submit'][text()='Login']")).click();
	}

	public static void clickOnAddBulkOrders() {
		driver.findElement(By.xpath("//span[text()='Order']/ancestor::button[@type='button']")).click();
		driver.findElement(By.xpath("//span[text()='Orders']/ancestor::button[@type='button']")).click();
		driver.findElement(By.xpath("//button[text()='Add Bulk Orders'][@type='button']")).click();
	}

	public static void uploadFileAndClickOnValidateData() throws Exception {
		String projectPath = System.getProperty("user.dir");
		driver.findElement(By.xpath("//input[@type='file']")).sendKeys(projectPath + "\\TestData\\demo-data.xlsx");
		driver.findElement(By.xpath("//button[text()='Import'][@type='button']")).click();
		driver.findElement(By.xpath("//button[text()='Validate Data'][@type='button']")).click();
		Thread.sleep(2000);
	}

}
