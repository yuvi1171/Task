package org.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

public class TaskOne {

	public static int numberOfLink = 5;
	public static int numberOfBrowser = 1;
	public static String excelSheetName = "Sheet1";
	public static String projectPath = System.getProperty("user.dir");
	public static WebDriver driver;
	public static String browserName;
	public static String url;

	public static void main(String[] args) throws Exception {
		for (int i = 0; i < numberOfLink; i++) {
			url = readExcell(i + 1, 0);
			System.out.println(url);
			for (int j = 0; j < numberOfBrowser; j++) {
				browserName = readExcell(j + 1, 1);
				System.out.println(browserName);
				driver = LaunchBrowser(browserName, url);
				takeScreenShot(browserName + (i + 1));
				driver.quit();
			}
		}
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

	public static String readExcell(int rownum, int cellnum) throws IOException {
		File file = new File(projectPath + "\\TestData\\WorkBook.xlsx");
		FileInputStream stream = new FileInputStream(file);
		Workbook book = new XSSFWorkbook(stream);
		Sheet sheet = book.getSheet(excelSheetName);
		Row row = sheet.getRow(rownum);
		Cell cell = row.getCell(cellnum);
		CellType cellType = cell.getCellType();
		String value = null;
		switch (cellType) {
		case STRING:
			value = cell.getStringCellValue();
			break;
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				Date dateCellValue = cell.getDateCellValue();
				SimpleDateFormat simple = new SimpleDateFormat("dd/MM/yyyy");
				value = simple.format(dateCellValue);
			} else {
				double numericCellValue = cell.getNumericCellValue();
				BigDecimal valueOf = BigDecimal.valueOf(numericCellValue);
				value = valueOf.toString();
			}
			break;
		default:
			break;
		}
		return value;
	}

	public static void takeScreenShot(String screenShotName) throws Exception {
		TakesScreenshot screenshot = (TakesScreenshot) driver;
		File target = screenshot.getScreenshotAs(OutputType.FILE);
		File Source = new File(projectPath + "\\TaskOneScreenShot\\" + screenShotName + ".png");
		FileUtils.copyFile(target, Source);
		Thread.sleep(3000);
	}
}
