//package DataClean;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//import com.alibaba.fastjson.JSON;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//public class DataClean {
//    public static void main(String args[]){
//        File file=new File("C:\\Users\\OCR_data\\09052018.xls");
//        DataClean dataClean=new DataClean();
//        List<DataDO> dataDOList= dataClean.readexcel(file);
////        System.out.print(JSON.toJSONString(dataDOList));
//        dataClean.dataclean(dataDOList);
//
//    }
//    public List<DataDO> readexcel(File file){
//        List<DataDO> dataDOS=new ArrayList<>();
//        try {
//            InputStream inputStream= new FileInputStream(file.getAbsoluteFile());
//            Workbook wb = null;
//            try {
//                wb = new XSSFWorkbook(inputStream);
//                Cell cell =null;
//                Sheet sheet = wb.getSheetAt(0);
//                Date date=new Date();
//
//                for( int i=1;i<sheet.getLastRowNum();i++){
//                            Row row=sheet.getRow(i);
//                            DataDO dataDO=new DataDO();
//                            String amount=row.getCell(0).getStringCellValue();
//                            String invoicedate=row.getCell(1).getStringCellValue();
//                            String invoiceReferenceNumber=row.getCell(2).getStringCellValue();
//                            String poshorttext=row.getCell(3).getStringCellValue();
//                            String purchaseOrderNumber=row.getCell(4).getStringCellValue();
//                            String quantity=row.getCell(5).getStringCellValue();
//                            String taxAmount=row.getCell(6).getStringCellValue();
//                            String totalAmount=row.getCell(7).getStringCellValue();
//                            String unitPrice=row.getCell(8).getStringCellValue();
//
//                          dataDO.setAmount(amount);
//                          dataDO.setInvoicedate(invoicedate);
//                          dataDO.setInvoiceReferenceNumber(invoiceReferenceNumber);
//                          dataDO.setPoshorttext(poshorttext);
//                          dataDO.setPurchaseOrderNumber(purchaseOrderNumber);
//                          dataDO.setQuantity(quantity);
//                          dataDO.setTaxAmount(taxAmount);
//                          dataDO.setTotalAmount(totalAmount);
//                          dataDO.setUnitPrice(unitPrice);
//                          dataDOS.add(dataDO);
//
//
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        return dataDOS;
//    }
//    public void dataclean(List<DataDO> dataDOS){
//        DataClean dataClean=new DataClean();
//      for(DataDO dataDO:dataDOS){
//          String amount=dataDO.getAmount();
//          String invoiceDate=dataDO.getInvoicedate();
//          String invoiceReferenceNumber=dataDO.getInvoiceReferenceNumber();
//          String poshorttext=dataDO.getPoshorttext();
//          String purchaseOrderNumber=dataDO.getPurchaseOrderNumber();
//          String quantity=dataDO.getQuantity();
//          String taxAmount=dataDO.getTaxAmount();
//          String totalAmount=dataDO.getTotalAmount();
//          String unitPrice=dataDO.getUnitPrice();
//         dataDO.setAmount(dataClean.cleanAmount(amount));
//         dataDO.setInvoicedate(dataClean.cleanInvoiceDate(invoiceDate));
//         dataDO.setInvoiceReferenceNumber(dataClean.cleanInvoiceReferenceNumber(invoiceReferenceNumber));
//         dataDO.setPoshorttext(poshorttext);
//         dataDO.setPurchaseOrderNumber(dataClean.cleanPurchaseOrderNumber(purchaseOrderNumber));
//         dataDO.setQuantity(dataClean.cleanQuantity(quantity));
//          dataDO.setTaxAmount(dataClean.cleanTaxAmount(taxAmount));
//          dataDO.setTotalAmount(dataClean.cleanTaxAmount(totalAmount));
//          dataDO.setUnitPrice(dataClean.cleanAmount(unitPrice));
//
//      }
//    }
//    public String cleanAmount(String amount){
//        DecimalFormat df=new DecimalFormat("0.00");
//        amount=amount.trim();
//        amount=amount.replace(",","");
//        amount=amount.replace(".","");
//        amount=amount.replace("S","5");
//        amount=amount.replace("I","1");
//        amount=amount.replaceAll("(?:I|!)","1");
//        amount=amount.replace("(","1");
//        amount=amount.replace(")","1");
//        amount=amount.replace("（","1");
//        amount=amount.replace("）","1");
//        amount=amount.replace("&","8");
//        amount=amount.replace("o","0");
//        amount=amount.replace("O","0");
//        amount=amount.replace("（","1");
//        amount=amount.replace("）","1");
//        amount=amount.replace("|","1");
//        amount=amount.replace("\\","1");
//        amount=amount.replace("/","1");
//        amount=amount.replace("\n","");
//        amount=amount.replace("↓","1");
//        amount= amount.trim();
//        int amount_clean=Integer.parseInt(amount);
////    System.out.print(df.format((float)(amount_clean/100))+"\n");
//        return  amount;
//
//    }
//    public String cleanInvoiceDate(String invoiceDate){
//        DecimalFormat df=new DecimalFormat("0.00");
//        invoiceDate=invoiceDate.trim();
//        invoiceDate=invoiceDate.replace(",","");
//        invoiceDate=invoiceDate.replace(".","");
//        invoiceDate=invoiceDate.replace("S","5");
//        invoiceDate=invoiceDate.replace("I","1");
//        invoiceDate=invoiceDate.replaceAll("(?:I|!)","1");
//        invoiceDate=invoiceDate.replace("(","1");
//        invoiceDate=invoiceDate.replace(")","1");
//        invoiceDate=invoiceDate.replace("（","1");
//        invoiceDate=invoiceDate.replace("）","1");
//        invoiceDate=invoiceDate.replace("&","8");
//        invoiceDate=invoiceDate.replace("o","0");
//        invoiceDate=invoiceDate.replace("O","0");
//        invoiceDate=invoiceDate.replace("（","1");
//        invoiceDate=invoiceDate.replace("）","1");
//        invoiceDate=invoiceDate.replace("|","1");
//        invoiceDate=invoiceDate.replace("\\","1");
//        invoiceDate=invoiceDate.replace("/","1");
//        invoiceDate=invoiceDate.replace("\n","");
//        invoiceDate=invoiceDate.replace("↓","1");
//        invoiceDate=invoiceDate.trim();
//        String invoiceDate_month=invoiceDate.substring(2,4);
//        String invoiceDate_clean;
//        if (Integer.parseInt(invoiceDate_month)>=1 && Integer.parseInt(invoiceDate_month)<=11){
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(new Date());
//            String year = String.valueOf(calendar.get(Calendar.YEAR));
//            invoiceDate_clean=invoiceDate.substring(0,4)+year;
//        }else {
//            invoiceDate_clean=invoiceDate;
//        }
//            invoiceDate_clean=invoiceDate_clean.substring(0,2)+"."+invoiceDate_clean.substring(2,4)+"."+invoiceDate_clean.substring(4,8);
////        System.out.print(invoiceDate_clean+"\n");
//        invoiceDate_clean=invoiceDate_clean.replace("\n","");
//        return  invoiceDate_clean;
//    }
//    public String cleanInvoiceReferenceNumber(String invoiceReferenceNumber){
//        DecimalFormat df=new DecimalFormat("0.00");
//        invoiceReferenceNumber=invoiceReferenceNumber.trim();
//        invoiceReferenceNumber=invoiceReferenceNumber.replace(",","");
//        invoiceReferenceNumber=invoiceReferenceNumber.replace(".","");
//        invoiceReferenceNumber=invoiceReferenceNumber.replace("S","5");
//        invoiceReferenceNumber=invoiceReferenceNumber.replace("I","1");
//        invoiceReferenceNumber=invoiceReferenceNumber.replaceAll("(?:I|!)","1");
//        invoiceReferenceNumber=invoiceReferenceNumber.replace("(","1");
//        invoiceReferenceNumber=invoiceReferenceNumber.replace(")","1");
//        invoiceReferenceNumber=invoiceReferenceNumber.replace("（","1");
//        invoiceReferenceNumber=invoiceReferenceNumber.replace("）","1");
//        invoiceReferenceNumber=invoiceReferenceNumber.replace("&","8");
//        invoiceReferenceNumber=invoiceReferenceNumber.replace("o","0");
//        invoiceReferenceNumber=invoiceReferenceNumber.replace("O","0");
//        invoiceReferenceNumber=invoiceReferenceNumber.replace("（","1");
//        invoiceReferenceNumber=invoiceReferenceNumber.replace("）","1");
//        invoiceReferenceNumber=invoiceReferenceNumber.replace("|","1");
//        invoiceReferenceNumber=invoiceReferenceNumber.replace("\\","1");
//        invoiceReferenceNumber=invoiceReferenceNumber.replace("/","1");
//        invoiceReferenceNumber=invoiceReferenceNumber.replace("\n","");
//        invoiceReferenceNumber=invoiceReferenceNumber.replace("↓","1");
//        invoiceReferenceNumber=invoiceReferenceNumber.trim();
//        if (invoiceReferenceNumber.length()==9 && invoiceReferenceNumber.substring(0).equals("9")){
//            invoiceReferenceNumber="1"+invoiceReferenceNumber;
//        }
//        invoiceReferenceNumber=invoiceReferenceNumber.replace("\n","");
//        return  invoiceReferenceNumber;
//
//    }
//    public String cleanPurchaseOrderNumber(String purchaseOrderNumber){
//        DecimalFormat df=new DecimalFormat("0.00");
//        purchaseOrderNumber=purchaseOrderNumber.trim();
//        purchaseOrderNumber=purchaseOrderNumber.replace(",","");
//        purchaseOrderNumber=purchaseOrderNumber.replace(".","");
//        purchaseOrderNumber=purchaseOrderNumber.replace("S","5");
//        purchaseOrderNumber=purchaseOrderNumber.replace("I","1");
//        purchaseOrderNumber=purchaseOrderNumber.replaceAll("(?:I|!)","1");
//        purchaseOrderNumber=purchaseOrderNumber.replace("(","1");
//        purchaseOrderNumber=purchaseOrderNumber.replace(")","1");
//        purchaseOrderNumber=purchaseOrderNumber.replace("（","1");
//        purchaseOrderNumber=purchaseOrderNumber.replace("）","1");
//        purchaseOrderNumber=purchaseOrderNumber.replace("&","8");
//        purchaseOrderNumber=purchaseOrderNumber.replace("o","0");
//        purchaseOrderNumber=purchaseOrderNumber.replace("O","0");
//        purchaseOrderNumber=purchaseOrderNumber.replace("（","1");
//        purchaseOrderNumber=purchaseOrderNumber.replace("）","1");
//        purchaseOrderNumber=purchaseOrderNumber.replace("|","1");
//        purchaseOrderNumber=purchaseOrderNumber.replace("\\","1");
//        purchaseOrderNumber=purchaseOrderNumber.replace("/","1");
//        purchaseOrderNumber=purchaseOrderNumber.replace("\n","");
//        purchaseOrderNumber=purchaseOrderNumber.replace("↓","1");
//        purchaseOrderNumber=purchaseOrderNumber.trim();
//        boolean result2=purchaseOrderNumber.substring(0,10).matches("[0-9]+");
//        boolean result3=purchaseOrderNumber.substring(0,9).matches("[0-9]+");
//        String purchaseOrderNumber_clean="";
//        if(purchaseOrderNumber.contains("TH")){
//            boolean result=purchaseOrderNumber.substring(6).substring(0,10).matches("[0-9]+");
//            boolean result1=purchaseOrderNumber.substring(6).substring(0,9).matches("[0-9]+");
//            if (result==true && purchaseOrderNumber.substring(6).substring(0,2).equals("56")){
//                 purchaseOrderNumber_clean = purchaseOrderNumber.substring(6).substring(0,10);
//            } else if (result1==true && purchaseOrderNumber.substring(6).substring(0,1).equals("1")){
//                 purchaseOrderNumber_clean ="5"+ purchaseOrderNumber.substring(6);
//                purchaseOrderNumber_clean=purchaseOrderNumber_clean.substring(0,10);
//            }else {
//                System.out.print("Error!");
//            }
//        }else {
//            if (result2==true && purchaseOrderNumber.substring(0,2).equals("56")){
//                purchaseOrderNumber_clean = purchaseOrderNumber.substring(0,10);
//            }else if(result3==true && purchaseOrderNumber.substring(0,1).equals("1")){
//                purchaseOrderNumber_clean ="5"+ purchaseOrderNumber;
//                purchaseOrderNumber_clean=purchaseOrderNumber_clean.substring(0,10);
//            }
//        }
//         purchaseOrderNumber_clean=purchaseOrderNumber_clean.replace("\n","");
//        System.out.print(purchaseOrderNumber_clean+"\n");
//        return  purchaseOrderNumber_clean;
//
//    }
//    public String cleanQuantity(String quantity){
//        DecimalFormat df=new DecimalFormat("0.000");
//        quantity=quantity.trim();
//        quantity=quantity.replace(",","");
//        quantity=quantity.replace(".","");
//        quantity=quantity.replace("S","5");
//        quantity=quantity.replace("I","1");
//        quantity=quantity.replaceAll("(?:I|!)","1");
//        quantity=quantity.replace("(","1");
//        quantity=quantity.replace(")","1");
//        quantity=quantity.replace("（","1");
//        quantity=quantity.replace("）","1");
//        quantity=quantity.replace("&","8");
//        quantity=quantity.replace("o","0");
//        quantity=quantity.replace("O","0");
//        quantity=quantity.replace("（","1");
//        quantity=quantity.replace("）","1");
//        quantity=quantity.replace("|","1");
//        quantity=quantity.replace("\\","1");
//        quantity=quantity.replace("/","1");
//        quantity=quantity.replace("\n","");
//        quantity=quantity.replace("↓","1");
//        quantity=quantity.trim();
//        int quantity_clean=Integer.parseInt(quantity);
//    System.out.print(df.format((float)(quantity_clean/100))+"\n");
//        return  df.format((float)(quantity_clean/100));
//
//    }
//    public String cleanTaxAmount(String totalAmount){
//        DecimalFormat df=new DecimalFormat("0.00");
//        totalAmount=totalAmount.trim();
//        String str1 = new StringBuilder(totalAmount).reverse().toString();
//        int index=str1.indexOf(",");
//        if (index==-1){
//            index=str1.indexOf("，");
//            if (index==-1){
//                index=str1.indexOf(".");
//            }
//        }
//        String str2=str1.substring(0,index);
//        str2=new StringBuilder(str2).reverse().toString();
//        if (str2.length()==3){
//            totalAmount=totalAmount.substring(0,totalAmount.indexOf(str2));
//        }
//        totalAmount=totalAmount.replace(",","");
//        totalAmount=totalAmount.replace(".","");
//        totalAmount=totalAmount.replace("S","5");
//        totalAmount=totalAmount.replace("I","1");
//        totalAmount=totalAmount.replaceAll("(?:I|!)","1");
//        totalAmount=totalAmount.replace("(","1");
//        totalAmount=totalAmount.replace(")","1");
//        totalAmount=totalAmount.replace("（","1");
//        totalAmount=totalAmount.replace("）","1");
//        totalAmount=totalAmount.replace("&","8");
//        totalAmount=totalAmount.replace("o","0");
//        totalAmount=totalAmount.replace("O","0");
//        totalAmount=totalAmount.replace("（","1");
//        totalAmount=totalAmount.replace("）","1");
//        totalAmount=totalAmount.replace("|","1");
//        totalAmount=totalAmount.replace("\\","1");
//        totalAmount=totalAmount.replace("/","1");
//        totalAmount=totalAmount.replace("\n","");
//        totalAmount=totalAmount.replace("↓","1");
//        totalAmount=totalAmount.trim();
//        int totalAmount_clean=Integer.parseInt(totalAmount);
//        System.out.print(df.format((float)(totalAmount_clean/100))+"\n");
//        return  df.format((float)(totalAmount_clean/100));
//
//    }
//
//}
