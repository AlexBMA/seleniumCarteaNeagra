package main;

import helper.HelperClass;
import org.apache.poi.common.usermodel.fonts.FontGroup;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.sl.usermodel.VerticalAlignment;
import org.apache.poi.sl.usermodel.PaintStyle;
import org.apache.poi.xslf.usermodel.*;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static helper.HelperClass.*;

public class GenerateSlides {
    public static final String CALIBRI_LIGHT = "Calibri Light";
    public static final String CALIBRI = "Calibri";
    public static final double FONT_SIZE = 80;

    public static final String OUTPUT_FOLDER = "ppt_files\\";
    public static final String INPUT_FOLDER = "txt_files\\";

    /*
     * Title Slide
     * Picture with Caption
     * Title and Vertical Text
     * Comparison
     * Blank
     * Vertical Title and Text
     * Title and Content
     * Title Only
     * Section Header
     * Two Content
     * Content with Caption
     */

    public static void main222() throws IOException {
//        System.out.println("BEGIN");
//
//        String txtFile ="D:\\Proiecte\\carteaNeagraSel\\TestNewR.txt";
//
//        String text = new String(Files.readAllBytes(Paths.get(txtFile)));
//        System.out.println(text);
//
//        String[] splitText = text.split("[0-9][.]+|[0-9]{1,}|(\r\n:)");
//
//        List<String> updateSplit = new ArrayList<>();
//
//        updateTextForPpt(text, splitText, updateSplit);
//        createNewPpt(updateSplit,OUTPUT_FOLDER + "TestNewRegex.ppt");
//
//        System.out.println("Done");

    }

    public static void main(String[] args) throws IOException {

        System.out.println("BEGIN MAKE SURE YOU PUT THE RIGHT SLASH WINDOWS OR LINUX  \\ is WINDOWS / is LINUX ");

        Stream<Path> paths = Files.walk(Paths.get(INPUT_FOLDER));
        List<Path> fileNames = paths.filter(item -> item.toString().endsWith("txt")).collect(Collectors.toList());

        for (Path item : fileNames) {
            int position = item.toString().lastIndexOf("\\");
            // System.out.println(position);
            String pptTitle = item.toString().substring(position + 1).replace("txt", "pptx");
            // System.out.println(pptTitle);

            System.out.println("reading txt file");
            String text = Files.readString(Paths.get(item.toString())).replace("\r", "");
            // System.out.println(text);

            String[] splitText = text.split("(\r\n\r\n[\r\n]*)|(\n\n[\n]*)");
            System.out.println("splitting the text by verses");
            List<String> updateSplit = splitTextByVerses(text,false);

            // updateTextForPpt(text, splitText, updateSplit);
            System.out.println("creating the ppt file");
            System.out.println(OUTPUT_FOLDER);
            createNewPpt(updateSplit, OUTPUT_FOLDER + pptTitle, false);

            System.out.println("Done");
        }
        paths.close();
    }

    private static List<String> splitTextByVerses(String text, boolean lowerThird) {
        String[] splitText = text.split("(\n\n[\n]*)");
        int firstIndexChorus = -1, lastIndexChorus = -1;
        boolean multipleChorus = false;

        for (int i = 0; i < splitText.length; i++) {
            if (splitText[i].startsWith("R:")) { // the following verse is a chorus
                // the first encounter of a chorus
                if (firstIndexChorus == -1) {
                    firstIndexChorus = i;
                } else {
                    // two chorus verses
                    // check if the chorus verses are separated
                    if (i - 1 != lastIndexChorus) {
                        multipleChorus = true;
                        break;
                    }
                }

                lastIndexChorus = i;
            }
        }

        // if no chorus was found, do not perform any addition
        if (firstIndexChorus == -1) {
            multipleChorus = true;
            lastIndexChorus = splitText.length - 1;
        }

        List<String> listOfVerses = new ArrayList<>(Arrays.asList(splitText).subList(0, lastIndexChorus + 1));

        for (int i = lastIndexChorus + 1; i < splitText.length; i++) {
            listOfVerses.add(splitText[i]);

            // if there is only one chorus, add it after each verse
            // firstIndexChorus will act also as an indicator for the periodicity of inserting the chorus
            if (!multipleChorus && (i - lastIndexChorus) % firstIndexChorus == 0) {
                listOfVerses.addAll(Arrays.asList(splitText).subList(firstIndexChorus, lastIndexChorus + 1));
            }
        }

        if (lowerThird) {
            List<String> listOfSubtitles = new ArrayList<>();


            for (String verse : listOfVerses) {
                String prefix = "";

                if (verse.startsWith("R:")) {
                    verse = verse.substring(2);
                    prefix = "R:";
                }

                String[] lines = verse.split("\n");

                for (int i = 0; i+1 < lines.length; i+=2)
                    listOfSubtitles.add(prefix + lines[i] + "\n" + lines[i+1]);

                if (lines.length % 2 == 1)
                    listOfSubtitles.add(prefix + lines[lines.length-1]);
            }

            return listOfSubtitles;
        }

        return listOfVerses;
    }

    private static void updateTextForPpt(String text, String[] splitText, List<String> updateSplit) {

//        if (text.contains("Cor:")) {
//            String corRefren = splitText[3].trim();
//
//
//            for (String t : splitText) {
//                String item = t.trim();
//
//                if (!item.isBlank() && !item.equals(corRefren)) {
//
//                    updateSplit.add(item);
//                    updateSplit.add(refren);
//                }
//            }
//        } else {
//
//            for (String t : splitText) {
//                String item = t.trim();
//                if (!item.isBlank()) {
//                    updateSplit.add(item);
//                }
//            }
//        }
    }

    private static void createNewPpt(List<String> updateSplit, String fileName, boolean lowerThird) throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        XMLSlideShow ppt = new XMLSlideShow();
        ppt.setPageSize(new Dimension((int) width, (int) height));

        XSLFSlideMaster defaultMaster = ppt.getSlideMasters().get(0);
        defaultMaster.getBackground().setFillColor(new Color(9, 45, 38));

        XSLFSlideLayout layout = defaultMaster.getLayout(SlideLayout.BLANK);

        int length = updateSplit.size();
        if (length > 0) {

            for (String s : updateSplit) {
                addOptionToSlide(s, new Rectangle((int) width, (int) height), ppt.createSlide(layout), lowerThird);
            }

        }

        FileOutputStream out = new FileOutputStream(fileName);
        ppt.write(out);
        ppt.close();
        out.close();
    }

    private static void addOptionToSlide(String s, Rectangle anchor, XSLFSlide slide1, boolean lowerThird) {
        XSLFTextBox shape = slide1.createTextBox();
        shape.setAnchor(anchor);

        // remove the other text paragraphs that were created
        if (shape.getTextParagraphs().size() > 0)
            shape.clearText();
       
        XSLFTextParagraph p = shape.addNewTextParagraph();

        shape.setVerticalAlignment(lowerThird ? VerticalAlignment.BOTTOM : VerticalAlignment.MIDDLE);
        p.setTextAlign(TextParagraph.TextAlign.CENTER);
        p.setFontAlign(TextParagraph.FontAlign.CENTER);
        XSLFTextRun r = p.addNewTextRun();

        if (s.trim().startsWith("R:")) {
            r.setItalic(true);
            s = s.trim().substring(2);
        }

        r.setText(s.trim());
        r.setFontColor(Color.WHITE);
        r.setFontSize(FONT_SIZE);
        r.setBold(true);
        setOutlineAndGlow(r, createSolidFillLineProperties(java.awt.Color.BLACK, 1.5), createGlow(java.awt.Color.BLACK, 7));

        r.setFontFamily(CALIBRI, FontGroup.LATIN); // or CALIBRI_LIGHT
    }


    static void setOutlineAndGlow(XSLFTextRun run,
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties lineProperties,
            org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList effectList) {
        // get underlying CTRegularTextRun object
        org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun ctRegularTextRun = (org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun) run
                .getXmlObject();
        // Are there run properties already? If not, add one.
        if (ctRegularTextRun.getRPr() == null)
            ctRegularTextRun.addNewRPr();
        // Is there outline set already? If so, unset it, because we are creating it
        // new.
        if (ctRegularTextRun.getRPr().isSetLn())
            ctRegularTextRun.getRPr().unsetLn();

        // Is there glow set already? If so, unset it, because we are creating it
        // new.
        if (ctRegularTextRun.getRPr().isSetEffectLst())
            ctRegularTextRun.getRPr().unsetEffectLst();

        // set a new outline and glow
        ctRegularTextRun.getRPr().setLn(lineProperties);
        ctRegularTextRun.getRPr().setEffectLst(effectList);
    }





    // your method fontStyles taken to Java code
    static void fontStyles(XSLFTextRun templateRun, XSLFTextShape textShape) {
        String fontFamily = templateRun.getFontFamily();
        PaintStyle fontColor = templateRun.getFontColor();
        Double fontSize = templateRun.getFontSize();
        boolean fontBold = templateRun.isBold();
        boolean fontItalic = templateRun.isItalic();
        TextParagraph.TextAlign textAlign = templateRun.getParagraph().getTextAlign();
        org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties lineProperties = getOutline(templateRun);
        org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList effectList = getEffectList(templateRun);
        for (XSLFTextParagraph paragraph : textShape.getTextParagraphs()) {
            for (XSLFTextRun run : paragraph.getTextRuns()) {
                run.setFontFamily(fontFamily);
                if (run != templateRun)
                    run.setFontColor(fontColor); // set PaintStyle has the issue which I am avoiding by using a copy of
                                                 // the underlying XML
                run.setFontSize(fontSize);
                run.setBold(fontBold);
                run.setItalic(fontItalic);
                run.getParagraph().setTextAlign(textAlign);

                setOutlineAndGlow(run, lineProperties, effectList);
            }
        }
    }

    // XSLFSlideLayout[] slideLayouts = defaultMaster.getSlideLayouts();
    // for (XSLFSlideLayout s: slideLayouts) {
    // System.out.println(s.getName());
    // }

    // for (XSLFShape shape : slide.getShapes()) {
    // if (shape instanceof XSLFAutoShape) {
    // // this is a template placeholder
    // }
    // }

    // GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    // String[] fontFamilies = ge.getAvailableFontFamilyNames();
    // for (String ff : fontFamilies) {
    // System.out.println(ff);
    // }
}
