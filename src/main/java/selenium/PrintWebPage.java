package selenium;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.Pdf;
import org.openqa.selenium.PrintsPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.print.PrintOptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Driver;
import java.util.Base64;

public class PrintWebPage {

	 void doIt() {

	}


	public static void main(String[] args) throws IOException {


		FirefoxOptions options = new FirefoxOptions();
		options.addArguments("-profile", "/home/alexander/.mozilla/firefox/y67tzlev.default-release/");
		FirefoxDriver driver = new FirefoxDriver(options);


		try {

			driver.get("about:blank");
			String domain = ".yandex.ru";

			Cookie cookie = new Cookie.Builder("", "").domain(domain).sameSite("Lax").build();
			driver.manage().addCookie(cookie);

			Thread.sleep(1000);

			driver.navigate().to("https://books.yandex.ru/reader/xk0okOPa?resource=book");


/*
		PrintOptions printOptions = new PrintOptions();
		printOptions.setOrientation(PrintOptions.Orientation.LANDSCAPE);

		PrintsPage printer = (PrintsPage) driver;
		Pdf printedPage = printer.print(printOptions);


		Pdf pdf = driver.print(new PrintOptions());
		byte[] pdfContent = Base64.getDecoder().decode(pdf.getContent());
		Files.write(Paths.get("./Baeldung_Weekly.pdf"), pdfContent);
*/
		} catch (InterruptedException e) {
			throw new RuntimeException("", e);
		} finally {
//			driver.quit();
		}
	}
}
