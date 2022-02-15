package application.controllers;
import application.dto.FileDto;
import application.services.GeneratePptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping(path = "/file")
@CrossOrigin
public class FileController {

    private GeneratePptService generatePptService;

    @Autowired
    public FileController(GeneratePptService generatePptService) {
        this.generatePptService = generatePptService;
    }

    @PostMapping(path="/upload")
    public ResponseEntity<byte[]> uploadFile(@RequestParam("file") MultipartFile multipartFile,
                                             @RequestParam("width")int width,
                                             @RequestParam("height") int height){

        try {
            byte[] pptFileBytes = generatePptService.createPPTFile(multipartFile,width, height);
            return ResponseEntity.ok(pptFileBytes);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
