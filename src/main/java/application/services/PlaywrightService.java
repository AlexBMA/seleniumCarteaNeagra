package application.services;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


@Service
public class PlaywrightService {

    public static final String BR_BR = "<br> <br>";
    public static final String BR = "<br>";
    public static final String REPLACEMENT = "";
    private  Playwright playwright;
    private Browser browser;

    @PostConstruct
    public void initPlaywright(){
        if(playwright== null) {
            playwright = Playwright.create();
            browser = playwright.firefox().launch();
        }
    }

    public String getTextFromLinkResurseCrestine(String link){
        Page page = browser.newPage();
        page.navigate(link);
        Locator locator = page.locator("//html/body/div[1]/div[1]/div[2]/div[1]/div[3]/div");
        String innerHTML = locator.innerHTML();
        return innerHTML.replace(BR_BR, "\n\r\n\r").replace(BR, REPLACEMENT);
    }


}
