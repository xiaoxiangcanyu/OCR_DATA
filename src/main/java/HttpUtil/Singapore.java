package HttpUtil;

import DataClean.DataDO;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Singapore {
    public static String ocrImageFile(String imageType,String fileName,byte[] fileData) throws Exception
    {
        String baseUrl = "http://10.138.93.103:8080/OcrServer/ocr/ocrImageByTemplate";
//        HttpGet get=new HttpGet(baseUrl);
        HttpPost post = new HttpPost(baseUrl);
        ContentType contentType = ContentType.create("multipart/form-data", Charset.forName("UTF-8"));

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(Charset.forName("UTF-8"));
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("file", fileData, contentType, fileName);// 文件流
        builder.addTextBody("imageType", imageType,contentType);// 类似浏览器表单提交，对应input的name和value

        HttpEntity entity = builder.build();
        post.setEntity(entity);
        // (3) 发送请求
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try
        {
            HttpResponse response = httpclient.execute(post);
            if(response.getStatusLine().getStatusCode() == 200)
            {
                String result = EntityUtils.toString(response.getEntity(),"utf-8");
                return result;
            } else
            {
                throw new Exception(EntityUtils.toString(response.getEntity(),"utf-8"));
            }
        }finally
        {
            httpclient.close();
        }
    }
    public static void main(String[] args) throws Exception
    {
        String companycode="62G0-HQ";
        String filepath= "C:\\Users\\songyu\\Desktop\\singapore\\62S0\\62S0.jpg";
        String OCR_filepath ="C:\\Users\\songyu\\Desktop\\singapore\\62S0\\OCR.xls";
        String SAP_filepath= "C:\\Users\\songyu\\Desktop\\singapore\\62S0\\ETL.xls";
//        String companycode=args[0];
//        String filepath=args[1];
//        String OCR_filepath =args[2];
//        String SAP_filepath= args[3];
        String filename =filepath.substring(filepath.lastIndexOf("\\")+1);
        File file = new File(filepath);
        byte[] fileData = FileUtils.readFileToByteArray(file);
        String json="";
        json = ocrImageFile("海尔各国从新加坡电产采购发票校验0909",file.getName(),fileData) ;
        com.alibaba.fastjson.JSONObject jsonObject= com.alibaba.fastjson.JSON.parseObject(json);
        String OCR_status = (String) (jsonObject.get("msg"));
        String OCR_result = (String) (jsonObject.get("result"));
        System.out.println("json"+json);
        Singapore singapore=new Singapore();
        if (OCR_result.contains("success")){
            JSONArray jsonArray =jsonObject.getJSONObject("ocrResult").getJSONArray("ranges");
            JSONArray jsonArray1 = jsonArray.getJSONObject(0).getJSONArray("rowDatas");
            System.out.println("jsonArray"+jsonArray);
            System.out.println("jsonArray1"+jsonArray1);
            int compare_num=0;
            for (int i2=0;i2<jsonArray1.size();i2++){
                ;
                String compare = com.alibaba.fastjson.JSON.toJSONString(jsonArray1.getJSONArray(i2).getJSONObject(2).get("value"));
                if (compare.contains("Total")){
                    compare_num = i2;
                    System.out.println(jsonArray1.getJSONArray(compare_num));
                }
            }
            String InvoiceReferenceNumber= (String)(jsonArray.getJSONObject(2).get("value"));
            String InvoiceDate=(String)(jsonArray.getJSONObject(3).get("value"));
            String totalAmount=(String) (jsonArray1.getJSONArray(compare_num).getJSONObject(3).get("value"));
            System.out.println("InvoiceReferenceNumber的值为："+InvoiceReferenceNumber+"\n");
            System.out.println("InvoiceDate的值为："+InvoiceDate+"\n");
            System.out.println("totalAmount的值为："+totalAmount+"\n");
            String taxAmount = "";
//            String PO=(String)(jsonArray.getJSONObject(1).get("value"));
//            int a = PO.indexOf("0003");
//            int b =PO.lastIndexOf("0003");
//            int c = PO.indexOf("45006");
//            int d =PO.lastIndexOf("45006");
////            String purchaseOrderNumber = PO.substring(c,d+10);
//            System.out.println("purchaseOrderNumber的值为："+purchaseOrderNumber+"\n");
//            String SOnumber = PO.substring(a,b+10);
//            System.out.println("SOnumber的值为："+SOnumber+"\n");
//            String GoodsDescription=(String)(jsonArray1.getJSONArray(0).getJSONObject(0).get("value"));
//            System.out.println("GoodsDescription的值为"+GoodsDescription+"\n");
            List<DataDO> dataDOList=new ArrayList<>();
            System.out.println("compare_num的值为"+compare_num+"\n");
            //判断图片的table读取行是否为0
            if (compare_num==0){
                totalAmount=(String) (jsonArray1.getJSONArray(2).getJSONObject(3).get("value"));
                String poshorttext = (String)(jsonArray1.getJSONArray(1).getJSONObject(0).get("value"));
                String Quantity=  (String)(jsonArray1.getJSONArray(1).getJSONObject(1).get("value"));
                String unitPrice=  (String)(jsonArray1.getJSONArray(1).getJSONObject(2).get("value"));
                String amount= (String) (jsonArray1.getJSONArray(1).getJSONObject(3).get("value"));
                System.out.println("poshorttext的值为："+poshorttext+"\n");
                System.out.println("Quantity的值为："+Quantity+"\n");
                System.out.println("unitPrice的值为："+unitPrice+"\n");
                System.out.println("amount的值为："+amount+"\n");
                DataDO dataDO = new DataDO();
                dataDO.setCompanyCode(companycode);
                dataDO.setAmount(amount);
                dataDO.setInvoicedate(InvoiceDate);
                dataDO.setInvoiceReferenceNumber(InvoiceReferenceNumber);
                dataDO.setInvoiceReferenceNumber2("");
                dataDO.setPoshorttext(poshorttext);
                dataDO.setPurchaseOrderNumber("");
                dataDO.setQuantity(Quantity);
                dataDO.setTotalAmount(totalAmount);
                dataDO.setUnitPrice(unitPrice);
                dataDO.setSOnumber("");
                dataDO.setTaxAmount(taxAmount);
                dataDO.setGoodDescription("");
                dataDO.setFilepath(filepath);
                if (dataDO.getAmount()==null){
                    dataDO.setCurrency("");
                }else {
                    dataDO.setCurrency(dataDO.getAmount().substring(0,3));
                }
                dataDO.setStatus("有字段OCR无法识别!");
                dataDOList.add(dataDO);


            }else {
                for (int i3=1;i3<compare_num;i3++){
                    System.out.println(jsonArray1.getJSONArray(i3+1)+"\n");
                    String poshorttext = (String)(jsonArray1.getJSONArray(i3).getJSONObject(0).get("value"));
                    String Quantity=  (String)(jsonArray1.getJSONArray(i3).getJSONObject(1).get("value"));
                    String unitPrice=  (String)(jsonArray1.getJSONArray(i3).getJSONObject(2).get("value"));
                    String amount= (String) (jsonArray1.getJSONArray(i3).getJSONObject(3).get("value"));
                    System.out.println("poshorttext的值为："+poshorttext+"\n");
                    System.out.println("Quantity的值为："+Quantity+"\n");
                    System.out.println("unitPrice的值为："+unitPrice+"\n");
                    System.out.println("amount的值为："+amount+"\n");
                    DataDO dataDO = new DataDO();
                    dataDO.setCompanyCode(companycode);
                    dataDO.setAmount(amount);
                    dataDO.setInvoicedate(InvoiceDate);
                    dataDO.setInvoiceReferenceNumber(InvoiceReferenceNumber);
                    dataDO.setInvoiceReferenceNumber2("");
                    dataDO.setPoshorttext(poshorttext);
                    dataDO.setPurchaseOrderNumber("");
                    dataDO.setQuantity(Quantity);
                    dataDO.setTotalAmount(totalAmount);
                    dataDO.setUnitPrice(unitPrice);
                    dataDO.setSOnumber("");
                    dataDO.setTaxAmount(taxAmount);
                    dataDO.setGoodDescription("");
                    dataDO.setFilepath(filepath);
                    dataDO.setStatus("OK");
                    try{
                        dataDO.setCurrency(dataDO.getAmount().substring(0,3));
                    }catch (Exception e){
                        dataDO.setCurrency("");
                    }
                    dataDOList.add(dataDO);

                }
            }
            //清理新加坡
            List<DataDO> dataDOList1=new ArrayList<>();
            String status="";
            String   Poshorttext_clean ="";
            int i=0;
            System.out.println("开始清理新加坡");
            for (DataDO dataDO:dataDOList){
                String Amount_clean=singapore.cleanAmount(dataDO).getAmount();
                String UnitPrice_clean=dataDO.getUnitPrice();
                String Quantity_clean=singapore.cleanQuantity(dataDO);
                String TotalAmount_clean=dataDO.getTotalAmount();
                String TaxAmount_clean=dataDO.getTaxAmount();
                if (dataDO.getPoshorttext()!=null){
                     Poshorttext_clean=dataDO.getPoshorttext().replace(" ","");
                }
                String InvoiceReferenceNumber_clean=dataDO.getInvoiceReferenceNumber();
                String InvoiceDate_clean=dataDO.getInvoicedate();
                System.out.println("Amount_clean："+Amount_clean+"\n");
                System.out.println("UnitPrice_clean："+UnitPrice_clean+"\n");
                System.out.println("Quantity_clean："+Quantity_clean+"\n");
                System.out.println("TaxAmount_clean："+TaxAmount_clean+"\n");
                System.out.println("TotalAmount_clean："+TotalAmount_clean+"\n");
                if (Amount_clean.equals("")||UnitPrice_clean.equals("")||Quantity_clean.equals("")){
                    if (i==0){
                        System.out.println("第一行的数据有误");
                        i++;
                        continue;
                    }else {
                        status="字段识别有误!";
                        dataDO.setStatus(status);
                    }
                }
                dataDO.setCompanyCode(companycode);
                dataDO.setTotalAmount(TotalAmount_clean);
                dataDO.setAmount(Amount_clean);
                dataDO.setTaxAmount(TaxAmount_clean);
                dataDO.setPurchaseOrderNumber(dataDO.getPurchaseOrderNumber());
                dataDO.setInvoiceReferenceNumber(InvoiceReferenceNumber_clean);
                dataDO.setUnitPrice(UnitPrice_clean);
                dataDO.setPoshorttext(Poshorttext_clean);
                dataDO.setQuantity(Quantity_clean);
                dataDO.setInvoicedate(InvoiceDate_clean);
                dataDOList1.add(dataDO);
                System.out.println("这是第"+i+"次循环"+"\n");
                i++;
            }

            dataDOList1 =singapore.invoiceVerification(dataDOList1);
            String ETL_filepath=SAP_filepath;
            singapore.excelOutput(dataDOList1,ETL_filepath,filename,1,companycode);

        }
        else {
            List<DataDO> dataDOList =new ArrayList<>();
            DataDO dataDO = new DataDO();
            dataDO.setStatus(OCR_status);
            dataDOList.add(dataDO);
            singapore.excelOutput(dataDOList,OCR_filepath,filename,0,companycode);
        }

    }
    public DataDO cleanAmount(DataDO dataDO){
        String amount = dataDO.getAmount();
        String price = dataDO.getUnitPrice();
        String amount_clean="";
        String taxAmount_clean="";
        String unitprice_clean="";
        String  totalamount_clean="";
        try {
            amount_clean = amount.trim();
            amount_clean = amount_clean.replace(",", "");
            amount_clean = amount_clean.replace(".", "");
            amount_clean = amount_clean.substring(3);
            amount_clean = amount_clean.substring(0, amount_clean.length() - 2) + "." + amount_clean.substring(amount_clean.length() - 2);

        }catch (Exception e){
            amount_clean="";
        }
        try {
            totalamount_clean =dataDO.getTotalAmount().replace("|","");
            totalamount_clean=totalamount_clean.substring(3);
            totalamount_clean=totalamount_clean.replace("\n","");
            Float totalamount_parse = Float.parseFloat(totalamount_clean);

        }catch (Exception e){
            totalamount_clean="";
        }
        try {
            unitprice_clean =price.replace("|","");
            unitprice_clean=unitprice_clean.substring(3);
            unitprice_clean=unitprice_clean.replace("\n","");
            Float unitprice_clean_parse = Float.parseFloat(unitprice_clean);
        }catch (Exception e){
            unitprice_clean="";
        }
        dataDO.setAmount(amount_clean);
        dataDO.setTaxAmount("");
        dataDO.setTotalAmount(totalamount_clean);
        dataDO.setUnitPrice(unitprice_clean);
        return  dataDO;

    }


    public String cleanQuantity(DataDO dataDO){
        DecimalFormat df=new DecimalFormat("0.000");
        String quantity =dataDO.getQuantity();
        String quantity_clean=quantity;
        try {
            quantity_clean=quantity_clean.trim();
            quantity_clean=quantity_clean.replace(",","");
            quantity_clean=quantity_clean.replace(".","");
            quantity_clean=quantity_clean.replace("I","1");
            quantity_clean=quantity_clean.replaceAll("(?:I|!)","1");
            quantity_clean=quantity_clean.replace("(","1");
            quantity_clean=quantity_clean.replace(")","1");
            quantity_clean=quantity_clean.replace("（","1");
            quantity_clean=quantity_clean.replace("）","1");
            quantity_clean=quantity_clean.replace("&","8");
            quantity_clean=quantity_clean.replace("o","0");
            quantity_clean=quantity_clean.replace("O","0");
            quantity_clean=quantity_clean.replace("（","1");
            quantity_clean=quantity_clean.replace("）","1");
            quantity_clean=quantity_clean.replace("|","1");
            quantity_clean=quantity_clean.replace("\\","1");
            quantity_clean=quantity_clean.replace("/","1");
            quantity_clean=quantity_clean.replace("\n","");
            quantity_clean=quantity_clean.replace("↓","1");
            quantity_clean=quantity_clean.replace("]","1");
            quantity_clean=quantity_clean.replace(" ","");
            quantity_clean=quantity_clean.trim();
            int S = quantity_clean.indexOf("S");
            quantity_clean=quantity_clean.substring(0,S);
            System.out.println("新加坡清洗之后的quantity是"+quantity_clean+"\n");
        }catch (Exception e){
            quantity_clean="";
        }
        return  quantity_clean;
    }

    public List<DataDO> invoiceVerification(List<DataDO> dataDOS){
        return getDataDOS(dataDOS);
    }

    public static List<DataDO> getDataDOS(List<DataDO> dataDOS) {
        Date date=new Date();
        DateFormat dFormat = new SimpleDateFormat("MM/dd/yyyy");
        String formatDate = null;
        formatDate=dFormat.format(date);
        List<DataDO> dataDOS1 =new ArrayList<>();
        for (DataDO dataDO:dataDOS){
            String companycode=dataDO.getCompanyCode();
            String invoicedate=dataDO.getInvoicedate().substring(0,2);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            String month = formatDate.substring(0,2);
            String year = String.valueOf(calendar.get(Calendar.YEAR));
            String previousfirstday = invoicedate+"/01"+"/"+year;
            String nowfirstday=month+"/01/"+year;
            if(companycode.equals("6430")){
                if (invoicedate.equals(month)){
                    dataDO.setPostingDate(formatDate);

                }else {
                    dataDO.setPostingDate(previousfirstday);
                }

            }else if(companycode.equals("62F0-FAC")||companycode.equals("62F0-HQ")){
                if (invoicedate.equals(month)){
                    dataDO.setPostingDate(dataDO.getInvoicedate());
                }else {
                    dataDO.setPostingDate(nowfirstday);
                }

            }else {
                dataDO.setPostingDate(formatDate);
            }
            if (companycode.equals("6550-HQ")){
                dataDO.setTaxCode("OP (Purchase in out of scope (e.g. overseas purchase))");
            }else if (companycode.equals("6560-FAC")){
                dataDO.setTaxCode("V7 (Input VAT Receivable 7%)");
            }else if (companycode.equals("6560-HQ")){
                dataDO.setTaxCode("V0 (Input VAT Receivable 0%)");
            }else if (companycode.equals("65G0-HQ")){
                dataDO.setTaxCode("I0 (Input Tax Zero Rated)");
            }else if (companycode.equals("6430-HQ")){
                dataDO.setTaxCode("X0 (no tax)");
            }else if (companycode.equals("62G0-HQ")){
                dataDO.setTaxCode("5E (I Acquisition Hors CE)");
            }else if (companycode.equals("62H0-HQ")){
                dataDO.setTaxCode("B4 (AK - IMP - Purchase - 0%)");
            }else if (companycode.equals("62T0-HQ")){
                dataDO.setTaxCode("3D (I Operaciones Extra Comunitarias 0%)");
            }else if (companycode.equals("6280-HQ")){
                dataDO.setTaxCode("W0 (0% VAT Input Tax)");
            }else if (companycode.equals("6200-HQ")){
                dataDO.setTaxCode("70 (FC IVA ART 7-bis, c1 DPR 633/72 -op non soggetta-)");
            }else if (companycode.equals("62S0-HQ")){
                dataDO.setTaxCode("16 (VAT 20% - Input tax)");
            }else if (companycode.equals("6620-HQ")){
                dataDO.setTaxCode("I0 (SALES TAX - INPUT(0%))");
            }else if (companycode.equals("6400-HQ")){
                dataDO.setTaxCode("W9 ()");
            }else if (companycode.equals("62F0-FAC")){
                dataDO.setTaxCode("P2 (18% Input Tax for Goods (Deferred Tax))");
            }else if (companycode.equals("62F0-HQ")){
                dataDO.setTaxCode("P0 (0% Input Tax for Goods,Services)");
            }
            dataDO.setText("Pengding");
            dataDO.setBaselineDate("Pending");
            dataDO.setExchangeRate("Pengding");
            dataDO.setBaselineDate("Pengding");
            dataDO.setPaymentBlock("Pengding");
            dataDO.setAssignment("Pengding");
            dataDO.setHeaderText("Pengding");
            dataDOS1.add(dataDO);
        }

        return dataDOS1;
    }

    public void excelOutput(List<DataDO> dataDOS,String filename,String filePath,int index,String comapnycode){
        XSSFWorkbook xssfWorkbook =new XSSFWorkbook();
        XSSFSheet xssfSheet=xssfWorkbook.createSheet("Sheet1");
        String[] headers=new String[]{};
        headers = new String[]{"FileName","DownloadStatus","CompanyCode", "Amount", "InvoiceDate", "InvoiceReferenceNumber","InvoiceReferenceNumber2", "POShortText", "PurchaseOrderNumber", "Quantity", "TaxAmount", "TotalAmount", "UnitPrice","Currency","SONumber","GoodsDescription","OCRStatus"};
        Row row0=xssfSheet.createRow(0);
        for(int i=0;i<headers.length;i++){
            XSSFCell cell = (XSSFCell) row0.createCell(i);
            XSSFRichTextString text = new XSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        int rowNum = 1;
        for (DataDO dataDO:dataDOS){
            XSSFRow row1 = xssfSheet.createRow(rowNum);
            if(index==0 ||index==1){
                row1.createCell(0).setCellValue(filePath);
                row1.createCell(1).setCellValue("OK");
                row1.createCell(2).setCellValue(dataDO.getCompanyCode());
                row1.createCell(3).setCellValue(dataDO.getAmount());
                row1.createCell(4).setCellValue(dataDO.getInvoicedate());
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
                row1.createCell(16).setCellValue(dataDO.getStatus());

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

}
