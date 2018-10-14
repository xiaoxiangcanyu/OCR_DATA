package HttpUtil;

import DataClean.DataDO;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static HttpUtil.Singapore.getDataDOS;

public class ETLCombine {
    public static void main(String[] args){
//        String companycode="62T0-HQ";
//        String ETL_path ="C:\\Users\\songyu\\Desktop\\singapore\\62T0\\ETL_file.xls";
//        String SAP_Path="C:\\Users\\songyu\\Desktop\\singapore\\62T0\\SAP_file.xls";
        String companycode=args[0];
        String ETL_path =args[1];
        String SAP_Path=args[2];
        File EIL_file =new File(ETL_path);
        Date OrderShipmentDate=new Date();
        Date ActualShipmentDate=new Date();
        String OrderShipmentDate_final="";
        String ActualShipmentDate_final="";
        try {
            FileInputStream EIL_file_IO = new FileInputStream(EIL_file);
            try {
                Workbook xssfSheets_ETL=null;
                try {
                    xssfSheets_ETL = WorkbookFactory.create(EIL_file_IO);
                } catch (InvalidFormatException e) {
                    e.printStackTrace();
                }
                Sheet sheet_ETL =xssfSheets_ETL.getSheetAt(0);
                List<DataDO> dataDO_ETL = new ArrayList<>();
                ETLCombine etlCombine =new ETLCombine();
                Date InvoiceDate= new Date();
                String FileName="";
                for (int r = 1; r <= sheet_ETL.getLastRowNum(); r++) {
                    Row row=sheet_ETL.getRow(r);
                    DataDO dataDO =new DataDO();
                    try {
                        row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                    }catch (Exception e){

                    }
                    try {
                        row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                    }catch (Exception e){

                    }
                    try {
                        row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
                    }catch (Exception e){

                    }
//
                    try {
                        row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
                    }catch (Exception e){

                    }
                    try {
                        row.getCell(7).setCellType(Cell.CELL_TYPE_STRING);
                    }catch (Exception e){

                    }
                    try {
                        row.getCell(8).setCellType(Cell.CELL_TYPE_STRING);
                    }catch (Exception e){

                    }
                    try {
                        row.getCell(9).setCellType(Cell.CELL_TYPE_STRING);
                    }catch (Exception e){

                    }
                    try {
                        row.getCell(11).setCellType(Cell.CELL_TYPE_STRING);
                    }catch (Exception e){

                    }
                    try {
                        row.getCell(12).setCellType(Cell.CELL_TYPE_STRING);
                    }catch (Exception e){

                    }
                    try {
                        row.getCell(13).setCellType(Cell.CELL_TYPE_STRING);
                    }catch (Exception e){

                    }
                    try {
                        row.getCell(14).setCellType(Cell.CELL_TYPE_STRING);
                    }catch (Exception e){

                    }
                    try {
                        row.getCell(15).setCellType(Cell.CELL_TYPE_STRING);
                    }catch (Exception e){

                    }
                    try {
                        row.getCell(16).setCellType(Cell.CELL_TYPE_STRING);
                    }catch (Exception e){

                    }
//                    try {
//                        row.getCell(17).setCellType(Cell.CELL_TYPE_STRING);
//                    }catch (Exception e){
//                    }
//                    try {
//                        row.getCell(18).setCellType(Cell.CELL_TYPE_STRING);
//                    }catch (Exception e){
//                    }


                     FileName =row.getCell(0).getStringCellValue();
                    String CompanyCode =row.getCell(2).getStringCellValue();
                    System.out.println("在读表的时候companycode是"+CompanyCode);
                    String Amount =""+row.getCell(3).getStringCellValue();
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MM/dd/YYYY");
                    String InvoiceDate_final="";
                    try {
                        InvoiceDate =row.getCell(4).getDateCellValue();
                        InvoiceDate_final=simpleDateFormat.format(InvoiceDate);
                    }catch (Exception e){
                        try {
                        row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
                            InvoiceDate_final=row.getCell(4).getStringCellValue();
                    }catch (Exception a){

                    }
                    }

                    System.out.println("在读表的时候InvoiceDate是"+InvoiceDate_final);
                    String InvoiceReferenceNumber =row.getCell(5).getStringCellValue();
                    String InvoiceReferenceNumber2="";
                    try {
                         InvoiceReferenceNumber2 =row.getCell(6).getStringCellValue();
                    }catch (NullPointerException e){
                         InvoiceReferenceNumber2="";
                    }
                    String POShortText =row.getCell(7).getStringCellValue();
                    String PurchaseOrderNumber =row.getCell(8).getStringCellValue();
                    System.out.println("在读表的时候PurchaseOrderNumber是"+PurchaseOrderNumber);
                    String Quantity =""+row.getCell(9).getStringCellValue();
                    String TaxAmount="";
                    try {
                         TaxAmount =""+row.getCell(10).getStringCellValue();
                    }catch (Exception e){
                        TaxAmount ="";
                    }

                    String TotalAmount =""+row.getCell(11).getStringCellValue();
                    String UnitPrice =""+row.getCell(12).getStringCellValue();
                    String Currency =""+row.getCell(13).getStringCellValue();
                    String SONumber =""+row.getCell(14).getStringCellValue();
                    String GoodsDescription =""+row.getCell(15).getStringCellValue();
                    try {
                        OrderShipmentDate=row.getCell(17).getDateCellValue();
                        OrderShipmentDate_final=simpleDateFormat.format(OrderShipmentDate);
                    }catch (Exception e){
                        try {
                            row.getCell(17).setCellType(Cell.CELL_TYPE_STRING);
                            OrderShipmentDate_final=row.getCell(17).getStringCellValue();
                        }catch (Exception a){

                        }
                    }
                    try {
                        ActualShipmentDate=row.getCell(18).getDateCellValue();
                        ActualShipmentDate_final=simpleDateFormat.format(ActualShipmentDate);
                    }catch (Exception e){
                        try {
                            row.getCell(18).setCellType(Cell.CELL_TYPE_STRING);
                            ActualShipmentDate_final=row.getCell(18).getStringCellValue();
                        }catch (Exception a){

                        }
                    }

                    dataDO.setFilepath(FileName);
                    System.out.println("在设定的时候companycode是"+CompanyCode);
                    dataDO.setCompanyCode(CompanyCode);
                    dataDO.setAmount(Amount);
                    dataDO.setInvoicedate(InvoiceDate_final);
                    System.out.println("在设定的时候InvoiceDate是"+InvoiceDate_final);
                    dataDO.setInvoiceReferenceNumber(InvoiceReferenceNumber);
                    dataDO.setInvoiceReferenceNumber2(InvoiceReferenceNumber2);
                    dataDO.setPoshorttext(POShortText);
                    dataDO.setPurchaseOrderNumber(PurchaseOrderNumber);
                    dataDO.setQuantity(Quantity);
                    dataDO.setTaxAmount(TaxAmount);
                    dataDO.setTotalAmount(TotalAmount);
                    dataDO.setUnitPrice(UnitPrice);
                    dataDO.setSOnumber(SONumber);
                    dataDO.setCurrency(Currency);
                    dataDO.setGoodDescription(GoodsDescription);
                    dataDO_ETL.add(dataDO);
                    System.out.println("companycode是"+dataDO.getCompanyCode());

                }
                    if (OrderShipmentDate.equals("")){
                        System.out.println("字段缺失异常！！！！！！！！！！！");
                        etlCombine.excelOutput_Exception_ETL(dataDO_ETL,ETL_path,FileName,OrderShipmentDate_final,ActualShipmentDate_final);
                    }else {
                        System.out.println("invoicedate的结果是"+OrderShipmentDate_final+"\n");
                        dataDO_ETL=  etlCombine.invoiceVerification(dataDO_ETL);
                        etlCombine.excelOutput(dataDO_ETL,SAP_Path,FileName,2,companycode,OrderShipmentDate_final,ActualShipmentDate_final);
                    }



            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    public void excelOutput(List<DataDO> dataDOS,String filename,String filePath,int index,String comapnycode,String OrderShipmentDate,String ActualShipmentDate){
        XSSFWorkbook xssfWorkbook =new XSSFWorkbook();
        XSSFSheet xssfSheet=xssfWorkbook.createSheet("Sheet1");
        String[] headers=new String[]{};
        headers = new String[]{"FileName","CompanyCode", "Amount", "InvoiceDate", "InvoiceReferenceNumber","InvoiceReferenceNumber2", "POShortText", "PurchaseOrderNumber", "Quantity", "TaxAmount", "TotalAmount", "UnitPrice","Currency","SONumber","GoodsDescription","OCRStatus","PostingDate", "TaxCode", "Text", "BaselineDate", "ExchangeRate", "PaymentBlock", "Assignment","HeaderText"};
        Row row0=xssfSheet.createRow(0);
        for(int i=0;i<headers.length;i++){
            XSSFCell cell = (XSSFCell) row0.createCell(i);
            XSSFRichTextString text = new XSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        int rowNum = 1;
        for (DataDO dataDO:dataDOS){
            XSSFRow row1 = xssfSheet.createRow(rowNum);
                row1.createCell(0).setCellType(Cell.CELL_TYPE_STRING);
                row1.createCell(1).setCellType(Cell.CELL_TYPE_STRING);
                row1.createCell(2).setCellType(Cell.CELL_TYPE_STRING);
                row1.createCell(3).setCellType(Cell.CELL_TYPE_STRING);
                row1.createCell(4).setCellType(Cell.CELL_TYPE_STRING);
                row1.createCell(5).setCellType(Cell.CELL_TYPE_STRING);
                row1.createCell(6).setCellType(Cell.CELL_TYPE_STRING);
                row1.createCell(7).setCellType(Cell.CELL_TYPE_STRING);
                row1.createCell(8).setCellType(Cell.CELL_TYPE_STRING);
                row1.createCell(9).setCellType(Cell.CELL_TYPE_STRING);
                row1.createCell(10).setCellType(Cell.CELL_TYPE_STRING);
                row1.createCell(11).setCellType(Cell.CELL_TYPE_STRING);
                row1.createCell(12).setCellType(Cell.CELL_TYPE_STRING);
                row1.createCell(13).setCellType(Cell.CELL_TYPE_STRING);
                row1.createCell(14).setCellType(Cell.CELL_TYPE_STRING);
                row1.createCell(15).setCellType(Cell.CELL_TYPE_STRING);
                row1.createCell(16).setCellType(Cell.CELL_TYPE_STRING);
                row1.createCell(17).setCellType(Cell.CELL_TYPE_STRING);
                row1.createCell(18).setCellType(Cell.CELL_TYPE_STRING);
                row1.createCell(19).setCellType(Cell.CELL_TYPE_STRING);
                row1.createCell(20).setCellType(Cell.CELL_TYPE_STRING);
                row1.createCell(21).setCellType(Cell.CELL_TYPE_STRING);
                row1.createCell(22).setCellType(Cell.CELL_TYPE_STRING);
                row1.createCell(23).setCellType(Cell.CELL_TYPE_STRING);
                row1.getCell(0).setCellValue(filePath);
                row1.getCell(1).setCellValue(dataDO.getCompanyCode());
                row1.getCell(2).setCellValue(dataDO.getAmount().replace("\n","")+"".toString());
                row1.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
                System.out.println("amount的值是"+ row1.getCell(2)+"\n");
                row1.getCell(3).setCellValue(OrderShipmentDate);
                try {
                    row1.getCell(4).setCellValue(dataDO.getInvoiceReferenceNumber().replace("\n",""));
                }catch (NullPointerException e){
                    row1.getCell(4).setCellValue(dataDO.getInvoiceReferenceNumber());
                }
                try {
                    row1.getCell(5).setCellValue(dataDO.getInvoiceReferenceNumber2().replace("\n",""));
                }catch (NullPointerException e){
                    row1.getCell(5).setCellValue(dataDO.getInvoiceReferenceNumber2());
                }
                row1.getCell(6).setCellValue(dataDO.getPoshorttext().replace("\n",""));
                try {
                    row1.getCell(7).setCellValue(dataDO.getPurchaseOrderNumber().replace("\n",""));
                }catch (NullPointerException e){
                    row1.getCell(7).setCellValue(dataDO.getPurchaseOrderNumber());
                }
                row1.getCell(8).setCellValue(dataDO.getQuantity().replace("\n","").toString());
                row1.getCell(9).setCellValue(dataDO.getTaxAmount().replace("\n","").toString());
                row1.getCell(10).setCellValue(dataDO.getTotalAmount().replace("\n","")+"".toString());
                row1.getCell(11).setCellValue(dataDO.getUnitPrice().replace("\n","").toString());
                try {
                        row1.getCell(12).setCellValue(dataDO.getCurrency().replace("\n",""));
                }catch (Exception e){
                        row1.getCell(12).setCellValue(dataDO.getCurrency());

                    }

                    try {
                        row1.getCell(13).setCellValue(dataDO.getSOnumber().replace("\n",""));
                        row1.getCell(14).setCellValue(dataDO.getGoodDescription().replace("\n",""));
                    }catch (NullPointerException e){
                        row1.getCell(13).setCellValue(dataDO.getSOnumber());
                        row1.getCell(14).setCellValue(dataDO.getGoodDescription());
                    }


                row1.getCell(15).setCellValue("OK");
                try {
                    row1.getCell(16).setCellValue(dataDO.getPostingDate().replace("\n",""));
                }catch (Exception e){
                    row1.getCell(16).setCellValue(dataDO.getPostingDate());
                }
                row1.getCell(17).setCellValue(dataDO.getTaxCode());
                try {
                        row1.getCell(18).setCellValue(dataDO.getPurchaseOrderNumber().replace("\n",""));
                    }catch (Exception e){
                        row1.getCell(18).setCellValue(dataDO.getPurchaseOrderNumber());
                    }


            switch (dataDO.getCompanyCode()){
                case "62G0-HQ" :
                    System.out.println("再生成baselinedate的时候法国运行到这里号为");
                    row1.getCell(19).setCellValue(OrderShipmentDate);
                    System.out.println("已经生成完baselinedatet的时候号为"+ row1.getCell(19)+"\n");
                    break;
                case "62H0-HQ" :
                    row1.getCell(19).setCellValue(OrderShipmentDate);
                    break;
                case "62T0-HQ" :
                    row1.getCell(19).setCellValue(OrderShipmentDate);
                    break;
                case "6280-HQ" :
                    row1.getCell(19).setCellValue(OrderShipmentDate);
                    break;
                case "6200-HQ" :
                    row1.getCell(19).setCellValue(OrderShipmentDate);
                    break;
                case "62S0-HQ" :
                    row1.getCell(19).setCellValue(OrderShipmentDate);
                    break;
                case "6400-HQ" :
                    row1.getCell(19).setCellValue(OrderShipmentDate);
                    break;
                case "6550-HQ" :
                    row1.getCell(19).setCellValue(OrderShipmentDate);
                    break;
                case "6620-HQ" :
                    row1.getCell(19).setCellValue(OrderShipmentDate);
                    break;
                default:
                    row1.getCell(19).setCellValue(dataDO.getInvoicedate());

            }
            System.out.println("再次检验的时候baselinedatet的时候号为"+ row1.getCell(19)+"\n");
                row1.getCell(20).setCellValue("");

                    row1.getCell(21).setCellValue("");

                    switch (dataDO.getCompanyCode()){
                        case "62G0-HQ":
                            row1.getCell(22).setCellValue(dataDO.getPurchaseOrderNumber());
                        case "62H0-HQ":
                            row1.getCell(22).setCellValue(dataDO.getPurchaseOrderNumber());
                        case "62T0-HQ":
                            row1.getCell(22).setCellValue(dataDO.getPurchaseOrderNumber());
                        case "6280-HQ":
                            row1.getCell(22).setCellValue(dataDO.getPurchaseOrderNumber());
                        case "6200-HQ":
                            row1.getCell(22).setCellValue(dataDO.getPurchaseOrderNumber());
                        case "62S0-HQ":
                            row1.getCell(22).setCellValue(dataDO.getPurchaseOrderNumber());
                        case "6400-HQ":
                            row1.getCell(22).setCellValue(dataDO.getPurchaseOrderNumber());
                        case "6550-HQ":
                            row1.getCell(22).setCellValue(dataDO.getPurchaseOrderNumber());
                        case "65G0-HQ":
                            row1.getCell(22).setCellValue(dataDO.getPurchaseOrderNumber());
                        case "62F0-HQ":
                            row1.getCell(22).setCellValue(dataDO.getPurchaseOrderNumber());
                    }
                    System.out.println("再生成headtext的时候 po号为"+dataDO.getPurchaseOrderNumber());
                System.out.println("再生成headtext的时候 companycode号为"+dataDO.getCompanyCode());
            switch (dataDO.getCompanyCode()){
                case "62T0-HQ":
                    row1.getCell(23).setCellValue(dataDO.getPurchaseOrderNumber());
                case "6200-HQ":
                    row1.getCell(23).setCellValue(dataDO.getPurchaseOrderNumber());
                case "62S0-HQ":
                    row1.getCell(23).setCellValue(dataDO.getPurchaseOrderNumber());
                case "6400-HQ":
                    row1.getCell(23).setCellValue(dataDO.getPurchaseOrderNumber());
                case "6550-HQ":
                    row1.getCell(23).setCellValue(dataDO.getPurchaseOrderNumber());
                case "65G0-HQ":
                    row1.getCell(23).setCellValue(dataDO.getPurchaseOrderNumber());
            }

            rowNum++;
        }


        try {
            FileOutputStream fileOutputStream=new FileOutputStream(filename);

            try {
                xssfWorkbook.write(fileOutputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public List<DataDO> invoiceVerification(List<DataDO> dataDOS){
        return getDataDOS(dataDOS);
    }
    public void excelOutput_Exception_ETL(List<DataDO> dataDOS,String filename,String filePath,String OrderShipmentDate,String ActualShipmentDate){
        XSSFWorkbook xssfWorkbook =new XSSFWorkbook();
        XSSFSheet xssfSheet=xssfWorkbook.createSheet("Sheet1");
        String[] headers=new String[]{};
        headers = new String[]{"FileName","DownloadStatus","CompanyCode", "Amount", "InvoiceDate", "InvoiceReferenceNumber","InvoiceReferenceNumber2", "POShortText", "PurchaseOrderNumber", "Quantity", "TaxAmount", "TotalAmount", "UnitPrice","Currency","SONumber","GoodsDescription","OCRStatus","OrderShipmentDate","ActualShipmentDate"};


        Row row0=xssfSheet.createRow(0);
        for(int i=0;i<headers.length;i++){
            XSSFCell cell = (XSSFCell) row0.createCell(i);
            XSSFRichTextString text = new XSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        int rowNum = 1;
        for (DataDO dataDO:dataDOS){
            XSSFRow row1 = xssfSheet.createRow(rowNum);

            row1.createCell(0).setCellValue(filePath);
            row1.createCell(1).setCellValue("OK");
            row1.createCell(2).setCellValue(dataDO.getCompanyCode());
            row1.createCell(3).setCellValue(dataDO.getAmount());
            row1.createCell(4).setCellValue(OrderShipmentDate);
            row1.createCell(5).setCellValue(dataDO.getInvoiceReferenceNumber());
            row1.createCell(6).setCellValue(dataDO.getInvoiceReferenceNumber2());
            row1.createCell(7).setCellValue(dataDO.getPoshorttext());
            row1.createCell(8).setCellValue(dataDO.getPurchaseOrderNumber());
            row1.createCell(9).setCellValue(dataDO.getQuantity());
            row1.createCell(10).setCellValue(dataDO.getTaxAmount());
            row1.createCell(11).setCellValue(dataDO.getTotalAmount());
            row1.createCell(12).setCellValue(dataDO.getUnitPrice());
            row1.createCell(13).setCellValue(dataDO.getCurrency());
            row1.createCell(14).setCellValue(dataDO.getSOnumber());
            row1.createCell(15).setCellValue(dataDO.getGoodDescription());
            row1.createCell(16).setCellValue("字段缺失!");
            row1.createCell(17).setCellValue(OrderShipmentDate);
            row1.createCell(18).setCellValue(ActualShipmentDate);
            rowNum++;
        }


        try {
            FileOutputStream fileOutputStream=new FileOutputStream(filename);

            try {
                xssfWorkbook.write(fileOutputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
