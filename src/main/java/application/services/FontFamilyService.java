package application.services;

import org.springframework.stereotype.Service;

import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.List;

@Service
public class FontFamilyService {


    public List<String> getAllFontFamilies(){
        return listFontFamily();
    }


    private List<String> listFontFamily() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontFamilies = ge.getAvailableFontFamilyNames();

        return Arrays.asList(fontFamilies);

//        for (String ff : fontFamilies) {
//            System.out.println(ff);
//        }
//
//        return null;
    }
}
