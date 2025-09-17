package main;

import clase.ElementExcellCantec;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static main.SeleniumResurseCrestine.*;

public class ExcellSongCreator {

    public static final String NR_CANTARE = "Nr cantare";
    public static final String TITLU_CANTARE = "Titlu cantare";
    public static final String TEXT_CANTARE = "Text cantare";
    public static final String TEXT_LINK = "Text link";
    public static final String VIDEO_LINK = "Video Link";
    public static final String MENTIUNE_1 = "Mentiune 1";
    public static final String MENTIUNE_2 = "Mentiune 2";
    public static final String CALIBRI = "Calibri";
    public static final String CANTECE = "cantece";
    public static final String ACORDURI = "acorduri";
    public static final String RESURSECRESTINE = "resursecrestine";
    public static final String HTTP = "http";
    public static final String YOUTU = "youtu";

    public static void main(String[] args) throws IOException {

        initSelenium();

        String filePath ="C:\\Users\\Alex\\Downloads\\Sondaj cantari (răspunsuri).xlsx";
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);

        Sheet sheet = workbook.getSheetAt(0);

        List<ElementExcellCantec> toateCantecele = new LinkedList<>();
        //1,4,7,10,13,16,19,22,25,28 - titlu
        //int[] nameIndex = {1,4,7,10,13,16,19,22,25,28};
        //2,5,8,11,14,17,20,23,26,29 - versuri text
        //3,6,9,12,15,18,21,24,27,30 - link youtube


        extractedCantece(sheet, toateCantecele);

        int size = toateCantecele.size();
        System.out.println(size);


        scrieRez(toateCantecele, sheet, filePath, workbook);

    }

    private static void scrieRez(List<ElementExcellCantec> toateCantecele, Sheet sheet, String filePath, Workbook workbook) {
        Workbook workbookRez = new XSSFWorkbook();
        Sheet sheetRez = workbookRez.createSheet("Rezultate");


        CellStyle style = creazaStilAntet(workbookRez);
        creazaAntet(sheetRez,style);


        int rowNum = 1;
        for(ElementExcellCantec e: toateCantecele){
            if(e.getTitlu()!=null && !e.getTitlu().isEmpty()){
                Row row = sheetRez.createRow(rowNum++);
                row.createCell(0).setCellValue(rowNum-1);
                row.createCell(1).setCellValue(e.getTitlu());
                row.createCell(2).setCellValue(e.getVersuri());
                row.createCell(3).setCellValue(e.getLinkVersuri());
                row.createCell(4).setCellValue(e.getLinkVideo());
                row.createCell(5).setCellValue(e.getMentiune());
                row.createCell(6).setCellValue(e.getMentiune());
            }

        }

        for(int i=0;i<7;i++){
            sheetRez.autoSizeColumn(i);
        }

        try (FileOutputStream fos = new FileOutputStream("FormularCantec11.xlsx")) {
            workbookRez.write(fos);
            workbookRez.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static CellStyle creazaStilAntet(Workbook workbookRez) {
        Font font = workbookRez.createFont();
        font.setBold(true);
        font.setItalic(false);
        font.setFontName(CALIBRI);
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setFontHeightInPoints((short) 14);

        CellStyle style = workbookRez.createCellStyle();
        style.setFont(font);
        return style;
    }

    private static void creazaAntet(Sheet sheetRez, CellStyle cellStyle) {
        Row header = sheetRez.createRow(0);
        Cell c1 = header.createCell(0);
        c1.setCellValue(NR_CANTARE);
        c1.setCellStyle(cellStyle);
        Cell c2 = header.createCell(1);
        c2.setCellValue(TITLU_CANTARE);
        c2.setCellStyle(cellStyle);
        Cell c3 = header.createCell(2);
        c3.setCellValue(TEXT_CANTARE);
        c3.setCellStyle(cellStyle);
        Cell c4 = header.createCell(3);
        c4.setCellValue(TEXT_LINK);
        c4.setCellStyle(cellStyle);
        Cell c5 = header.createCell(4);
        c5.setCellValue(VIDEO_LINK);
        c5.setCellStyle(cellStyle);
        Cell c6 = header.createCell(5);
        c6.setCellValue(MENTIUNE_1);
        c6.setCellStyle(cellStyle);
        Cell c7 = header.createCell(6);
        c7.setCellStyle(cellStyle);
        c7.setCellValue(MENTIUNE_2);
    }


    private static void extractedCantece(Sheet sheet, List<ElementExcellCantec> toateCantecele) {
        Set<String> dublicat = new LinkedHashSet<>();
        for (int cellNr = 1; cellNr<=30; cellNr=cellNr+3){
            ElementExcellCantec elementExcellCantec = null;

            for(Row row: sheet){
                if(row.getRowNum()!=0) {
                    elementExcellCantec = new ElementExcellCantec();

                    Cell cellTitle = row.getCell(cellNr);

                    if (cellTitle != null && cellTitle.getCellType() == CellType.STRING) {
                        String valoare = cellTitle.getStringCellValue().toUpperCase();

                        if(!dublicat.contains(valoare)){
                            elementExcellCantec.setTitlu(valoare);
                            dublicat.add(valoare);
                        }else{
                            System.out.println("dublicat");
                        }

                    }

                    Cell cellText = row.getCell(cellNr + 1);

                    if (cellText != null && cellText.getCellType() == CellType.STRING && elementExcellCantec.getTitlu()!=null) {
                        String valoare = cellText.getStringCellValue();

                        if (valoare.contains(HTTP)) {
                            elementExcellCantec.setLinkVersuri(valoare);
                            if (valoare.contains(RESURSECRESTINE)&& valoare.contains(CANTECE)) {
                                System.out.println(valoare);
                                String rezultatResurse = getSongTextFromResurseCrestine(valoare);
                                elementExcellCantec.setVersuri(rezultatResurse);
                            }
                            if(valoare.contains(RESURSECRESTINE)&& valoare.contains(ACORDURI)){
                                System.out.println(valoare);
                                String rezultatResurse = getSongTextFromAccordPage(valoare);
                                elementExcellCantec.setVersuri(rezultatResurse);
                            }
                            if(valoare.contains(YOUTU)){
                                System.out.println(valoare);
                                elementExcellCantec.setLinkVideo(valoare);
                            }
                        }
                        else {
                            elementExcellCantec.setVersuri(valoare);
                        }
                    }

                    Cell cellVideo = row.getCell(cellNr + 2);

                    if (cellVideo != null && cellVideo.getCellType() == CellType.STRING && elementExcellCantec.getTitlu()!=null) {
                        String valoare = cellVideo.getStringCellValue();
                        elementExcellCantec.setLinkVideo(valoare);
                    }

                    toateCantecele.add(elementExcellCantec);
                }

            }

        }

        closeSelenium();
    }
}
