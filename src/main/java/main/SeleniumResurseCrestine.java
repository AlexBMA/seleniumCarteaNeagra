package main;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class SeleniumResurseCrestine {


    //https://www.versuricrestine.ro/muzica-crestina/4705/apa-i-schimbat-o-in-vin.html
    //
    private static WebDriver driver;

    public static  void initSelenium(){
        if(driver==null){
            //D:\\Proiecte\\carteaNeagraSel\\geckodriver-v0.29.1-win64\\geckodriver.exe"
            System.setProperty("webdriver.gecko.driver","geckodriver-v0.29.1-win64\\geckodriver.exe");

            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");
            driver = new FirefoxDriver(options);
        }
    }

    public static void closeSelenium(){
        if(driver!=null) {
            driver.close();
        }
    }

    public static String getSongTextFromResurseCrestine(String link){

        driver.get(link);
        WebElement element = driver.findElement(By.className("resized-text"));

        return element.getText();
    }
}
