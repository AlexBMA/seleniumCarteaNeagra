package main;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class SeleniumCNeagra {



    public static void main123456789(String[] args) throws IOException {

        String user = "Alex";
        String pass ="alex";

        String sharedFolder="shared";
        String path="smb://192.168.56.1/"+sharedFolder+"/test.txt";
        String path2 ="smb://192.168.1.11";
        String path3 ="smb://192.168.56.1";

        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("",user, pass);




        SmbFile smbFile = new SmbFile(path3,auth);
        SmbFile[] smbFiles = smbFile.listFiles();
        String aa="123456";

//        SmbFileOutputStream smbfos = new SmbFileOutputStream(smbFile);
//        smbfos.write("testing....and writing to a file".getBytes());
        System.out.println("completed ...nice !");
    }

    public static void mainOOO() {

        //setting the driver executable
        System.setProperty("webdriver.gecko.driver",
                "D:\\Proiecte\\carteaNeagraSel\\geckodriver-v0.29.1-win64\\geckodriver.exe");


        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("browser.download.folderList", 2);
        options.addPreference("browser.download.dir", "C:\\Users\\Alexandru\\Desktop\\cantari_2019\\cartea neagra");
        options.addPreference("browser.download.useDownloadDir", true);
        options.addPreference("browser.download.viewableInternally.enabledTypes", "");
        options.addPreference("browser.helperApps.neverAsk.saveToDisk", "application/vnd.ms-powerpoint;application/pdf;text/plain;application/text;text/xml;application/xml");
        options.addPreference("pdfjs.disabled", true);  // disable the built-in PDF viewer
        options.addArguments("--headless");



        //String addressCantari ="https://www.cantaricrestine.ro/?pg=25&categoria=cn#rezultate";

       // String firstPage = "https://www.cantaricrestine.ro/?pg=25&categoria=cn#rezultate";


        for(int i=1;i<=25;i++){

            //Initiating your chromedriver
            WebDriver driver = new FirefoxDriver(options);

            //Applied wait time
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            //maximize window
            driver.manage().window().maximize();
            String link = "https://www.cantaricrestine.ro/?pg="+i+"&categoria=cn#rezultate";

            System.out.println(link);

            driver.get(link);


            if(i != 25){

                for(int j=1;j<=20;j++){

                    String downloadLink = "/html/body/div[1]/div[4]/div[1]/table/tbody/tr["+j+"]/td[4]/a";
                    System.out.println(link+"    "+ downloadLink);
                    WebElement urlDescarca_web = driver.findElement(By.xpath(downloadLink));
                    urlDescarca_web.click();
                }


            }else{

                String downloadLink = "/html/body/div[1]/div[4]/div[1]/table/tbody/tr[1]/td[4]/a";
                WebElement urlDescarca_web = driver.findElement(By.xpath(downloadLink));
                urlDescarca_web.click();


                driver.close();
            }




        }




        /*
        /html/body/div[1]/div[4]/div[1]/table/tbody/tr[1]/td[4]/a
                /html/body/div[1]/div[4]/div[1]/table/tbody/tr[2]/td[4]/a
        ...
        /html/body/div[1]/div[4]/div[1]/table/tbody/tr[20]/td[4]/a
        */

        //open browser with desried URL
       // driver.get(firstPage);

        //System.out.println(driver.getPageSource());

       // WebElement urlDescarca_web = driver.findElement(By.xpath("/html/body/div[1]/div[4]/div[1]/table/tbody/tr[1]/td[4]/a"));
      //  urlDescarca_web.click();



        //driver.findElement("").getAttribute("title").;
        //class="urlDescarca web"


        //closing the browser

    }
}
