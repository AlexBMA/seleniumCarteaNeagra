package application.services;

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

    public byte[] createPPTFile(MultipartFile multipartFile, int width,int height) throws IOException {

        String name = multipartFile.getOriginalFilename();
        System.err.println(name);

        String pptTitle = name.replace(TXT_EXTENSION, PPTX_EXTENSION);

        System.err.println(pptTitle);

        byte[] allBytes = multipartFile.getInputStream().readAllBytes();
        String text = new String(allBytes, StandardCharsets.UTF_8);

        List<String> updateSplit = splitTextByVerses(text,false);

        String pptFile = OUTPUT_FOLDER + pptTitle;

        createNewPpt(updateSplit, pptFile, false, width, height);

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
            if (splitText[i].startsWith(R)) { // the following verse is a chorus
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

    private void createNewPpt(List<String> updateSplit, String fileName, boolean lowerThird, int width, int height) throws IOException {
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        double width = screenSize.getWidth();
//        double height = screenSize.getHeight();

        XMLSlideShow ppt = new XMLSlideShow();
        ppt.setPageSize(new Dimension(width, height));

        XSLFSlideMaster defaultMaster = ppt.getSlideMasters().get(0);
        defaultMaster.getBackground().setFillColor(new Color(RED, GREEN, BLUE));

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

    private void addOptionToSlide(String s, Rectangle anchor, XSLFSlide slide1, boolean lowerThird) {
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
        r.setFontColor(Color.WHITE);
        r.setFontSize(FONT_SIZE);
        r.setBold(true);
        setOutlineAndGlow(r, createSolidFillLineProperties(java.awt.Color.BLACK, OUTLINE_WEIGHT), createGlow(java.awt.Color.BLACK, RADIUS_PT));

        r.setFontFamily(CALIBRI, FontGroup.LATIN); // or CALIBRI_LIGHT
    }

}
