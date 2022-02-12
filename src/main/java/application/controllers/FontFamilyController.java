package application.controllers;


import application.services.FontFamilyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FontFamilyController {

    FontFamilyService fontFamilyService;

    @Autowired
    public FontFamilyController(FontFamilyService fontFamilyService) {
        this.fontFamilyService = fontFamilyService;
    }

    @GetMapping(path = "/allFontFamily")
    public ResponseEntity<List<String>> getFontFamily(){
        List<String> allFontFamilies = this.fontFamilyService.getAllFontFamilies();

        return  new ResponseEntity<>(allFontFamilies, HttpStatus.OK);
    }


}
