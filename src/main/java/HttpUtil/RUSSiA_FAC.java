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

import static HttpUtil.Singapore.getDataDOS;

public class RUSSiA_FAC {
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

//        String filepath1= "C:/Users/songyu/Desktop/ImageMagick-7.0.8-Q16/original/90004480/90004480-0.jpg";
//        String filepath2= "C:/Users/songyu/Desktop/ImageMagick-7.0.8-Q16/original/90004480/90004480-1.jpg";
//        String OCR_filepath ="C:/Users/songyu/Desktop/result/OCR.xls";
//        String SAP_filepath= "C:/Users/songy/Desktop/SAP_Result/SAP.xls";
//        String companycode="62F0-FAC-450003678";

        String companycode=args[0];
        String filepath1 =args[1];
        String filepath2 =args[2];
        String OCR_filepath = args[3];
        String SAP_filepath= args[4];
        //俄罗斯独有的结构，从companycode里面获取po号，本次传入的是两张jpg图片，分别是filepath1和filepath2
        System.out.println("filepath1+"+filepath1+"\n");
        System.out.println("filepath2+"+filepath2+"\n");
        String filename =filepath1.substring(filepath1.lastIndexOf("\\")+1);
        filename=filename.substring(0,filename.lastIndexOf("-"))+".pdf";
        System.out.println("filename是："+filename+"\n");
        String ponumber ="";
        String referencenumber2 ="";
        ponumber = companycode.substring(companycode.lastIndexOf("-")+1);
        companycode=companycode.substring(0,companycode.lastIndexOf("-"));
        System.out.println("companycode的号码是"+companycode);
          //本来想尝试调用线程进行pdf切割，这部分先注释掉
//        String outputPath =OCR_filepath.substring(0,OCR_filepath.lastIndexOf("."))+".jpg";
//        System.out.println("pdf的原始路径是："+filepath+",生成jpg的路径是:"+outputPath+"\n");
//        System.out.println("在服务器测试的路径"+"C:\\Program Files\\ImageMagick-7.0.8-Q16\\convert -rotate 270 -density 200"+" "+filepath+" "+outputPath);
//        java.lang.Process process= Runtime.getRuntime().exec("C:\\Program Files\\ImageMagick-7.0.8-Q16\\convert -rotate 270 -density 200"+" "+filepath+" "+outputPath);
//        process.waitFor();
//        String filepath1 = outputPath.substring(0,outputPath.lastIndexOf("."))+"-0"+".jpg";
//        String filepath2 = outputPath.substring(0,outputPath.lastIndexOf("."))+"-1"+".jpg";
        //读取图片
        File file = new File(filepath1);
        File file2 = new File(filepath2);
        byte[] fileData = FileUtils.readFileToByteArray(file);
        byte[] fileData2 = FileUtils.readFileToByteArray(file2);

        RUSSiA_FAC rusSiA_fac=new RUSSiA_FAC();
        String json="";
        String json2="";
        //调用模板返回json，由于是两个图片所以就是两个模板
        json = ocrImageFile("海尔俄罗斯采购发票校验模板一0911",file.getName(),fileData) ;
        System.out.println("json是："+json+"\n");
        json2 = ocrImageFile("海尔俄罗斯采购发票校验模板二0915",file2.getName(),fileData2) ;
        System.out.println("json2是："+json2+"\n");
        com.alibaba.fastjson.JSONObject jsonObject= com.alibaba.fastjson.JSON.parseObject(json);
        com.alibaba.fastjson.JSONObject jsonObject2= com.alibaba.fastjson.JSON.parseObject(json2);
        String OCR_status = (String) (jsonObject.get("msg"));
        String OCR_result = (String) (jsonObject.get("result"));
        String OCR_status2 = (String) (jsonObject2.get("msg"));
        String OCR_result2 = (String) (jsonObject2.get("result"));
        //判断两张图片都是否返回正确的json，如果是，进行数据字段获取和清洗，若果不是，则不清洗，返回到OCR中的标明问题字段
        if (OCR_result.contains("success")&&OCR_result2.contains("success")){
            JSONArray jsonArray =jsonObject.getJSONObject("ocrResult").getJSONArray("ranges");
            JSONArray jsonArray2_ =jsonObject2.getJSONObject("ocrResult").getJSONArray("ranges");

            JSONArray jsonArray1=jsonArray.getJSONObject(2).getJSONArray("rowDatas");
            JSONArray jsonArray2=jsonArray2_.getJSONObject(0).getJSONArray("rowDatas");

            //遍历第一个json获取相关字段
            String InvoiceReferenceNumber =(String) jsonArray.getJSONObject(0).get("value");
            String InvoiceDate =(String) jsonArray.getJSONObject(1).get("value");
            String TaxAmount =(String) jsonArray.getJSONObject(3).get("value");
            String TotalAmount =(String) jsonArray.getJSONObject(4).get("value");
            List<DataDO> dataDOS1 = new ArrayList<>();
            for (int i1=0;i1<jsonArray1.size();i1++){
                String POShortText = (String)jsonArray1.getJSONArray(i1).getJSONObject(0).get("value");
                System.out.println("我在获取的时候的第一个POshortText是"+POShortText+"\n");
                String UnitPrice = (String)jsonArray1.getJSONArray(i1).getJSONObject(1).get("value");
                String Amount = (String)jsonArray1.getJSONArray(i1).getJSONObject(2).get("value");
                String Quantity = (String)jsonArray1.getJSONArray(i1).getJSONObject(3).get("value");

                DataDO dataDO =new DataDO();
                dataDO.setCompanyCode(companycode);
                dataDO.setInvoiceReferenceNumber(InvoiceReferenceNumber);
                dataDO.setInvoicedate(InvoiceDate);
                dataDO.setTaxAmount(TaxAmount);
                System.out.println("设定第一个图的时候TaxAmount:"+dataDO.getTaxAmount()+"\n");
                dataDO.setTotalAmount(TotalAmount);
                dataDO.setPoshorttext(POShortText);
                dataDO.setUnitPrice(UnitPrice);
                dataDO.setAmount(Amount);
                dataDO.setQuantity(Quantity);
                dataDO.setPurchaseOrderNumber(ponumber);
                dataDOS1.add(dataDO);
                System.out.println("获取的时候第一个图的时候Amount:"+Amount+"\n");
                System.out.println("获取的时候第一个图的时候Uniteprice:"+UnitPrice+"\n");
                System.out.println("获取的时候第一个图的时候Quantity:"+Quantity+"\n");
                System.out.println("获取的时候第一个图的时候TotalAmount:"+TotalAmount+"\n");
                System.out.println("获取的时候第一个图的时候InvoiceDate:"+InvoiceDate+"\n");
                System.out.println("获取的时候第一个图的时候TaxAmount:"+TaxAmount+"\n");
            }

            //遍历第二个json获取相关字段
            String InvoiceReferenceNumber2 =(String) jsonArray2_.getJSONObject(1).get("value");
            String InvoiceDate2 =(String) jsonArray2_.getJSONObject(2).get("value");
            String TaxAmount2 =(String) jsonArray2_.getJSONObject(3).get("value");
            String TotalAmount2 =(String) jsonArray2_.getJSONObject(4).get("value");

            List<DataDO> dataDOS2 = new ArrayList<>();
            for (int i1=0;i1<jsonArray1.size();i1++){
                String POShortText2 = (String)jsonArray2_.getJSONObject(0).getJSONArray("rowDatas").getJSONArray(0).getJSONObject(0).get("value");
                System.out.println("我在获取的时候的第二个POshortText是"+POShortText2+"\n");
                String UnitPrice2 = (String)jsonArray2_.getJSONObject(0).getJSONArray("rowDatas").getJSONArray(0).getJSONObject(1).get("value");
                String Amount2 = (String)jsonArray2_.getJSONObject(0).getJSONArray("rowDatas").getJSONArray(0).getJSONObject(2).get("value");
                String Quantity2 = (String)jsonArray2_.getJSONObject(0).getJSONArray("rowDatas").getJSONArray(0).getJSONObject(3).get("value");
//
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
                dataDO.setTaxAmount(TaxAmount);
                dataDOS2.add(dataDO);
                System.out.println("获取的时候第二个图的时候Amount:"+Amount2+"\n");
                System.out.println("获取的时候第二个图的时候Uniteprice:"+UnitPrice2+"\n");
                System.out.println("获取的时候第二个图的时候Quantity:"+Quantity2+"\n");
                System.out.println("获取的时候第二个图的时候TotalAmount:"+TotalAmount2+"\n");
                System.out.println("获取的时候第二个图的时候InvoiceDate:"+InvoiceDate2+"\n");
                System.out.println("获取的时候第二个图的时候TaxAmount:"+TaxAmount2+"\n");

            }
            //以上是生成OCR的两个表
            String InvoiceDate_clean="";
            String TotalAmount_clean="";
            String InvoiceDate_clean2="";
            String TotalAmount_clean2="";
            List<DataDO> dataDOs_clean1 =new ArrayList<>();
            List<DataDO> dataDOs_clean2 =new ArrayList<>();
            //第一个图的清理
           for (DataDO dataDO:dataDOS1){
               InvoiceDate_clean=rusSiA_fac.cleanInvoiceDate(dataDO);
               TotalAmount_clean=rusSiA_fac.cleanAmount(dataDO).getTotalAmount();
               String amount1=rusSiA_fac.cleanAmount(dataDO).getAmount();
               String uniteprice1 =rusSiA_fac.cleanAmount(dataDO).getUnitPrice();
               String quantity1 =rusSiA_fac.cleanQuantity(dataDO);
               String Taxamount=rusSiA_fac.cleanAmount(dataDO).getTaxAmount();
               dataDO.setInvoiceReferenceNumber(rusSiA_fac.cleanInvoiceReferenceNumber(dataDO.getInvoiceReferenceNumber()));
               dataDO.setPoshorttext(rusSiA_fac.cleanPOshorttext(dataDO));
               dataDO.setAmount(amount1);
               dataDO.setUnitPrice(uniteprice1);
               dataDO.setQuantity(quantity1);
               dataDO.setTaxAmount(Taxamount);
               dataDO.setInvoicedate(InvoiceDate_clean);
               dataDO.setTotalAmount(TotalAmount_clean);
               dataDOs_clean1.add(dataDO);

               System.out.println("清洗第一个图的时候Amount:"+amount1+"\n");
               System.out.println("清洗第一个图的时候Uniteprice:"+uniteprice1+"\n");
               System.out.println("清洗第一个图的时候Quantity:"+quantity1+"\n");
               System.out.println("清洗第一个图的时候TotalAmount:"+TotalAmount_clean+"\n");
               System.out.println("清洗第一个图的时候InvoiceDate:"+InvoiceDate_clean+"\n");
               System.out.println("清洗第一个图的时候TaxAmount:"+dataDO.getTaxAmount()+"\n");


           }
            //第二个图的清理
            for (DataDO dataDO:dataDOS2){
                InvoiceDate_clean2=rusSiA_fac.cleanInvoiceDate(dataDO);
                TotalAmount_clean2=rusSiA_fac.cleanAmount(dataDO).getTotalAmount();
                String amount1=rusSiA_fac.cleanAmount(dataDO).getAmount();
                String Taxamount2=rusSiA_fac.cleanAmount(dataDO).getTaxAmount();
                String uniteprice1 =rusSiA_fac.cleanAmount(dataDO).getUnitPrice();
                String quantity1 =rusSiA_fac.cleanQuantity(dataDO);
                dataDO.setAmount(amount1);
                dataDO.setInvoiceReferenceNumber2(rusSiA_fac.cleanInvoiceReferenceNumber(dataDO.getInvoiceReferenceNumber2()));
                dataDO.setUnitPrice(uniteprice1);
                dataDO.setQuantity(quantity1);
                dataDO.setPoshorttext(rusSiA_fac.cleanPOshorttext(dataDO));
                dataDO.setInvoicedate(InvoiceDate_clean);
                dataDO.setTaxAmount(Taxamount2);
                dataDO.setTotalAmount(TotalAmount_clean);
                dataDOs_clean2.add(dataDO);
                DataDO dataDO2 = new DataDO();
                dataDO2.setInvoiceReferenceNumber2(dataDO.getInvoiceReferenceNumber2());
                referencenumber2=dataDO2.getInvoiceReferenceNumber2();
                dataDOs_clean2.add(dataDO2);
            }
//            System.out.println("发票日期的第一张表的值为："+InvoiceDate_clean+"\n");
//            System.out.println("发票日期的第二张表的值为："+InvoiceDate_clean2+"\n");
//            System.out.println("总账数的第一张表的值为："+TotalAmount_clean+"\n");
//            System.out.println("总账数的第二张表的值为："+TotalAmount_clean2+"\n");


            //将两个表单进行校验，以第一个字段为主，如果第一个表字段没有的，则以第二个表填充，校验成功以后，将两个表组合的数据生成sap，如果两个表的某个字段都没有，则报出异常直接返回OCR
            int n=0;
           String Status ="";
            if (InvoiceDate_clean.equals(InvoiceDate_clean2)&&TotalAmount_clean.equals(TotalAmount_clean2)){
                System.out.println("开始校验！！！！！！！！！！"+"\n");
               for (DataDO dataDO1:dataDOs_clean1){
                   DataDO dataDO2 = dataDOs_clean2.get(n);
                   while (n<dataDOs_clean2.size()){
                       Boolean InvoiceDate_Equal = dataDO1.getInvoicedate().equals(dataDO2.getInvoicedate());
                       Boolean InvoiceDate_1_empty = dataDO1.getInvoicedate().equals("");
                       Boolean Amount_Equal = dataDO1.getAmount().equals(dataDO2.getAmount());
                       Boolean Amount_1_empty = dataDO1.getAmount().equals("");
                       Boolean POShortText_Equal = dataDO1.getPoshorttext().equals(dataDO2.getPoshorttext());
                       System.out.println("第一张图片"+dataDO1.getPoshorttext()+"\n");
                       Boolean POShortText_1_empty = dataDO1.getPoshorttext().equals("");
                       Boolean TotalAmount_Equal = dataDO1.getTotalAmount().equals(dataDO2.getTotalAmount());
                       Boolean TotalAmount_1_empty = dataDO1.getTotalAmount().equals("");
                       Boolean UnitPrice_Equal = dataDO1.getUnitPrice().equals(dataDO2.getUnitPrice());
                       Boolean UnitPrice_1_empty = dataDO1.getUnitPrice().equals("");
                       Boolean Quantity_Equal = dataDO1.getQuantity().equals(dataDO2.getQuantity());
                       Boolean Quantity_1_empty = dataDO1.getQuantity().equals("");

                       //Amount
                       if (Amount_Equal){
                           if (Amount_1_empty){
                               Status="Amount为空";
                               rusSiA_fac.excelOutput(dataDOs_clean1,OCR_filepath,filename,0,companycode,3);
                           }
                       }else {
                           if (Amount_1_empty){
                               dataDO1.setInvoicedate(dataDO2.getAmount());
                           }else {
                               dataDO1.setInvoicedate(dataDO1.getAmount());
                           }
                       }
                       System.out.println("开始校验Amount！！！！！！！！！！"+"\n");
                        //InvoiceDate
                     if (InvoiceDate_Equal){
                         if (InvoiceDate_1_empty){
                             Status="InvoiceDate为空";
                             rusSiA_fac.excelOutput(dataDOs_clean1,OCR_filepath,filename,0,companycode,3);
                         }
                     }else {
                         if (InvoiceDate_1_empty){
                             dataDO1.setInvoicedate(dataDO2.getInvoicedate());
                         }else {
                             dataDO1.setInvoicedate(dataDO1.getInvoicedate());
                         }
                     }
                       System.out.println("开始校验InvoiceDate！！！！！！！！！！"+"\n");
                       //POShortText
                       if (POShortText_Equal){
                           if (POShortText_1_empty){
                               Status="POShortText为空";
                               rusSiA_fac.excelOutput(dataDOs_clean1,OCR_filepath,filename,0,companycode,3);
                           }
                       }else {
                           if (POShortText_1_empty){
                               dataDO1.setPoshorttext(dataDO2.getPoshorttext());
                           }else {
                               dataDO1.setPoshorttext(dataDO1.getPoshorttext());
                           }
                       }
                       System.out.println("开始校验POShortText！！！！！！！！！！"+"\n");
                       //TotalAmount
                       if (TotalAmount_Equal){
                           if (TotalAmount_1_empty){
                               Status="TotalAmount为空";
                               rusSiA_fac.excelOutput(dataDOs_clean1,OCR_filepath,filename,0,companycode,3);
                           }
                       }else {
                           if (TotalAmount_1_empty){
                               dataDO1.setTotalAmount(dataDO2.getTotalAmount());
                           }else {
                               dataDO1.setTotalAmount(dataDO1.getTotalAmount());
                           }
                       }
                       System.out.println("开始校验TotalAmount！！！！！！！！！！"+"\n");
                       //UnitPrice
                       if (UnitPrice_Equal){
                           if (UnitPrice_1_empty){
                               Status="UnitPrice为空";
                               rusSiA_fac.excelOutput(dataDOs_clean1,OCR_filepath,filename,0,companycode,3);
                           }
                       }else {
                           if (UnitPrice_1_empty){
                               dataDO1.setUnitPrice(dataDO2.getUnitPrice());
                           }else {
                               dataDO1.setUnitPrice(dataDO1.getUnitPrice());
                           }
                       }
                       System.out.println("开始校验UnitPrice！！！！！！！！！！"+"\n");
                       //Quantity
                       if (Quantity_Equal){
                           if (Quantity_1_empty){
                               Status="Quantity为空";
                               rusSiA_fac.excelOutput(dataDOs_clean1,OCR_filepath,filename,0,companycode,3);
                               break;
                           }
                       }else {
                           if (Quantity_1_empty){
                               dataDO1.setQuantity(dataDO2.getQuantity());
                           }else {
                               dataDO1.setQuantity(dataDO1.getQuantity());
                           }
                       }
                       dataDO1.setInvoiceReferenceNumber2(referencenumber2);
                       System.out.println("开始校验Quantity！！！！！！！！！！"+"\n");
                    break;
                   }

                   n++;
               }
               if (Status.equals("")){
                   rusSiA_fac.excelOutput_RUS_ETL(dataDOs_clean1,OCR_filepath,filename,1);
                   dataDOs_clean1 =rusSiA_fac.invoiceVerification(dataDOs_clean1);
                   rusSiA_fac.excelOutput(dataDOs_clean1,SAP_filepath,filename,2,companycode,0);
               }

            }else {
                rusSiA_fac.excelOutput_RUS_ETL(dataDOs_clean1,OCR_filepath,"ETL校验",0);
            }
        } else {
            System.out.println("OCR_result:"+OCR_result+"\n");
            System.out.println("OCR_result2:"+OCR_result2+"\n");
            if (OCR_result.equals("success")&&!OCR_result2.equals("success")){
                List<DataDO> dataDOList =new ArrayList<>();
                DataDO dataDO2 = new DataDO();
                dataDO2.setStatus(OCR_status2);
                dataDOList.add(dataDO2);
                rusSiA_fac.excelOutput(dataDOList,OCR_filepath,filename,0,companycode,2);
            }else if (!OCR_result.equals("success")&&OCR_result2.equals("success")){
                System.out.println("系统运行到这步"+"\n");
                List<DataDO> dataDOList =new ArrayList<>();
                DataDO dataDO = new DataDO();
                dataDO.setStatus(OCR_status);
                dataDOList.add(dataDO);
                rusSiA_fac.excelOutput(dataDOList,OCR_filepath,filename,0,companycode,1);
            }else {
                List<DataDO> dataDOList =new ArrayList<>();
                DataDO dataDO = new DataDO();
                dataDO.setStatus(OCR_status2);
                dataDOList.add(dataDO);
                rusSiA_fac.excelOutput(dataDOList,OCR_filepath,filename,0,companycode,3);
            }

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
            amount_clean = amount_clean.replace(",", ".");

            amount_clean = amount_clean.trim();

            Float amount_clean_parse = Float.parseFloat(amount_clean);
        }catch (Exception e){
            amount_clean="";
        }

        try {
            totalamount_clean = dataDO.getTotalAmount().trim();
            totalamount_clean = totalamount_clean.replace(",", ".");

            totalamount_clean = totalamount_clean.trim();
            totalamount_clean =totalamount_clean.replace("|","");
            totalamount_clean=totalamount_clean.replace("\n","");
            Float totalamount_parse = Float.parseFloat(totalamount_clean);

        }catch (Exception e){
            totalamount_clean="";
        }
        try {
            unitprice_clean = dataDO.getUnitPrice().trim();
            unitprice_clean = unitprice_clean.replace(",", ".");

            unitprice_clean = unitprice_clean.trim();
            unitprice_clean=unitprice_clean.replace("\n","");
            Float unitprice_clean_parse = Float.parseFloat(unitprice_clean);
        }catch (Exception e){
            unitprice_clean="";
        }

        try {
            taxAmount_clean = dataDO.getTaxAmount().trim();
            taxAmount_clean = taxAmount_clean.replace(",", ".");
            taxAmount_clean=taxAmount_clean.replace(" ","");
            taxAmount_clean=taxAmount_clean.replace("|","");
            taxAmount_clean = taxAmount_clean.trim();
            taxAmount_clean=taxAmount_clean.replace("\n","");
            Float taxAmount_clean_parse = Float.parseFloat(taxAmount_clean);
        }catch (Exception e){
            unitprice_clean="";
        }
        dataDO.setAmount(amount_clean);
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

        invoiceDate_clean =dataDO.getInvoicedate().substring(3,5)+"/"+dataDO.getInvoicedate().substring(0,2)+"/"+dataDO.getInvoicedate().substring(6);

        invoiceDate_clean=invoiceDate_clean.replace("\n","");
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
    public String cleanQuantity(DataDO dataDO){
        DecimalFormat df=new DecimalFormat("0.000");
        String quantity =dataDO.getQuantity();
        String quantity_clean=quantity;

        try {
            quantity_clean=quantity_clean.trim();
            quantity_clean=quantity_clean.replace(",",".");
            quantity_clean=quantity_clean.trim();
        }catch (Exception e){
            quantity_clean="";
        }

        return  quantity_clean;

    }
    public String cleanPOshorttext(DataDO dataDO){

            String POshorttext = dataDO.getPoshorttext();
            POshorttext = POshorttext.trim();
            int a =POshorttext.lastIndexOf(" ");
            POshorttext = POshorttext.trim().replace("\n","");
            POshorttext=POshorttext.replace(" ","");
            return POshorttext.substring(a);

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
    public void excelOutput(List<DataDO> dataDOS,String filename,String filePath,int index,String comapnycode,int type){
        XSSFWorkbook xssfWorkbook =new XSSFWorkbook();
        XSSFSheet xssfSheet=xssfWorkbook.createSheet("Sheet1");
        String[] headers=new String[]{};
        if(index==0 ||index==1){
            headers = new String[]{"FileName","DownloadStatus","CompanyCode", "Amount", "InvoiceDate", "InvoiceReferenceNumber","InvoiceReferenceNumber2", "POShortText", "PurchaseOrderNumber", "Quantity", "TaxAmount", "TotalAmount", "UnitPrice","Currency","SONumber","GoodsDescription","OCRStatus"};
        }else {
            headers = new String[]{"FileName","DownloadStatus","CompanyCode", "Amount", "InvoiceDate", "InvoiceReferenceNumber","InvoiceReferenceNumber2", "POShortText", "PurchaseOrderNumber", "Quantity", "TaxAmount", "TotalAmount", "UnitPrice","Currency","SONumber","OCRStatus","PostingDate","TaxCode","Text","BaselineDate","ExchangeRate","PaymentBlock","Assignment","HeaderText"};

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
                row1.createCell(13).setCellValue("RUB");
                row1.createCell(14).setCellValue(dataDO.getSOnumber());
                row1.createCell(15).setCellValue(dataDO.getGoodDescription());
                if (index==0){
                    if (type==1){
                        row1.createCell(16).setCellValue("第一张图表的识别异常状态是："+dataDO.getStatus()+",第二章图表没问题！");
                    }else if (type==2){
                        row1.createCell(16).setCellValue("第二张图表的识别异常状态是："+dataDO.getStatus()+",第一章图表没问题！");
                    }else if (type==3){
                        row1.createCell(16).setCellValue("第一张和第二张图表都识别异常");
                    }
                }else {
                    row1.createCell(16).setCellValue("OK");
                }
            }else {
                row1.createCell(0).setCellValue(filePath);
                row1.createCell(1).setCellValue("OK");
                row1.createCell(2).setCellValue(dataDO.getCompanyCode());
                row1.createCell(3).setCellValue(dataDO.getAmount().replace("\n",""));
                row1.createCell(4).setCellValue(dataDO.getInvoicedate().replace("\n",""));
                try {
                    row1.createCell(5).setCellValue(dataDO.getInvoiceReferenceNumber().replace("\n",""));
                }catch (NullPointerException e){
                    row1.createCell(5).setCellValue(dataDO.getInvoiceReferenceNumber());
                }
                try {
                    row1.createCell(6).setCellValue(dataDO.getInvoiceReferenceNumber2().replace("\n",""));
                }catch (NullPointerException e){
                    row1.createCell(6).setCellValue(dataDO.getInvoiceReferenceNumber2());
                }
                row1.createCell(7).setCellValue(dataDO.getPoshorttext());
                try {
                    row1.createCell(8).setCellValue(dataDO.getPurchaseOrderNumber().replace("\n",""));
                }catch (NullPointerException e){
                    row1.createCell(8).setCellValue(dataDO.getPurchaseOrderNumber());
                }
                row1.createCell(9).setCellValue(dataDO.getQuantity().replace("\n",""));
                row1.createCell(10).setCellValue(dataDO.getTaxAmount().replace("\n",""));
                row1.createCell(11).setCellValue(dataDO.getTotalAmount().replace("\n",""));
                row1.createCell(12).setCellValue(dataDO.getUnitPrice().replace("\n",""));
                row1.createCell(13).setCellValue("RUB");
                try {
                        row1.createCell(14).setCellValue(dataDO.getSOnumber().replace("\n",""));
                    }catch (NullPointerException e){
                        row1.createCell(14).setCellValue(dataDO.getSOnumber());
                    }
                row1.createCell(15).setCellValue("OK");
                try {
                    row1.createCell(16).setCellValue(dataDO.getPostingDate().replace("\n",""));
                }catch (Exception e){
                    row1.createCell(16).setCellValue(dataDO.getPostingDate());
                }
                row1.createCell(17).setCellValue(dataDO.getTaxCode());
                try {
                        row1.createCell(18).setCellValue(dataDO.getPurchaseOrderNumber().replace("\n",""));
                    }catch (Exception e){
                        row1.createCell(18).setCellValue(dataDO.getPurchaseOrderNumber());
                    }
                    row1.createCell(19).setCellValue(dataDO.getInvoicedate().replace("\n",""));


                    row1.createCell(20).setCellValue("");
                    row1.createCell(21).setCellValue("Payment Block");
                    row1.createCell(22).setCellValue(dataDO.getInvoiceReferenceNumber2());
                    row1.createCell(23).setCellValue(dataDO.getPurchaseOrderNumber());

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
                row1.createCell(16).setCellValue("OK");
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

    }

