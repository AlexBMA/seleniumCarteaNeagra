package application.services;

import application.dto.OptionInputDTO;
import org.apache.poi.common.usermodel.fonts.FontGroup;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.sl.usermodel.VerticalAlignment;
import org.apache.poi.xslf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static application.GenerateSlides.*;
import static application.helper.Constants.*;
import static application.helper.HelperClass.createGlow;
import static application.helper.HelperClass.createSolidFillLineProperties;

@Service
public class GeneratePptService {

    public static final String R_1 = "R1";
    public static final String R_2 = "R2";

    public byte[] createPPTFileFromLink(String text, OptionInputDTO optionInputDTO ) throws IOException {
        List<String> updateSplit = splitTextByVerses(text,false);

        String pptFile = OUTPUT_FOLDER + "testLink.pptx";
                //pptTitle;

        createNewPpt(updateSplit, pptFile, false, optionInputDTO);

        InputStream fileInputStream = new FileInputStream(pptFile);
        return fileInputStream.readAllBytes();
    }

    public byte[] createPPTFile(MultipartFile multipartFile, OptionInputDTO optionInputDTO) throws IOException {

        String name = multipartFile.getOriginalFilename();
        System.err.println(name);

        String pptTitle = name.replace(TXT_EXTENSION, PPTX_EXTENSION);

        System.err.println(pptTitle);

        byte[] allBytes = multipartFile.getInputStream().readAllBytes();
        String text = new String(allBytes, StandardCharsets.UTF_8);

        List<String> updateSplit = splitTextByVerses(text,false);

        String pptFile = OUTPUT_FOLDER + pptTitle;

        createNewPpt(updateSplit, pptFile, false, optionInputDTO);

        InputStream fileInputStream = new FileInputStream(pptFile);
        return fileInputStream.readAllBytes();

    }

    private List<String> splitTextByVerses(String text, boolean lowerThird) {
        // for windows case
        text = text.trim().replaceAll("\\r","");
        String[] splitText = text.split("(\n\n[\n]*)");
        int firstIndexChorus = -1;
        int lastIndexChorus = -1;
        boolean multipleChorus = false;

        int lengthSplitText = splitText.length;
        for (int i = 0; i < lengthSplitText; i++) {
            if (splitText[i].startsWith(R)||
                    splitText[i].startsWith(R_1)||
                    splitText[i].startsWith(R_2)) { // the following verse is a chorus
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
            lastIndexChorus = lengthSplitText - 1;
        }

        List<String> listOfVerses = new ArrayList<>(Arrays.asList(splitText).subList(0, lastIndexChorus + 1));

        for (int i = lastIndexChorus + 1; i < lengthSplitText; i++) {
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

                if (verse.startsWith(R)) {
                    verse = verse.substring(2);
                    prefix = R;
                }

                String[] lines = verse.split(NEW_LINE);

                int length = lines.length;
                for (int index = 0; index+1 < length; index+=2)
                    listOfSubtitles.add(prefix + lines[index] + NEW_LINE + lines[index+1]);

                if (length % 2 == 1)
                    listOfSubtitles.add(prefix + lines[length -1]);
            }

            return listOfSubtitles;
        }

        return listOfVerses;
    }

    private void createNewPpt(List<String> updateSplit, String fileName, boolean lowerThird, OptionInputDTO optionInputDTO) throws IOException {

        XMLSlideShow ppt = new XMLSlideShow();
        int width = Integer.parseInt(optionInputDTO.getWidth());
        int height = Integer.parseInt(optionInputDTO.getHeight());
        ppt.setPageSize(new Dimension(width, height));

        XSLFSlideMaster defaultMaster = ppt.getSlideMasters().get(0);
        Color backgroundColor =  Color.decode(optionInputDTO.getBackgroundColor());
        defaultMaster.getBackground().setFillColor(backgroundColor);

        XSLFSlideLayout layout = defaultMaster.getLayout(SlideLayout.BLANK);

        Color colorFont = Color.decode(optionInputDTO.getTextColor());
        Color colorOutline = Color.decode(optionInputDTO.getOutlineColor());
        Color colorGlow = Color.decode(optionInputDTO.getGlowColor());

        int length = updateSplit.size();
        if (length > 0) {

            for (String s : updateSplit) {
                addOptionToSlide(s, new Rectangle(width, height), ppt.createSlide(layout), lowerThird, colorFont, Double.parseDouble(optionInputDTO.getTextSize()), Double.parseDouble(optionInputDTO.getOutlineValue()), colorOutline, colorGlow, Double.parseDouble(optionInputDTO.getGlowValue()));
            }

        }

        FileOutputStream out = new FileOutputStream(fileName);
        ppt.write(out);
        ppt.close();
        out.close();
    }

    private void addOptionToSlide(String s, Rectangle anchor, XSLFSlide slide1, boolean lowerThird, Color color, double fontSize, double outlineWeight, Color outlineColor, Color glowColor, double radiusPt) {
        XSLFTextBox shape = slide1.createTextBox();
        shape.setAnchor(anchor);

        // remove the other text paragraphs that were created
        if (!shape.getTextParagraphs().isEmpty()) {
            shape.clearText();
        }

        XSLFTextParagraph p = shape.addNewTextParagraph();

        shape.setVerticalAlignment(lowerThird ? VerticalAlignment.BOTTOM : VerticalAlignment.MIDDLE);
        p.setTextAlign(TextParagraph.TextAlign.CENTER);
        p.setFontAlign(TextParagraph.FontAlign.CENTER);
        XSLFTextRun r = p.addNewTextRun();

        if (s.trim().startsWith(R)) {
            r.setItalic(true);
            s = s.trim().substring(2);
        }

        r.setText(s.trim());
        r.setFontColor(color);
        r.setFontSize(fontSize);
        r.setBold(true);
        setOutlineAndGlow(r, createSolidFillLineProperties(outlineColor, outlineWeight), createGlow(glowColor, radiusPt));

        r.setFontFamily(CALIBRI, FontGroup.LATIN); // or CALIBRI_LIGHT
    }

}
