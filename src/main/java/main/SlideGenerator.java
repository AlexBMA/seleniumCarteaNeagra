package main;

import org.apache.poi.common.usermodel.fonts.FontGroup;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.sl.usermodel.VerticalAlignment;
import org.apache.poi.xslf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGlowEffect;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SlideGenerator {

    public static XMLSlideShow pptSlideShow;
    public static XSLFSlideLayout slideLayout;
    private static int width;
    private static int height;
    public static final long EMU = 914400;
    public static final long INCH_PT = 72;

    public static List<String> splitTextByVerses(String text) {
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
            if (!multipleChorus && (firstIndexChorus == 0 || (i - lastIndexChorus) % firstIndexChorus == 0)) {
                listOfVerses.addAll(Arrays.asList(splitText).subList(firstIndexChorus, lastIndexChorus + 1));
            }
        }

        return listOfVerses;
    }

    public static List<String> createSubtitles(List<String> verseList) {
        List<String> subtitlesList = new ArrayList<>();

        for (String verse : verseList) {
            String prefix = "";

            if (verse.startsWith("R:")) {
                verse = verse.substring(2);
                prefix = "R:";
            }

            String[] lines = verse.split("\n");

            for (int i = 0; i + 1 < lines.length; i += 2)
                subtitlesList.add(prefix + lines[i] + "\n" + lines[i + 1]);

            if (lines.length % 2 == 1)
                subtitlesList.add(prefix + lines[lines.length - 1]);
        }

        return subtitlesList;
    }

    public static void createPpt(int width, int height) {
        SlideGenerator.width = width;
        SlideGenerator.height = height;

        pptSlideShow = new XMLSlideShow();
        pptSlideShow.setPageSize(new Dimension(width, height));
    }

    public static void createPpt() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = (int) screenSize.getWidth();
        height = (int) screenSize.getHeight();

        pptSlideShow = new XMLSlideShow();
        pptSlideShow.setPageSize(new Dimension(width, height));
    }

    public static void setBackground(int red, int green, int blue) {
        XSLFSlideMaster slideMaster = pptSlideShow.getSlideMasters().get(0);
        slideMaster.getBackground().setFillColor(new Color(red, green, blue));

        slideLayout = slideMaster.getLayout(SlideLayout.BLANK);
    }

    public static XSLFSlide addSlide() {
        XSLFSlide slide = pptSlideShow.createSlide(slideLayout);
        XSLFTextBox shape = slide.createTextBox();
        shape.setAnchor(new Rectangle(width, height));

        // remove the other text paragraphs that were created
        if (shape.getTextParagraphs().size() > 0)
            shape.clearText();

        shape.addNewTextParagraph();

        return slide;
    }

    public static XSLFTextRun addText(XSLFSlide slide, String slideText, Color textColor, double fontSize, String fontTypeFace, FontGroup fontGroup, boolean lowerThird) {
        XSLFTextBox shape = (XSLFTextBox) slide.getShapes().get(0);
        shape.setVerticalAlignment(lowerThird ? VerticalAlignment.BOTTOM : VerticalAlignment.MIDDLE);

        XSLFTextParagraph textParagraph = shape.getTextParagraphs().get(0);
        textParagraph.setTextAlign(TextParagraph.TextAlign.CENTER);
        textParagraph.setFontAlign(TextParagraph.FontAlign.CENTER);

        XSLFTextRun textRun = textParagraph.addNewTextRun();

        if (slideText!=null && slideText.trim().startsWith("R:")) {
            textRun.setItalic(true);
            slideText = slideText.trim().substring(2);
        }

        if(slideText!=null){
            textRun.setText(slideText.trim());
            textRun.setFontColor(textColor);
            textRun.setFontSize(fontSize);
            textRun.setBold(true);
            textRun.setFontFamily(fontTypeFace, fontGroup);

        }

        return textRun;
    }

    public static void addGlow(XSLFTextRun textRun, double glowRadius, int red, int green, int blue) {
        CTEffectList effectList = CTEffectList.Factory.newInstance();

        // add Glow effect
        CTGlowEffect glowEffect = effectList.addNewGlow();
        glowEffect.setRad((int) (glowRadius / INCH_PT * EMU)); // glow radius
        glowEffect.addNewSrgbClr().setVal(new byte[]{(byte) red, (byte) green, (byte) blue}); // glow color

        CTRegularTextRun ctRegularTextRun = (CTRegularTextRun) textRun.getXmlObject();

        // Are there run properties already? If not, add one.
        if (ctRegularTextRun.getRPr() == null)
            ctRegularTextRun.addNewRPr();

        // Is there glow set already? If so, unset it, because we are creating it new.
        if (ctRegularTextRun.getRPr().isSetEffectLst())
            ctRegularTextRun.getRPr().unsetEffectLst();

        // set a new glow
        ctRegularTextRun.getRPr().setEffectLst(effectList);
    }

    public static void addOutline(XSLFTextRun textRun, double outlineWeight, int red, int green, int blue) {
        CTLineProperties lineProperties = org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties.Factory
                .newInstance();
        // set line solid fill color
        lineProperties.addNewSolidFill().addNewSrgbClr()
                .setVal(new byte[]{(byte) red, (byte) green, (byte) blue}); // outline color
        lineProperties.setW((int) (outlineWeight / INCH_PT * EMU)); // outline weight

        CTRegularTextRun ctRegularTextRun = (CTRegularTextRun) textRun.getXmlObject();

        // Are there run properties already? If not, add one.
        if (ctRegularTextRun.getRPr() == null)
            ctRegularTextRun.addNewRPr();

        // Is there outline set already? If so, unset it, because we are creating it new.
        if (ctRegularTextRun.getRPr().isSetLn())
            ctRegularTextRun.getRPr().unsetLn();

        // set a new outline
        ctRegularTextRun.getRPr().setLn(lineProperties);
    }

    public static void savePpt(String outputFilePath) throws IOException {
        FileOutputStream out = new FileOutputStream(outputFilePath);
        pptSlideShow.write(out);
        out.close();
        pptSlideShow.close();
    }
}