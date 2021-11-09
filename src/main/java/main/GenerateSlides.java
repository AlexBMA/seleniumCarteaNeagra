package main;

import org.apache.poi.common.usermodel.fonts.FontGroup;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.sl.usermodel.VerticalAlignment;
import org.apache.poi.xslf.usermodel.*;


import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class GenerateSlides {
    public static final String CALIBRI_LIGHT = "Calibri Light";

    public static final String OUTPUT_FOLDER = "D:\\Proiecte\\carteaNeagraSel\\carteaNeagraPpt\\";
    public static final String INPUT_FOLDER = "D:\\Proiecte\\carteaNeagraSel\\carteaNeagraTxt\\";

    /*
    Title Slide
    Picture with Caption
    Title and Vertical Text
    Comparison
    Blank
    Vertical Title and Text
    Title and Content
    Title Only
    Section Header
    Two Content
    Content with Caption
    */

    public static void main(String[] args) throws IOException {

        System.out.println("BEGIN");

        String txtFile ="D:\\Proiecte\\carteaNeagraSel\\TestNewR.txt";

        String text = new String(Files.readAllBytes(Paths.get(txtFile)));
        System.out.println(text);

        String[] splitText = text.split("[0-9][.]+|[0-9]{1,}|(\r\n:)");

        List<String> updateSplit = new ArrayList<>();

        updateTextForPpt(text, splitText, updateSplit);
        createNewPpt(updateSplit,OUTPUT_FOLDER + "TestNewRegex.ppt");

        System.out.println("Done");

    }

    public static void main222() throws IOException {

        System.out.println("BEGIN");

        Stream<Path> paths = Files.walk(Paths.get(INPUT_FOLDER));
        List<Path> fileNames = paths.filter(item->item.toString().endsWith("txt")).collect(Collectors.toList());

        for (Path item : fileNames) {
            int position = item.toString().lastIndexOf("\\");
            System.out.println(position);
            String pptTitle = item.toString().substring(position+1).replace("txt","ppt");
            System.out.println(pptTitle);


            String text = new String(Files.readAllBytes(Paths.get(item.toString())));
            System.out.println(text);

            String[] splitText = text.split("[0-9][.]+|[0-9]{2,}|(Cor:)");


            List<String> updateSplit = new ArrayList<>();

            updateTextForPpt(text, splitText, updateSplit);
            createNewPpt(updateSplit,OUTPUT_FOLDER + pptTitle);

            System.out.println("Done");

        }

    }

    private static void updateTextForPpt(String text, String[] splitText, List<String> updateSplit) {
        if(text.contains("Cor:")||text.contains("R:")){
            // needs more work for R: case

            String refren = splitText[3].trim();

            for(String t: splitText){
                if(t.contains("Cor:")||t.contains("R:")){
                    refren = t.trim();
                }
            }
            
           // corRefren = splitText[3].trim();

            for(String t: splitText){
                String item = t.trim();
                if(!item.isBlank() && !item.equals(refren)){
                    updateSplit.add(item);
                    updateSplit.add(refren);
                }
            }
        } else {

            for(String t: splitText){
                String item = t.trim();
                if(!item.isBlank()){
                    updateSplit.add(item);
                }
            }
        }
    }

    private static void createNewPpt(List<String> updateSplit,String fileName) throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

//        System.out.println(width);
//        System.out.println(height);

        XMLSlideShow ppt = new XMLSlideShow();
        ppt.setPageSize(new Dimension((int)width,(int)height));

        XSLFSlideMaster defaultMaster = ppt.getSlideMasters().get(0);
        defaultMaster.getBackground().setFillColor(new Color(9,45,38));

        XSLFSlideLayout layout = defaultMaster.getLayout(SlideLayout.BLANK);

//        System.out.println("####");

        int length = updateSplit.size();
        if(length >0){

            for (String s : updateSplit) {
                addOptionToSlide(s, new Rectangle((int) width, (int) height), ppt.createSlide(layout));
            }

        }

        FileOutputStream out = new FileOutputStream(fileName);
        ppt.write(out);
        out.close();
    }

    private static void addOptionToSlide(String s, Rectangle anchor, XSLFSlide slide1) {
        XSLFTextBox shape = slide1.createTextBox();
        shape.setAnchor(anchor);
        XSLFTextParagraph p = shape.addNewTextParagraph();

        shape.setVerticalAlignment(VerticalAlignment.MIDDLE);
        p.setTextAlign(TextParagraph.TextAlign.CENTER);
        p.setFontAlign(TextParagraph.FontAlign.CENTER);
        XSLFTextRun r = p.addNewTextRun();
        r.setText(s.trim());
        r.setFontColor(Color.WHITE);
        r.setFontSize(60.);
        r.setFontFamily(CALIBRI_LIGHT, FontGroup.LATIN);
    }


//        XSLFSlideLayout[] slideLayouts = defaultMaster.getSlideLayouts();
//        for (XSLFSlideLayout s: slideLayouts) {
//                System.out.println(s.getName());
//            }

//        for (XSLFShape shape : slide.getShapes()) {
//            if (shape instanceof XSLFAutoShape) {
//                // this is a template placeholder
//            }
//        }


//        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        String[] fontFamilies = ge.getAvailableFontFamilyNames();
//        for (String ff : fontFamilies) {
//            System.out.println(ff);
//        }
}
