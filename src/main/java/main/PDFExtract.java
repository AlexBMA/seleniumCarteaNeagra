package main;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.common.usermodel.fonts.FontGroup;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

public class PDFExtract {

    public static void main(String[] args) throws IOException {

        Map<String,List<String>> monthsDates = new HashMap<>();

        monthsDates.put("ianuarie",new ArrayList<>());
        monthsDates.put("februarie",new ArrayList<>());
        monthsDates.put("martie",new ArrayList<>());
        monthsDates.put("aprilie",new ArrayList<>());
        monthsDates.put("mai",new ArrayList<>());
        monthsDates.put("iunie",new ArrayList<>());
        monthsDates.put("iulie",new ArrayList<>());
        monthsDates.put("august",new ArrayList<>());
        monthsDates.put("septembrie",new ArrayList<>());
        monthsDates.put("octombrie",new ArrayList<>());
        monthsDates.put("noiembrie",new ArrayList<>());
        monthsDates.put("decembrie",new ArrayList<>());

        Set<String> months = monthsDates.keySet();

        File file = new File("D:\\Biserici din comunitate\\Calendar Rugăciune BCB 2023.pdf");
        PDDocument pdDocument = PDDocument.load(file);

        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        String text = pdfTextStripper.getText(pdDocument);
        Stream<String> lines = text.lines();
        lines.forEach(line -> {
            String trimedLine = line.trim();
            if(!trimedLine.isBlank()){
                months.forEach(item->{
                    if(trimedLine.contains(item) && trimedLine.length()<20) {
                        monthsDates.get(item).add(trimedLine);
                    }
                });
            }
        });


        String[] split = text.split("Biserica Baptistă|Bisericile Baptiste");

        int length = split.length;
        for(int index = 12; index< 13;index++){
            int lastIndexOfFullStop = split[index].lastIndexOf('.');
            if(lastIndexOfFullStop>0) {
                System.out.println(index+" ##");
                String textBiserica = "Biserica Baptistă "+split[index].trim().substring(0, lastIndexOfFullStop).replace("\r\n","\n");

                System.out.println(textBiserica);
                createPPTForBiserica(textBiserica, index);
            }
        }
    }

    public static void createPPTForBiserica(String text,int index){
        SlideGenerator.createPpt();
        Color slideBackgroundColor = Color.decode("#052D26");
        SlideGenerator.setBackground(slideBackgroundColor.getRed(), slideBackgroundColor.getGreen(), slideBackgroundColor.getBlue());
        XSLFSlide slide = SlideGenerator.addSlide();
        SlideGenerator.addText(slide, text, Color.decode("#FFFFFF"), 45d, "Calibri", FontGroup.LATIN, false);
        try {
            SlideGenerator.savePpt( "SlideBiserica"+index+".pptx");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
