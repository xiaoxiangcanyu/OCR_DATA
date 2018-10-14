package HttpUtil;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import DataClean.DataDO;
import com.alibaba.fastjson.JSONArray;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;
import org.hibernate.validator.internal.constraintvalidators.hv.pl.PolishNumberValidator;
import org.im4java.core.ConvertCmd;
import org.im4java.core.GMOperation;
import org.im4java.core.IMOperation;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import static HttpUtil.Singapore.getDataDOS;


public class HttpUtil
{
	
	public static String ocrImageFile(String imageType,String fileName,byte[] fileData) throws Exception
    {
        String baseUrl = "http://10.138.93.103:8080/OcrServer/ocr/ocrImageByTemplate";
//        HttpGet get=new HttpGet(baseUrl);
        HttpPost post = new HttpPost(baseUrl);
        ContentType contentType = ContentType.create("multipart/form-data",Charset.forName("UTF-8"));

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
//	    String filepath= "C:\\Users\\songyu\\Desktop\\ImageMagick-7.0.8-Q16\\original\\62F0-FAC-90004146.pdf";
//        String OCR_filepath ="C:\\Users\\songyu\\Desktop\\OCR_Result\\OCR_file.xls";
//        String SAP_filepath= "C:\\Users\\songyu\\Desktop\\SAP_Result\\SAP_file.xls";
        String companycode=args[0];
        String filepath=args[1];
        String OCR_filepath =args[2];
        String SAP_filepath= args[3];
        String filename =filepath.substring(filepath.lastIndexOf("\\")+1);
       int position=filename.indexOf("-");
       if (!companycode.contains("62F0-FAC")&&!companycode.contains("6560-FAC")){
           companycode=filename.substring(0,position+3);
       }
       System.out.println(companycode);

        String ponumber ="";
//        int n=0;
//        if (filename.contains("HQ")){
//            n =filename.indexOf("-")+3;
//        }else {
//             n =filename.indexOf("-")+4;
//        }
        if(companycode.contains("62F0")){
            ponumber = companycode.substring(companycode.indexOf("_"));
            companycode=companycode.substring(0,companycode.indexOf("_"));
        }
        System.out.print("companycode是："+companycode);
        File file = new File(filepath);
        File file2 = new File(filepath);
        if (companycode.contains("62F0")){
            String outputPath =OCR_filepath.substring(0,OCR_filepath.lastIndexOf("."))+".jpg";
            filename="62F0-90004145.jpg";
            Runtime.getRuntime().exec("C:\\Users\\songyu\\Desktop\\ImageMagick-7.0.8-Q16\\convert -rotate 270 -density 200"+" "+filepath+" "+outputPath);
            String filepath1 = outputPath.substring(0,outputPath.lastIndexOf("."))+"-0"+".jpg";
            String filepath2 = outputPath.substring(0,outputPath.lastIndexOf("."))+"-1"+".jpg";
            file = new File(filepath1);
            file2 = new File(filepath2);

        }
	    byte[] fileData = FileUtils.readFileToByteArray(file);
	    HttpUtil httpUtil=new HttpUtil();
        String json="";
        String json2="";
        if (companycode.contains("6560-FAC")){
             json = ocrImageFile("haier",file.getName(),fileData) ;
        }else if (companycode.contains("62F0")){
            json = ocrImageFile("海尔俄罗斯采购发票校验模板一0911",file.getName(),fileData) ;
            byte[] fileData2 = FileUtils.readFileToByteArray(file2);
            json2 = ocrImageFile("海尔俄罗斯采购发票校验模板二0915",file2.getName(),fileData2) ;
        }else {
             json = ocrImageFile("海尔各国从新加坡电产采购发票校验0909",file.getName(),fileData) ;
        }
        com.alibaba.fastjson.JSONObject jsonObject= com.alibaba.fastjson.JSON.parseObject(json);
        String OCR_status = (String) (jsonObject.get("msg"));
        String OCR_result = (String) (jsonObject.get("result"));
        System.out.println("json"+json);

        if (OCR_result.contains("success")){
            JSONArray jsonArray =jsonObject.getJSONObject("ocrResult").getJSONArray("ranges");
            if (companycode.contains("6560-FAC")){
                String InvoiceReferenceNumber =(String) jsonArray.getJSONObject(0).get("value");
                String InvoiceDate =(String) jsonArray.getJSONObject(1).get("value");
                String TaxAmount =(String) jsonArray.getJSONObject(3).get("value");
                String TotalAmount =(String) jsonArray.getJSONObject(4).get("value");
                String PurchaseOrderNumber =(String) jsonArray.getJSONObject(5).get("value");
                List<DataDO> dataDOList = new ArrayList<>();
                for (int i1=0;i1<jsonArray.getJSONObject(2).getJSONArray("rowDatas").size();i1++){
                    System.out.print(jsonArray.getJSONObject(2).getJSONArray("rowDatas").get(i1)+"\n");
                    JSONArray jsonArray1=jsonArray.getJSONObject(2).getJSONArray("rowDatas").getJSONArray(i1);
                    String POShortText =(String) jsonArray1.getJSONObject(0).get("value");
                    String UnitPrice =(String)jsonArray1.getJSONObject(1).get("value");
                    String Quantity =(String)jsonArray1.getJSONObject(2).get("value");
                    String Amount =(String) jsonArray1.getJSONObject(3).get("value");
                    DataDO dataDO_original=new DataDO();
                    dataDO_original.setAmount(Amount);
                    dataDO_original.setInvoiceReferenceNumber(InvoiceReferenceNumber);
                    dataDO_original.setCompanyCode(companycode);
                    dataDO_original.setInvoicedate(InvoiceDate);
                    dataDO_original.setQuantity(Quantity);
                    dataDO_original.setPoshorttext(POShortText);
                    dataDO_original.setUnitPrice(UnitPrice);
                    dataDO_original.setPurchaseOrderNumber(PurchaseOrderNumber);
                    dataDO_original.setTaxAmount(TaxAmount);
                    dataDO_original.setTotalAmount(TotalAmount);
                    dataDOList.add(dataDO_original);
                }
                httpUtil.excelOutput(dataDOList,OCR_filepath,filename,1,companycode);
                List<DataDO> dataDOList1 = new ArrayList<>();
                for (DataDO dataDO:dataDOList){
                    String Amount_clean=httpUtil.cleanAmount(dataDO).getAmount();
                    String InvoiceReferenceNumber_clean=httpUtil.cleanInvoiceReferenceNumber(dataDO.getInvoiceReferenceNumber());
                    String InvoiceDate_clean=httpUtil.cleanInvoiceDate(dataDO);
                    String UnitPrice_clean=httpUtil.cleanAmount(dataDO).getUnitPrice();
                    String Quantity_clean=httpUtil.cleanQuantity(dataDO);
                    String TaxAmount_clean=httpUtil.cleantotalAmount(dataDO).getTaxAmount();
                    String TotalAmount_clean=httpUtil.cleantotalAmount(dataDO).getTotalAmount();
                    String PurchaseOrderNumber_clean=httpUtil.cleanPurchaseOrderNumber(PurchaseOrderNumber);
                    DataDO dataDO1=new DataDO();
                    dataDO1.setCompanyCode(companycode);
                    dataDO1.setTotalAmount(TotalAmount_clean);
                    dataDO1.setAmount(Amount_clean);
                    dataDO1.setTaxAmount(TaxAmount_clean);
                    dataDO1.setPurchaseOrderNumber(PurchaseOrderNumber_clean);
                    dataDO1.setInvoiceReferenceNumber(InvoiceReferenceNumber_clean);
                    dataDO1.setUnitPrice(UnitPrice_clean);
                    String Poshorttext =httpUtil.cleanShortText(dataDO1);
                    dataDO1.setPoshorttext(Poshorttext.replace("\n",""));
                    dataDO1.setQuantity(Quantity_clean);
                    dataDO1.setInvoicedate(InvoiceDate_clean);
                    dataDOList1.add(dataDO1);
                }
                List<DataDO> dataDO1=httpUtil.invoiceVerification(dataDOList1);
                httpUtil.excelOutput(dataDO1,SAP_filepath,filename,2,companycode);
            }else if (companycode.contains("62F0")){
                com.alibaba.fastjson.JSONObject jsonObject2= com.alibaba.fastjson.JSON.parseObject(json2);
                System.out.println("jsonObject2是："+jsonObject2+"\n");
                JSONArray jsonArray2 =jsonObject2.getJSONObject("ocrResult").getJSONArray("ranges");
                for (int i1=0;i1<jsonArray2.size();i1++){
                    System.out.println(jsonArray2.getJSONObject(0).getJSONArray("rowDatas")+"\n");
                }
                String InvoiceReferenceNumber =(String) jsonArray.getJSONObject(0).get("value");
                String InvoiceDate =(String) jsonArray.getJSONObject(1).get("value");
                String TaxAmount =(String) jsonArray.getJSONObject(3).get("value");
                String TotalAmount =(String) jsonArray.getJSONObject(4).get("value");
                System.out.println("jsonArry2+++++++++++++++++++++++++是"+jsonArray2);
                for (int i1=0;i1<jsonArray2.size();i1++){
                    System.out.println(jsonArray2.getJSONObject(i1)+"\n");
                }
                JSONArray jsonArray1=jsonArray.getJSONObject(2).getJSONArray("rowDatas");
                List<DataDO> dataDOS = new ArrayList<>();
                for (int i1=0;i1<jsonArray1.size();i1++){
                    String POShortText = (String)jsonArray1.getJSONArray(i1).getJSONObject(0).get("value");
                    String UnitPrice = (String)jsonArray1.getJSONArray(i1).getJSONObject(1).get("value");
                    String Amount = (String)jsonArray1.getJSONArray(i1).getJSONObject(2).get("value");
                    String Quantity = (String)jsonArray1.getJSONArray(i1).getJSONObject(3).get("value");

                    DataDO dataDO =new DataDO();
                    dataDO.setCompanyCode(companycode);
                    dataDO.setInvoiceReferenceNumber(InvoiceReferenceNumber);
                    dataDO.setInvoicedate(InvoiceDate);
                    dataDO.setTaxAmount(TaxAmount);
                    dataDO.setTotalAmount(TotalAmount);
                    dataDO.setPoshorttext(POShortText);
                    dataDO.setUnitPrice(UnitPrice);
                    dataDO.setAmount(Amount);
                    dataDO.setQuantity(Quantity);
                    dataDO.setPurchaseOrderNumber(ponumber);
                    dataDOS.add(dataDO);
                }
//                httpUtil.excelOutput(dataDOS,OCR_filepath,filepath,1,companycode);
                for (int i1=0;i1<jsonArray2.size();i1++){
                    System.out.println(jsonArray2.getJSONObject(i1)+"+++++++++++++++++++++++++++++++++++++"+"\n");
                }
                String InvoiceReferenceNumber2 =(String) jsonArray2.getJSONObject(1).get("value");
                String InvoiceDate2 =(String) jsonArray2.getJSONObject(2).get("value");
                String TaxAmount2 =(String) jsonArray2.getJSONObject(3).get("value");
                String TotalAmount2 =(String) jsonArray2.getJSONObject(4).get("value");
                System.out.println(InvoiceReferenceNumber2+"\n");
                System.out.println(InvoiceDate2+"\n");
                System.out.println(TaxAmount2+"\n");
                System.out.println(TotalAmount2+"\n");
                List<DataDO> dataDOS1 = new ArrayList<>();
                for (int i1=0;i1<jsonArray1.size();i1++){
                    String POShortText2 = (String)jsonArray2.getJSONObject(0).getJSONArray("rowDatas").getJSONArray(0).getJSONObject(0).get("value");
                    String UnitPrice2 = (String)jsonArray2.getJSONObject(0).getJSONArray("rowDatas").getJSONArray(0).getJSONObject(1).get("value");
                    String Amount2 = (String)jsonArray2.getJSONObject(0).getJSONArray("rowDatas").getJSONArray(0).getJSONObject(2).get("value");
                    String Quantity2 = (String)jsonArray2.getJSONObject(0).getJSONArray("rowDatas").getJSONArray(0).getJSONObject(3).get("value");
//                    System.out.println(POShortText+"\n");
//                    System.out.println(UnitPrice+"\n");
//                    System.out.println(Amount+"\n");
//                    System.out.println(Quantity+"\n");
                    DataDO dataDO =new DataDO();
                    dataDO.setCompanyCode(companycode);
                    dataDO.setInvoiceReferenceNumber2(InvoiceReferenceNumber2);
                    dataDO.setInvoicedate(InvoiceDate2);
                    dataDO.setTaxAmount(TaxAmount2);
                    dataDO.setTotalAmount(TotalAmount2);
                    dataDO.setPoshorttext(POShortText2);
                    dataDO.setUnitPrice(UnitPrice2);
                    dataDO.setAmount(Amount2);
                    dataDO.setPurchaseOrderNumber(ponumber);
                    dataDO.setQuantity(Quantity2);
                    dataDOS1.add(dataDO);
                }
//                httpUtil.excelOutput(dataDOS1,OCR_filepath,filename,1,companycode);
                //以上是生成OCR的两个表
                String InvoiceDate_clean="";
                String TotalAmount_clean="";
                String InvoiceDate_clean2="";
                String TotalAmount_clean2="";
                List<DataDO> dataDOs_clean =new ArrayList<>();
                for (DataDO dataDO:dataDOS){
                    System.out.println(dataDO.getAmount()+"原来的amount是");
                    String  Amount_clean = httpUtil.cleanAmount(dataDO).getAmount();
                     InvoiceDate_clean=httpUtil.cleanInvoiceDate(dataDO);
                    System.out.println("到了这一步InvoiceDate_clean"+InvoiceDate_clean);
                    String Poshorttext_clean = httpUtil.cleanPOshorttext(dataDO);
                    String TaxAmount_clean=httpUtil.cleanAmount(dataDO).getTaxAmount();
                     TotalAmount_clean =httpUtil.cleanAmount(dataDO).getTotalAmount();
                    String UnitPrice = httpUtil.cleanAmount(dataDO).getUnitPrice();
                    dataDO.setPoshorttext(Poshorttext_clean.trim());
                    dataDO.setInvoicedate(InvoiceDate_clean);
                    dataDOs_clean.add(dataDO);
                    InvoiceDate_clean =dataDO.getInvoicedate();


                }
                List<DataDO> dataDO2s_clean =new ArrayList<>();
                for (DataDO dataDO:dataDOS1){
                    String Amount_clean2 = httpUtil.cleanAmount(dataDO).getAmount();
                     InvoiceDate_clean2=httpUtil.cleanInvoiceDate(dataDO);
                    String Poshorttext_clean2 = httpUtil.cleanPOshorttext(dataDO);
                    String TaxAmount_clean2=httpUtil.cleanAmount(dataDO).getTaxAmount();
                    TotalAmount_clean2 =httpUtil.cleanAmount(dataDO).getTotalAmount();
                    String UnitPrice2 = httpUtil.cleanAmount(dataDO).getUnitPrice();
                    dataDO.setInvoicedate(InvoiceDate_clean2);
                    dataDO.setPoshorttext(Poshorttext_clean2);
                    InvoiceDate_clean2 = dataDO.getInvoicedate();
                    TotalAmount_clean2=dataDO.getTotalAmount();
                    dataDO2s_clean.add(dataDO);

                }
                    System.out.println("发票日期的第一张表的值为："+InvoiceDate_clean+"\n");
                    System.out.println("发票日期的第二张表的值为："+InvoiceDate_clean2+"\n");
                    System.out.println("总账数的第一张表的值为："+TotalAmount_clean+"\n");
                    System.out.println("总账数的第二张表的值为："+TotalAmount_clean2+"\n");
                if (InvoiceDate_clean.equals(InvoiceDate_clean2)&&TotalAmount_clean.equals(TotalAmount_clean2)){
                    System.out.println("校验成功!+++++++++++++++++++"+"\n"+"校验路径是 "+OCR_filepath+"\n");

                    httpUtil.excelOutput_RUS_ETL(dataDOs_clean,OCR_filepath,"ETL校验",1);
                    System.out.println("校验表生成成功！+++++++++++++++++++"+"\n");
                    dataDOs_clean =httpUtil.invoiceVerification(dataDO2s_clean);
                    httpUtil.excelOutput(dataDOs_clean,SAP_filepath,filename,2,companycode);
                }else {
                    System.out.println("校验失败!+++++++++++++++++++"+"\n");
                    httpUtil.excelOutput_RUS_ETL(dataDO2s_clean,OCR_filepath,"ETL校验",0);
                }
            }
            else {
                JSONArray jsonArray1 = jsonArray.getJSONObject(0).getJSONArray("rowDatas");
                System.out.println("jsonArray"+jsonArray);
                System.out.println("jsonArray1"+jsonArray1);
//                for (int i1=0;i1<jsonArray.size();i1++){
//                    System.out.print(jsonArray.get(i1)+"\n");
//                }
//                for (int i1=0;i1<jsonArray1.size();i1++){
//                    System.out.print(jsonArray1.getJSONArray(i1)+"\n");
//                }
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
                    String PO=(String)(jsonArray.getJSONObject(1).get("value"));
                    int a = PO.indexOf("00036");
                    int b =PO.lastIndexOf("00036");
                    int c = PO.indexOf("45006");
                    int d =PO.lastIndexOf("45006");
                    String purchaseOrderNumber = PO.substring(c,d+11);
                    String SOnumber = PO.substring(a,b+11);
                    System.out.println("purchaseOrderNumber的值为："+purchaseOrderNumber+"\n");
                    System.out.println("SOnumber的值为："+SOnumber+"\n");
                    String GoodsDescription=(String)(jsonArray1.getJSONArray(0).getJSONObject(0).get("value"));
                    System.out.println("GoodsDescription的值为"+GoodsDescription+"\n");
                List<DataDO> dataDOList=new ArrayList<>();
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
                          dataDO.setPurchaseOrderNumber(purchaseOrderNumber);
                          dataDO.setQuantity(Quantity);
                          dataDO.setTotalAmount(totalAmount);
                          dataDO.setUnitPrice(unitPrice);
                          dataDO.setSOnumber(SOnumber);
                          dataDO.setTaxAmount(taxAmount);
                          dataDO.setGoodDescription(GoodsDescription);
                          dataDO.setFilepath(filepath);
                          dataDO.setCurrency(dataDO.getAmount().substring(0,3));
                          dataDOList.add(dataDO);
//清理新加坡
                }
                httpUtil.excelOutput(dataDOList,OCR_filepath,filename,1,companycode);
                    List<DataDO> dataDOList1=new ArrayList<>();
                    for (DataDO dataDO:dataDOList){
                        String Amount_clean=httpUtil.cleanAmount(dataDO).getAmount();
                        String UnitPrice_clean=dataDO.getUnitPrice();
                        System.out.println("UnitPrice_clean以后的结果是+"+UnitPrice_clean+"\n");
                        String InvoiceReferenceNumber_clean=httpUtil.cleanInvoiceReferenceNumber(dataDO.getInvoiceReferenceNumber());
                        String InvoiceDate_clean=httpUtil.cleanInvoiceDate(dataDO);
                        String Quantity_clean=httpUtil.cleanQuantity(dataDO);
                        String Poshorttext_clean=dataDO.getPoshorttext().replaceAll(" ","");
                        String TotalAmount_clean=dataDO.getTotalAmount();
                        dataDO.setCompanyCode(companycode);
                        dataDO.setTotalAmount(TotalAmount_clean);
                        dataDO.setAmount(Amount_clean);
                        dataDO.setTaxAmount(dataDO.getTaxAmount());
                        dataDO.setPurchaseOrderNumber(dataDO.getPurchaseOrderNumber());
                        dataDO.setInvoiceReferenceNumber(InvoiceReferenceNumber_clean);
                        dataDO.setUnitPrice(UnitPrice_clean);
                        dataDO.setPoshorttext(Poshorttext_clean);
                        dataDO.setQuantity(Quantity_clean);
                        dataDO.setInvoicedate(InvoiceDate_clean);
                       dataDOList1.add(dataDO);
                    }
                    dataDOList1 =httpUtil.invoiceVerification(dataDOList);
               String ETL_filepath="C:\\Users\\songyu\\Desktop\\OCR_Result\\ETL.xls";
                httpUtil.excelOutput(dataDOList1,ETL_filepath,filename,1,companycode);
            }
        }
	else {
            List<DataDO> dataDOList =new ArrayList<>();
            DataDO dataDO = new DataDO();
            dataDO.setStatus(OCR_status);
            dataDOList.add(dataDO);
            httpUtil.excelOutput(dataDOList,OCR_filepath,filename,0,companycode);
        }
	}
    public DataDO cleanAmount(DataDO dataDO){
            DecimalFormat df=new DecimalFormat("0.00");
            String amount = dataDO.getAmount();
            String price = dataDO.getUnitPrice();
            System.out.println("原始的price 是"+price);
            amount=amount.trim();
            amount=amount.replace(",","");
            amount=amount.replace(".","");
            amount=amount.replace("S","5");
            amount=amount.replace("I","1");
            amount=amount.replaceAll("(?:I|!)","1");
            amount=amount.replace("(","1");
            amount=amount.replace(")","1");
            amount=amount.replace("（","1");
            amount=amount.replace("）","1");
            amount=amount.replace("&","8");
            amount=amount.replace("o","0");
            amount=amount.replace("O","0");
            amount=amount.replace("（","1");
            amount=amount.replace("）","1");
            amount=amount.replace("|","1");
            amount=amount.replace("\\","1");
            amount=amount.replace("/","1");
            amount=amount.replace("\n","");
            amount=amount.replace("↓","1");

            amount= amount.trim();
            String  taxAmount_clean=dataDO.getTaxAmount().replace("|","");
            System.out.println("TotalAmount是"+dataDO.getTotalAmount());
            String  totalamount_clean =dataDO.getTotalAmount().replace("|","");
            String unitprice_clean=dataDO.getUnitPrice().replace(",",".");
             if(dataDO.getCompanyCode().contains("62F0-FAC")){
                amount= amount;

                 taxAmount_clean=taxAmount_clean.trim();
                 taxAmount_clean=taxAmount_clean.replace(",",".");

                 totalamount_clean=totalamount_clean.trim();
                 totalamount_clean=totalamount_clean.replace(",",".");
                 unitprice_clean=unitprice_clean.replace("|","");
                 unitprice_clean=unitprice_clean.trim();

            }
            else {
                amount=amount.substring(3);
                System.out.println("price是"+price);
                 unitprice_clean=price.substring(3);
                 totalamount_clean=totalamount_clean.substring(3);

            }
            amount= amount.substring(0,amount.length()-2)+"."+amount.substring(amount.length()-2);
            dataDO.setAmount(amount);
            price=price.replace("\n","");
            dataDO.setUnitPrice(price);
            dataDO.setTaxAmount(taxAmount_clean);
            dataDO.setTotalAmount(totalamount_clean);
            dataDO.setUnitPrice(unitprice_clean);
        return  dataDO;

    }
    public String cleanInvoiceDate(DataDO dataDO){
        DecimalFormat df=new DecimalFormat("0.00");
        String invoiceDate =dataDO.getInvoicedate();
        String comapanycode=dataDO.getCompanyCode();
        invoiceDate=invoiceDate.trim();
        invoiceDate=invoiceDate.replace(",","");
        invoiceDate=invoiceDate.replace(".","");
        invoiceDate=invoiceDate.replace("S","5");
        invoiceDate=invoiceDate.replace("I","1");
        invoiceDate=invoiceDate.replaceAll("(?:I|!)","1");
        invoiceDate=invoiceDate.replace("(","1");
        invoiceDate=invoiceDate.replace(")","1");
        invoiceDate=invoiceDate.replace("（","1");
        invoiceDate=invoiceDate.replace("）","1");
        invoiceDate=invoiceDate.replace("&","8");
        invoiceDate=invoiceDate.replace("o","0");
        invoiceDate=invoiceDate.replace("O","0");
        invoiceDate=invoiceDate.replace("（","1");
        invoiceDate=invoiceDate.replace("）","1");
        invoiceDate=invoiceDate.replace("|","1");
        invoiceDate=invoiceDate.replace("\\","1");
        invoiceDate=invoiceDate.replace("/","1");
        invoiceDate=invoiceDate.replace("\n","");
        invoiceDate=invoiceDate.replace("↓","1");
        invoiceDate=invoiceDate.trim();
        String invoiceDate_clean="";
        if (comapanycode.contains("6560-FAC")){
            String invoiceDate_month=invoiceDate.substring(2,4);

            if (Integer.parseInt(invoiceDate_month)>=1 && Integer.parseInt(invoiceDate_month)<=11){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                String year = String.valueOf(calendar.get(Calendar.YEAR));
                invoiceDate_clean=invoiceDate.substring(0,4)+year;
            }else {
                invoiceDate_clean=invoiceDate;

            }
            invoiceDate_clean=invoiceDate_clean.substring(2,4)+"/"+invoiceDate_clean.substring(0,2)+"/"+invoiceDate_clean.substring(4,8);

            invoiceDate_clean=invoiceDate_clean.replace("\n","");
        }else {
            System.out.println("日期处理运行到这里+"+dataDO.getInvoicedate());
            if (dataDO.getCompanyCode().contains("62F0-FAC")){
               invoiceDate_clean =dataDO.getInvoicedate().substring(3,5)+"/"+dataDO.getInvoicedate().substring(0,2)+"/"+dataDO.getInvoicedate().substring(6);
            }else {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date=simpleDateFormat.parse(invoiceDate);
                    invoiceDate_clean=new SimpleDateFormat("MM/dd/yyyy").format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }
        invoiceDate_clean=invoiceDate_clean.replace("\n","");
        System.out.print("发票日期是："+invoiceDate_clean);
        return  invoiceDate_clean;
    }
    public String cleanInvoiceReferenceNumber(String invoiceReferenceNumber){
        DecimalFormat df=new DecimalFormat("0.00");
        invoiceReferenceNumber=invoiceReferenceNumber.trim();
        invoiceReferenceNumber=invoiceReferenceNumber.replace(",","");
        invoiceReferenceNumber=invoiceReferenceNumber.replace(".","");
        invoiceReferenceNumber=invoiceReferenceNumber.replace("S","5");
        invoiceReferenceNumber=invoiceReferenceNumber.replace("I","1");
        invoiceReferenceNumber=invoiceReferenceNumber.replaceAll("(?:I|!)","1");
        invoiceReferenceNumber=invoiceReferenceNumber.replace("(","1");
        invoiceReferenceNumber=invoiceReferenceNumber.replace(")","1");
        invoiceReferenceNumber=invoiceReferenceNumber.replace("（","1");
        invoiceReferenceNumber=invoiceReferenceNumber.replace("）","1");
        invoiceReferenceNumber=invoiceReferenceNumber.replace("&","8");
        invoiceReferenceNumber=invoiceReferenceNumber.replace("o","0");
        invoiceReferenceNumber=invoiceReferenceNumber.replace("O","0");
        invoiceReferenceNumber=invoiceReferenceNumber.replace("（","1");
        invoiceReferenceNumber=invoiceReferenceNumber.replace("）","1");
        invoiceReferenceNumber=invoiceReferenceNumber.replace("|","1");
        invoiceReferenceNumber=invoiceReferenceNumber.replace("\\","1");
        invoiceReferenceNumber=invoiceReferenceNumber.replace("/","1");
        invoiceReferenceNumber=invoiceReferenceNumber.replace("\n","");
        invoiceReferenceNumber=invoiceReferenceNumber.replace("↓","1");
        invoiceReferenceNumber=invoiceReferenceNumber.trim();
        if (invoiceReferenceNumber.length()==9 && invoiceReferenceNumber.substring(0).equals("9")){
            invoiceReferenceNumber="1"+invoiceReferenceNumber;
        }
        invoiceReferenceNumber=invoiceReferenceNumber.replace("\n","");
        return  invoiceReferenceNumber;

    }
    public String cleanPurchaseOrderNumber(String purchaseOrderNumber){
        DecimalFormat df=new DecimalFormat("0.00");
        purchaseOrderNumber=purchaseOrderNumber.trim();
        purchaseOrderNumber=purchaseOrderNumber.replace(",","");
        purchaseOrderNumber=purchaseOrderNumber.replace(".","");
        purchaseOrderNumber=purchaseOrderNumber.replace("S","5");
        purchaseOrderNumber=purchaseOrderNumber.replace("I","1");
        purchaseOrderNumber=purchaseOrderNumber.replaceAll("(?:I|!)","1");
        purchaseOrderNumber=purchaseOrderNumber.replace("(","1");
        purchaseOrderNumber=purchaseOrderNumber.replace(")","1");
        purchaseOrderNumber=purchaseOrderNumber.replace("（","1");
        purchaseOrderNumber=purchaseOrderNumber.replace("）","1");
        purchaseOrderNumber=purchaseOrderNumber.replace("&","8");
        purchaseOrderNumber=purchaseOrderNumber.replace("o","0");
        purchaseOrderNumber=purchaseOrderNumber.replace("O","0");
        purchaseOrderNumber=purchaseOrderNumber.replace("（","1");
        purchaseOrderNumber=purchaseOrderNumber.replace("）","1");
        purchaseOrderNumber=purchaseOrderNumber.replace("|","1");
        purchaseOrderNumber=purchaseOrderNumber.replace("\\","1");
        purchaseOrderNumber=purchaseOrderNumber.replace("/","1");
        purchaseOrderNumber=purchaseOrderNumber.replace("\n","");
        purchaseOrderNumber=purchaseOrderNumber.replace("↓","1");
        purchaseOrderNumber=purchaseOrderNumber.trim();
        boolean result2=purchaseOrderNumber.substring(0,10).matches("[0-9]+");
        boolean result3=purchaseOrderNumber.substring(0,9).matches("[0-9]+");
        String purchaseOrderNumber_clean="";
        if(purchaseOrderNumber.contains("TH")){
            boolean result=purchaseOrderNumber.substring(6).substring(0,10).matches("[0-9]+");
            boolean result1=purchaseOrderNumber.substring(6).substring(0,9).matches("[0-9]+");
            if (result==true && purchaseOrderNumber.substring(6).substring(0,2).equals("56")){
                purchaseOrderNumber_clean = purchaseOrderNumber.substring(6).substring(0,10);
            } else if (result1==true && purchaseOrderNumber.substring(6).substring(0,1).equals("1")){
                purchaseOrderNumber_clean ="5"+ purchaseOrderNumber.substring(6);
                purchaseOrderNumber_clean=purchaseOrderNumber_clean.substring(0,10);
            }else {
                System.out.print("Error!");
            }
        }else {
            if (result2==true && purchaseOrderNumber.substring(0,2).equals("56")){
                purchaseOrderNumber_clean = purchaseOrderNumber.substring(0,10);
            }else if(result3==true && purchaseOrderNumber.substring(0,1).equals("1")){
                purchaseOrderNumber_clean ="5"+ purchaseOrderNumber;
                purchaseOrderNumber_clean=purchaseOrderNumber_clean.substring(0,10);
            }
        }
        purchaseOrderNumber_clean=purchaseOrderNumber_clean.replace("\n","");
        return  purchaseOrderNumber_clean;

    }

    public String cleanQuantity(DataDO dataDO){
        DecimalFormat df=new DecimalFormat("0.000");
        String quantity =dataDO.getQuantity();
        System.out.print("泰国原始的quantiy是"+quantity);
        quantity=quantity.trim();
        quantity=quantity.replace(",","");
        quantity=quantity.replace(".","");
        quantity=quantity.replace("I","1");
        quantity=quantity.replaceAll("(?:I|!)","1");
        quantity=quantity.replace("(","1");
        quantity=quantity.replace(")","1");
        quantity=quantity.replace("（","1");
        quantity=quantity.replace("）","1");
        quantity=quantity.replace("&","8");
        quantity=quantity.replace("o","0");
        quantity=quantity.replace("O","0");
        quantity=quantity.replace("（","1");
        quantity=quantity.replace("）","1");
        quantity=quantity.replace("|","1");
        quantity=quantity.replace("\\","1");
        quantity=quantity.replace("/","1");
        quantity=quantity.replace("\n","");
        quantity=quantity.replace("↓","1");
        quantity=quantity.trim();
        String quantity_clean=quantity.substring(0,quantity.length()-3)+"."+quantity.substring(quantity.length()-3);
        if (!dataDO.getCompanyCode().contains("6560-FAC")){
            int S = quantity.indexOf("S");
            quantity_clean=quantity_clean.substring(0,S);
        }
        System.out.print("泰国的quantity是"+quantity_clean);
        return  quantity_clean;

    }
    public DataDO cleantotalAmount(DataDO dataDO){
        DecimalFormat df=new DecimalFormat("0.00");
        String totalAmount =dataDO.getTotalAmount();
        totalAmount=totalAmount.trim();
        String str1 = new StringBuilder(totalAmount).reverse().toString();
        int index=str1.indexOf(",");
        if (index==-1){
            index=str1.indexOf("，");
            if (index==-1){
                index=str1.indexOf(".");
            }
        }
        try {
            String str2=str1.substring(0,index);
            str2=new StringBuilder(str2).reverse().toString();
            if (str2.length()==3){
                totalAmount=totalAmount.substring(0,totalAmount.indexOf(str2));
            }
            totalAmount=totalAmount.replace(",","");
            totalAmount=totalAmount.replace(".","");
            totalAmount=totalAmount.replace("S","5");
            totalAmount=totalAmount.replace("I","1");
            totalAmount=totalAmount.replaceAll("(?:I|!)","1");
            totalAmount=totalAmount.replace("(","1");
            totalAmount=totalAmount.replace(")","1");
            totalAmount=totalAmount.replace("（","1");
            totalAmount=totalAmount.replace("）","1");
            totalAmount=totalAmount.replace("&","8");
            totalAmount=totalAmount.replace("o","0");
            totalAmount=totalAmount.replace("O","0");
            totalAmount=totalAmount.replace("（","1");
            totalAmount=totalAmount.replace("）","1");
            totalAmount=totalAmount.replace("|","1");
            totalAmount=totalAmount.replace("\\","1");
            totalAmount=totalAmount.replace("/","1");
            totalAmount=totalAmount.replace("\n","");
            totalAmount=totalAmount.replace("↓","1");
            totalAmount=totalAmount.trim();
            String totalAmount_clean=totalAmount.substring(0,totalAmount.length()-2)+"."+totalAmount.substring(totalAmount.length()-2);
            if (!dataDO.getCompanyCode().contains("6560")){
                totalAmount_clean=totalAmount_clean.substring(3);
                dataDO.setTotalAmount(totalAmount_clean);
            }
            return  dataDO;
        } catch (StringIndexOutOfBoundsException e){
            return null;
        }
    }
    public String cleanPOshorttext(DataDO dataDO){
	    if (dataDO.getCompanyCode().contains("62F0-FAC")){

	        String POshorttext = dataDO.getPoshorttext();
	        POshorttext = POshorttext.trim();
            int a =POshorttext.lastIndexOf(" ");
            POshorttext = POshorttext.trim().replace("\n","");
            return POshorttext.substring(a);
        }
	    return null;
    }
    public List<DataDO> invoiceVerification(List<DataDO> dataDOS){
        return getDataDOS(dataDOS);
    }
    public String cleanShortText(DataDO dataDO){
	    String poshorttext =dataDO.getPoshorttext();

	    poshorttext = poshorttext.replace(",","");
	    poshorttext=poshorttext.replace(".","");
	    poshorttext=poshorttext.replace(" ","");
	    if (poshorttext.substring(0,4).equals("FIR")||poshorttext.substring(0,4).equals("l-I")||poshorttext.substring(0,4).equals("IIR")||poshorttext.substring(0,4).equals("liR")||poshorttext.substring(0,4).equals("TIR")){
	        poshorttext = "HR"+poshorttext.substring(4);
	        if (poshorttext.substring(0,3).equals("HR-")){
                if (poshorttext.substring(6,7).equals("I")){
                    poshorttext=poshorttext.replace("\n","");
                    StringBuffer stringBuffer=new StringBuffer(poshorttext);
                    stringBuffer.replace(6,7,"1");
                    stringBuffer.insert(stringBuffer.length()-2," ");
                    poshorttext = new String(stringBuffer);

                }
            }else if (poshorttext.substring(0,4).equals("HRF-")){
                poshorttext=poshorttext.replace("\n","");
                StringBuffer stringBuffer=new StringBuffer(poshorttext);
                stringBuffer.insert(stringBuffer.length()-2," ");
            }
	    }else if (poshorttext.substring(0,3).equals("HCF")){
	        poshorttext.replace("ww","");
	        poshorttext.replace("\n","");
        }else if (poshorttext.substring(0,3).equals("HWM")){
            if (poshorttext.substring(poshorttext.length()-4).equals("2015")){
                StringBuffer stringBuffer=new StringBuffer(poshorttext);
                stringBuffer.replace(stringBuffer.length()-4,stringBuffer.length()-1,"");
            }else if (poshorttext.substring(poshorttext.length()-4).equals("CPP)")){
                StringBuffer stringBuffer=new StringBuffer(poshorttext);
                stringBuffer.replace(stringBuffer.length()-4,stringBuffer.length()-1,"(PP)");
            }
        }else if(poshorttext.substring(0,3).equals("HSU")){
	        poshorttext.replace("O3T","03T");
	        poshorttext.replace("^^","-");
	        poshorttext.replace("\n","");
        }else{
	        poshorttext.replace("\n","");
        }
        return poshorttext;
    }
    public void excelOutput(List<DataDO> dataDOS,String filename,String filePath,int index,String comapnycode){
        XSSFWorkbook xssfWorkbook =new XSSFWorkbook();
        XSSFSheet xssfSheet=xssfWorkbook.createSheet("Data_clean");
        String[] headers=new String[]{};
        if(index==0 ||index==1){

            if (comapnycode.contains("62F0-fAC")){
                headers = new String[]{"FileName","DownloadStatus","CompanyCode", "Amount", "InvoiceDate", "InvoiceReferenceNumber","InvoiceReferenceNumber2", "POShortText", "PurchaseOrderNumber", "Quantity", "TaxAmount", "TotalAmount", "UnitPrice","Currency","SONumber","GoodsDescription","OCRStatus","POShortText2", "Quantity2", "TaxAmount2", "TotalAmount2", "UnitPrice2"};
            }else {
                headers = new String[]{"FileName","DownloadStatus","CompanyCode", "Amount", "InvoiceDate", "InvoiceReferenceNumber","InvoiceReferenceNumber2", "POShortText", "PurchaseOrderNumber", "Quantity", "TaxAmount", "TotalAmount", "UnitPrice","Currency","SONumber","GoodsDescription","OCRStatus"};
            }
        }else {
             headers = new String[]{"FileName","CompanyCode", "Amount", "InvoiceDate", "InvoiceReferenceNumber","InvoiceReferenceNumber2", "POShortText", "PurchaseOrderNumber", "Quantity", "TaxAmount", "TotalAmount", "UnitPrice","Currency","SONumber","GoodsDescription","OCRStatus","PostingDate", "TaxCode", "Text", "BaselineDate", "ExchangeRate", "PaymentBlock", "Assignment", "HeaderText"};
        }

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
                if (comapnycode.contains("6560-FAC")){
                    row1.createCell(13).setCellValue("THB");
                }else if (comapnycode.contains("62F0-FAC")){
                    row1.createCell(13).setCellValue("RUB");
                }else {
                    row1.createCell(13).setCellValue(dataDO.getCurrency());
                }
                row1.createCell(14).setCellValue(dataDO.getSOnumber());
                row1.createCell(15).setCellValue(dataDO.getGoodDescription());
                if (index==0){
                    row1.createCell(16).setCellValue(dataDO.getStatus());
                }else {
                    row1.createCell(16).setCellValue("OK");
                }
            }else {
                row1.createCell(0).setCellValue(filePath);
                row1.createCell(1).setCellValue(dataDO.getCompanyCode());
                row1.createCell(2).setCellValue(dataDO.getAmount().replace("\n",""));
                row1.createCell(3).setCellValue(dataDO.getInvoicedate().replace("\n",""));
                try {
                    row1.createCell(4).setCellValue(dataDO.getInvoiceReferenceNumber().replace("\n",""));
                }catch (NullPointerException e){
                    row1.createCell(4).setCellValue(dataDO.getInvoiceReferenceNumber());
                }
                try {
                    row1.createCell(5).setCellValue(dataDO.getInvoiceReferenceNumber2().replace("\n",""));
                }catch (NullPointerException e){
                    row1.createCell(5).setCellValue(dataDO.getInvoiceReferenceNumber2());
                }
                row1.createCell(6).setCellValue(dataDO.getPoshorttext().replace("\n",""));
                try {
                    row1.createCell(7).setCellValue(dataDO.getPurchaseOrderNumber().replace("\n",""));
                }catch (NullPointerException e){
                    row1.createCell(7).setCellValue(dataDO.getPurchaseOrderNumber());
                }
                row1.createCell(8).setCellValue(dataDO.getQuantity().replace("\n",""));
                row1.createCell(9).setCellValue(dataDO.getTaxAmount().replace("\n",""));
                row1.createCell(10).setCellValue(dataDO.getTotalAmount().replace("\n",""));
                row1.createCell(11).setCellValue(dataDO.getUnitPrice().replace("\n",""));
                if (comapnycode.equals("6560-FAC")){
                    row1.createCell(12).setCellValue("THB");
                }else if (comapnycode.equals("62F0-FAC")){
                    row1.createCell(12).setCellValue("RUB");
                }else {
                    try{
                        row1.createCell(12).setCellValue(dataDO.getCurrency().replace("\n",""));
                    }catch (Exception e){
                        row1.createCell(12).setCellValue(dataDO.getCurrency());
                    }
                }
                if (comapnycode.equals("6560-FAC")){
                    row1.createCell(13).setCellValue(dataDO.getSOnumber());
                    row1.createCell(14).setCellValue(dataDO.getGoodDescription());
                }else {
                    try {
                        row1.createCell(13).setCellValue(dataDO.getSOnumber().replace("\n",""));
                        row1.createCell(14).setCellValue(dataDO.getGoodDescription().replace("\n",""));
                    }catch (NullPointerException e){
                        row1.createCell(13).setCellValue(dataDO.getSOnumber());
                        row1.createCell(14).setCellValue(dataDO.getGoodDescription());
                    }

                }
                row1.createCell(15).setCellValue("OK");
                try {
                    row1.createCell(16).setCellValue(dataDO.getPostingDate().replace("\n",""));
                }catch (Exception e){
                    row1.createCell(16).setCellValue(dataDO.getPostingDate());
                }
                row1.createCell(17).setCellValue(dataDO.getTaxCode());
                if (comapnycode.contains("6550")||comapnycode.contains("6560")||comapnycode.contains("65G0")){
                    row1.createCell(18).setCellValue("");
                }else {
                    try {
                        row1.createCell(18).setCellValue(dataDO.getPurchaseOrderNumber().replace("\n",""));
                    }catch (Exception e){
                        row1.createCell(18).setCellValue(dataDO.getPurchaseOrderNumber());
                    }

                }
                if (comapnycode.contains("6550")){
                    Date date =new Date();
                    SimpleDateFormat simpleDateFormat =new SimpleDateFormat("YYYY.MM.");
                     String date1 =simpleDateFormat.format(date)+"31";
                    row1.createCell(19).setCellValue(date1);
                }else {
                    row1.createCell(19).setCellValue(dataDO.getInvoicedate().replace("\n",""));
                }
                if (comapnycode.contains("62F0-FAC")){
                    row1.createCell(20).setCellValue("");
                    row1.createCell(21).setCellValue("Payment Block");
                    row1.createCell(22).setCellValue(dataDO.getInvoiceReferenceNumber2());
                    row1.createCell(23).setCellValue(dataDO.getPurchaseOrderNumber());
                }else {
                    row1.createCell(20).setCellValue("");
                    row1.createCell(21).setCellValue("");
                    if (!dataDO.getCompanyCode().contains("6560-FAC")&&!dataDO.getCompanyCode().contains("62F0-FAC")&&!dataDO.getCompanyCode().contains("6560-HQ")&&!dataDO.getCompanyCode().contains("6430-HQ")){
                        row1.createCell(22).setCellValue(dataDO.getPurchaseOrderNumber());
                    }else {
                        row1.createCell(22).setCellValue("");
                    }
                    switch (dataDO.getCompanyCode()){
                            case "62F0-FAC":
                                row1.createCell(22).setCellValue(dataDO.getPurchaseOrderNumber());
                            case "62T0-HQ":
                                row1.createCell(22).setCellValue(dataDO.getPurchaseOrderNumber());
                            case "6200-HQ":
                                row1.createCell(22).setCellValue(dataDO.getPurchaseOrderNumber());
                            case "62S0-HQ":
                                row1.createCell(22).setCellValue(dataDO.getPurchaseOrderNumber());
                            case "6400-HQ":
                                row1.createCell(22).setCellValue(dataDO.getPurchaseOrderNumber());
                            case "6550-HQ":
                                row1.createCell(22).setCellValue(dataDO.getPurchaseOrderNumber());
                            case "65G0-HQ":
                                row1.createCell(22).setCellValue(dataDO.getPurchaseOrderNumber());
                    }
                }
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
    public void excelOutput_RUS_ETL(List<DataDO> dataDOS,String filename,String filePath,int index){
        XSSFWorkbook xssfWorkbook =new XSSFWorkbook();
        XSSFSheet xssfSheet=xssfWorkbook.createSheet("Data_clean");
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
                row1.createCell(13).setCellValue("RUB");
                row1.createCell(14).setCellValue(dataDO.getSOnumber());
                row1.createCell(15).setCellValue(dataDO.getGoodDescription());
                if (index==1){
                    row1.createCell(16).setCellValue("校验成功!");
                }else {
                    row1.createCell(16).setCellValue("校验失败!");
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
//    public void excelOutput_RUS_ETL(List<DataDO> dataDOS,String filename,String filePath,int index){
//        XSSFWorkbook xssfWorkbook =new XSSFWorkbook();
//        XSSFSheet xssfSheet=xssfWorkbook.createSheet("Data_clean");
//        String[] headers=new String[]{};
//        headers = new String[]{"FileName","DownloadStatus","CompanyCode", "Amount", "InvoiceDate", "InvoiceReferenceNumber","InvoiceReferenceNumber2", "POShortText", "PurchaseOrderNumber", "Quantity", "TaxAmount", "TotalAmount", "UnitPrice","Currency","SONumber","GoodsDescription","OCRStatus"};
//
//
//        Row row0=xssfSheet.createRow(0);
//        for(int i=0;i<headers.length;i++){
//            XSSFCell cell = (XSSFCell) row0.createCell(i);
//            XSSFRichTextString text = new XSSFRichTextString(headers[i]);
//            cell.setCellValue(text);
//        }
//        int rowNum = 1;
//        for (DataDO dataDO:dataDOS){
//            XSSFRow row1 = xssfSheet.createRow(rowNum);
//
//            row1.createCell(0).setCellValue(filePath);
//            row1.createCell(1).setCellValue("OK");
//            row1.createCell(2).setCellValue(dataDO.getCompanyCode());
//            row1.createCell(3).setCellValue(dataDO.getAmount());
//            row1.createCell(4).setCellValue(dataDO.getInvoicedate());
//            row1.createCell(5).setCellValue(dataDO.getInvoiceReferenceNumber());
//            row1.createCell(6).setCellValue(dataDO.getInvoiceReferenceNumber2());
//            row1.createCell(7).setCellValue(dataDO.getPoshorttext());
//            row1.createCell(8).setCellValue(dataDO.getPurchaseOrderNumber());
//            row1.createCell(9).setCellValue(dataDO.getQuantity());
//            row1.createCell(10).setCellValue(dataDO.getTaxAmount());
//            row1.createCell(11).setCellValue(dataDO.getTotalAmount());
//            row1.createCell(12).setCellValue(dataDO.getUnitPrice());
//            row1.createCell(13).setCellValue("RUB");
//            row1.createCell(14).setCellValue(dataDO.getSOnumber());
//            row1.createCell(15).setCellValue(dataDO.getGoodDescription());
//            if (index==1){
//                row1.createCell(16).setCellValue("校验成功!");
//            }else {
//                row1.createCell(16).setCellValue("校验失败!");
//            }
//            rowNum++;
//        }
//
//
//        try {
//            FileOutputStream fileOutputStream=new FileOutputStream(filename);
//
//            try {
//                xssfWorkbook.write(fileOutputStream);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
}
