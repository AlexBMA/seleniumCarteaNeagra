package application.controllers;
import application.dto.FileDto;
import application.dto.FileOutputDto;
import application.services.GeneratePptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


@RestController
@RequestMapping(path = "/file")
@CrossOrigin
public class FileController {

    private GeneratePptService generatePptService;

    @Autowired
    public FileController(GeneratePptService generatePptService) {
        this.generatePptService = generatePptService;
    }

    @PostMapping(path="/upload", produces = {"application/octet-stream"})
    public ResponseEntity<Resource> uploadFile(@RequestParam("file") MultipartFile multipartFile,
                                               @RequestParam("width")int width,
                                               @RequestParam("height") int height){

        System.err.println(width);
        System.err.println(height);
        try {
            byte[] pptFile = generatePptService.createPPTFile(multipartFile, width, height);

            Resource resource = new ByteArrayResource(pptFile);

            return ResponseEntity.ok()
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
