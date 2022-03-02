package main;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.List;

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

    public static String getSongTextFromSearchResurseCrestine(String text){
        //https://www.resursecrestine.ro/cauta/Cred+in+Dumnezeu+/2/titlu
        driver.get("https://www.resursecrestine.ro/cauta/"+text.replace(" ","+")+"/2/titlu/sorteaza/vizualizari");

        try{
            List<WebElement> dottedSection = driver.findElements(By.xpath("/html/body/div[1]/div[1]/div[2]/div[1]/div[4]/div[1]/div/div[1]/div[1]/a"));

            WebElement subElement = dottedSection.get(0);
            String href = subElement.getAttribute("href");

            return getSongTextFromResurseCrestine(href);

        } catch (Exception e){
            return null;
        }

    }
}
