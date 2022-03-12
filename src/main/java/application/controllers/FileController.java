package application.controllers;

import application.dto.OptionInputDTO;
import application.services.GeneratePptService;
import application.services.PlaywrightService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
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
    private Gson gson;

    @Autowired
    public FileController(GeneratePptService generatePptService, PlaywrightService playwrightService)
    {
        this.generatePptService = generatePptService;
        this.playwrightService = playwrightService;
        gson = new Gson();
    }



    @PostMapping(path="/upload", produces = {"application/octet-stream"})
    public ResponseEntity<Resource> uploadFile(@RequestParam("file") MultipartFile multipartFile,
                                               @RequestParam("options") String options)
    {

        try{
            OptionInputDTO optionInputDTO = gson.fromJson(options, OptionInputDTO.class);

            byte[] pptFile = generatePptService.createPPTFile(multipartFile, optionInputDTO);

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
                                                 @RequestParam("options") String options)
    {

        //String link ="https://www.resursecrestine.ro/cantece/65663/cred-in-dumnezeu-ca-tata";
        try {
            OptionInputDTO optionInputDTO = gson.fromJson(options, OptionInputDTO.class);

            String text = playwrightService.getTextFromLinkResurseCrestine(url);
            byte[] pptFile = generatePptService.createPPTFileFromLink(text, optionInputDTO);

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
                                                  @RequestParam("options") String options)
    {

        try {
            OptionInputDTO optionInputDTO = gson.fromJson(options, OptionInputDTO.class);

            String text = playwrightService.getTextFromSearchResuseCrestine(url);
            byte[] pptFile = generatePptService.createPPTFileFromLink(text, optionInputDTO);

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
