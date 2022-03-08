package application.controllers;

import application.services.GeneratePptService;
import application.services.PlaywrightService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping(path = "/file")
@CrossOrigin
public class FileController {

    private GeneratePptService generatePptService;
    private PlaywrightService playwrightService;

    @Autowired
    public FileController(GeneratePptService generatePptService, PlaywrightService playwrightService)
    {
        this.generatePptService = generatePptService;
        this.playwrightService = playwrightService;
    }

    @PostMapping(path="/upload", produces = {"application/octet-stream"})
    public ResponseEntity<Resource> uploadFile(@RequestParam("file") MultipartFile multipartFile,
                                               @RequestParam("width")int width,
                                               @RequestParam("height") int height,
                                               @RequestParam("textSize") float textSize,
                                               @RequestParam("textColor") String textColor,
                                               @RequestParam("glowValue")float glowValue,
                                               @RequestParam("glowColor") String glowColor,
                                               @RequestParam("outlineValue")  float outlineValue,
                                               @RequestParam("outlineColor")  String outlineColor,
                                               @RequestParam("backgroundColor")  String background)
    {

        try{
            byte[] pptFile = generatePptService.createPPTFile(multipartFile, width, height);

            Resource resource = new ByteArrayResource(pptFile);

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);


        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping(path="/link", produces = {"application/octet-stream"})
    public ResponseEntity<Resource> getFromLink (@RequestParam("url") String url,
                                                 @RequestParam("width") int width,
                                                 @RequestParam("height") int height,
                                                 @RequestParam("textSize") float textSize,
                                                 @RequestParam("textColor") String textColor,
                                                 @RequestParam("glowValue")float glowValue,
                                                 @RequestParam("glowColor") String glowColor,
                                                 @RequestParam("outlineValue")  float outlineValue,
                                                 @RequestParam("outlineColor")  String outlineColor,
                                                 @RequestParam("backgroundColor")  String background)
    {

        //String link ="https://www.resursecrestine.ro/cantece/65663/cred-in-dumnezeu-ca-tata";
        try {

            String text = playwrightService.getTextFromLinkResurseCrestine(url);
            byte[] pptFile = generatePptService.createPPTFileFromLink(text, width, height);

            Resource resource = new ByteArrayResource(pptFile);

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }


    }

    @PostMapping(path="/search", produces = {"application/octet-stream"})
    public ResponseEntity<Resource> getFromSearch(@RequestParam("url") String url,
                                                 @RequestParam("width") int width,
                                                 @RequestParam("height") int height,
                                                 @RequestParam("textSize") float textSize,
                                                 @RequestParam("textColor") String textColor,
                                                 @RequestParam("glowValue")float glowValue,
                                                 @RequestParam("glowColor") String glowColor,
                                                 @RequestParam("outlineValue")  float outlineValue,
                                                 @RequestParam("outlineColor")  String outlineColor,
                                                 @RequestParam("backgroundColor")  String background)
    {

        try {

            String text = playwrightService.getTextFromSearchResuseCrestine(url);
            byte[] pptFile = generatePptService.createPPTFileFromLink(text, width, height);

            Resource resource = new ByteArrayResource(pptFile);

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }


    }

}
