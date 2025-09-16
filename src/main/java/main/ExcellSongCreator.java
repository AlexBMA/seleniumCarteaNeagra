package main;

import clase.ElementExcellCantec;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ExcellSongCreator {

    public static void main(String[] args) throws IOException {

        String filePath ="C:\\Users\\Alex\\Downloads\\Sondaj cantari (răspunsuri).xlsx";
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);

        Sheet sheet = workbook.getSheetAt(0);

        //int cellNr = 1;

        List<ExcellSongCreator> list = new LinkedList<>();

        Set<Integer> nameIndex = new LinkedHashSet<>();
        nameIndex.add(1);
        nameIndex.add(4);
        nameIndex.add(7);
        nameIndex.add(10);
        nameIndex.add(13);
        nameIndex.add(16);
        nameIndex.add(19);
        nameIndex.add(22);
        nameIndex.add(25);
        nameIndex.add(28);

        List<ElementExcellCantec> toateCantecele = new LinkedList<>();
        //1,4,7,10,13,16,19,22,25,28 - titlu
        //int[] nameIndex = {1,4,7,10,13,16,19,22,25,28};
        //2,5,8,11,14,17,20,23,26,29 - versuri text
        //3,6,9,12,15,18,21,24,27,30 - link youtube


        extractedCantece(sheet, toateCantecele);

        String abcd = "";
        int size = toateCantecele.size();
        System.out.println(size);


        scrieRez(toateCantecele, sheet, filePath, workbook);

    }

    private static void scrieRez(List<ElementExcellCantec> toateCantecele, Sheet sheet, String filePath, Workbook workbook) {
        Workbook workbookRez = new XSSFWorkbook();
        Sheet sheetRez = workbookRez.createSheet("Rezultate");

        Row header = sheetRez.createRow(0);
        header.createCell(0).setCellValue("Nr cantare");
        header.createCell(1).setCellValue("Titlu cantare");
        header.createCell(2).setCellValue("Text cantare");
        header.createCell(3).setCellValue("Text link");
        header.createCell(4).setCellValue("Video Link");
        header.createCell(5).setCellValue("Mentiune 1");
        header.createCell(6).setCellValue("Mentiune 2");


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
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fos = new FileOutputStream("FormularCantec.xlsx")) {
            workbookRez.write(fos);
            workbookRez.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void extractedCantece(Sheet sheet, List<ElementExcellCantec> toateCantecele) {
        for (int cellNr = 1; cellNr<=30; cellNr=cellNr+3){
            ElementExcellCantec elementExcellCantec = null;

            for(Row row: sheet){
                if(row.getRowNum()!=0) {
                    elementExcellCantec = new ElementExcellCantec();

                    Cell cellTitle = row.getCell(cellNr);

                    if (cellTitle != null && cellTitle.getCellType() == CellType.STRING) {
                        String valoare = cellTitle.getStringCellValue();
                        elementExcellCantec.setTitlu(valoare);
                    }

                    Cell cellText = row.getCell(cellNr + 1);

                    if (cellText != null && cellText.getCellType() == CellType.STRING) {
                        String valoare = cellText.getStringCellValue();

                        if (valoare.contains("http")) elementExcellCantec.setLinkVersuri(valoare);
                        else elementExcellCantec.setVersuri(valoare);
                    }

                    Cell cellVideo = row.getCell(cellNr + 2);

                    if (cellVideo != null && cellVideo.getCellType() == CellType.STRING) {
                        String valoare = cellVideo.getStringCellValue();
                        elementExcellCantec.setLinkVideo(valoare);
                    }

                    toateCantecele.add(elementExcellCantec);
                }

            }

        }
    }
}
