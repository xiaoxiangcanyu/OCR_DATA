//package HttpUtil;
//
//import DataClean.DataDO;
//import FileIo.FileIO;
//import com.alibaba.fastjson.JSONArray;
//import org.apache.commons.io.FileUtils;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.xssf.usermodel.*;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.charset.Charset;
//import java.text.DateFormat;
//import java.text.DecimalFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.regex.Pattern;
//
//import static HttpUtil.Singapore.getDataDOS;
//
//public class Thailand {
//    public static String ocrImageFile(String imageType, String fileName, byte[] fileData) throws Exception {
//        String baseUrl = "http://10.138.93.103:8080/OcrServer/ocr/ocrImageByTemplate";
////        HttpGet get=new HttpGet(baseUrl);
//        HttpPost post = new HttpPost(baseUrl);
//        ContentType contentType = ContentType.create("multipart/form-data", Charset.forName("UTF-8"));
//
//        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//        builder.setCharset(Charset.forName("UTF-8"));
//        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//        builder.addBinaryBody("file", fileData, contentType, fileName);// 文件流
//        builder.addTextBody("imageType", imageType, contentType);// 类似浏览器表单提交，对应input的name和value
//
//        HttpEntity entity = builder.build();
//        post.setEntity(entity);
//        // (3) 发送请求
//        CloseableHttpClient httpclient = HttpClients.createDefault();
//        try {
//            HttpResponse response = httpclient.execute(post);
//            if (response.getStatusLine().getStatusCode() == 200) {
//                String result = EntityUtils.toString(response.getEntity(), "utf-8");
//                return result;
//            } else {
//                throw new Exception(EntityUtils.toString(response.getEntity(), "utf-8"));
//            }
//        } finally {
//            httpclient.close();
//        }
//    }
//
//    public static void main(String[] args) throws Exception {
//        String companycode = args[0];
//        String filepath = args[1];
//        String OCR_filepath = args[2];
//        String SAP_filepath = args[3];
////       String companycode= "6560-FAC";
////       String filepath= "C:\\Users\\songyu\\Desktop\\pic\\微信图片_201809261157415.jpg";
////       String OCR_filepath="C:\\Users\\songyu\\Desktop\\OCR_Result\\OCR.xls";
////       String SAP_filepath="C:\\Users\\songyu\\Desktop\\SAP_Result\\SAP.xls";
//        //从路径中获取companycode和filename
//        String filename = filepath.substring(filepath.lastIndexOf("\\") + 1);
//        System.out.print("companycode是：" + companycode);
//        System.out.println("读取图片的路径是" + filepath + "\n");
//        File file = new File(filepath);
//        byte[] fileData = FileUtils.readFileToByteArray(file);
//        Thailand thailand = new Thailand();
//        //获取json
//        String json = ocrImageFile("haier", file.getName(), fileData);
//        //处理json
//        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(json);
//        JSONArray jsonArray = jsonObject.getJSONObject("ocrResult").getJSONArray("ranges");
//        String OCR_status = (String) (jsonObject.get("msg"));
//        String OCR_result = (String) (jsonObject.get("result"));
//        String TaxAmount = "";
//        String TotalAmount = "";
//        String Amount = "";
//        String UnitPrice = "";
//        System.out.println("json" + json);
//
//        //判断json模板是否返回成功
//        if (OCR_result.contains("success")) {
//            //获取税单前几个字段
//            String InvoiceReferenceNumber = (String) jsonArray.getJSONObject(0).get("value");
//            String InvoiceDate = (String) jsonArray.getJSONObject(1).get("value");
//            TaxAmount = (String) jsonArray.getJSONObject(3).get("value");
//            TotalAmount = (String) jsonArray.getJSONObject(4).get("value");
//            String PurchaseOrderNumber = (String) jsonArray.getJSONObject(5).get("value");
//
//            List<DataDO> dataDOList = new ArrayList<>();
//            //遍历table获取table里面的数据
//            for (int i1 = 0; i1 < jsonArray.getJSONObject(2).getJSONArray("rowDatas").size(); i1++) {
//                System.out.print(jsonArray.getJSONObject(2).getJSONArray("rowDatas").get(i1) + "\n");
//                JSONArray jsonArray1 = jsonArray.getJSONObject(2).getJSONArray("rowDatas").getJSONArray(i1);
//                String POShortText = (String) jsonArray1.getJSONObject(0).get("value");
//                System.out.println("从JSON获取的POshottext:" + POShortText + "\n");
//                UnitPrice = (String) jsonArray1.getJSONObject(1).get("value");
//                String Quantity = (String) jsonArray1.getJSONObject(2).get("value");
//                Amount = (String) jsonArray1.getJSONObject(3).get("value");
//                DataDO dataDO_original = new DataDO();
//                dataDO_original.setAmount(Amount);
//                dataDO_original.setInvoiceReferenceNumber(InvoiceReferenceNumber);
//                dataDO_original.setCompanyCode(companycode);
//                dataDO_original.setInvoicedate(InvoiceDate);
//                dataDO_original.setQuantity(Quantity);
//                dataDO_original.setPoshorttext(POShortText);
//                dataDO_original.setUnitPrice(UnitPrice);
//                dataDO_original.setPurchaseOrderNumber(PurchaseOrderNumber);
//                dataDO_original.setTaxAmount(TaxAmount);
//                dataDO_original.setTotalAmount(TotalAmount);
//                dataDOList.add(dataDO_original);
//            }
//            //生成OCR表单
//            thailand.excelOutput(dataDOList, OCR_filepath, filename, 1, companycode);
//            //数据清洗，将存好的数据封装实体类
//            List<DataDO> dataDOList1 = new ArrayList<>();
//            for (DataDO dataDO : dataDOList) {
//                String Amount_clean = thailand.cleanAmount(dataDO).getAmount();
//                String InvoiceReferenceNumber_clean = thailand.cleanInvoiceReferenceNumber(dataDO.getInvoiceReferenceNumber());
//                String InvoiceDate_clean = thailand.cleanInvoiceDate(dataDO);
//                String UnitPrice_clean = dataDO.getUnitPrice();
//                String Quantity_clean = thailand.cleanQuantity(dataDO);
//                String TaxAmount_clean = dataDO.getTaxAmount();
//                String TotalAmount_clean = dataDO.getTotalAmount();
//                System.out.println("清洗之前的PurchaseOrderNumber_clean是" + PurchaseOrderNumber + "\n");
//                String PurchaseOrderNumber_clean = thailand.cleanPurchaseOrderNumber(PurchaseOrderNumber);
//                System.out.println("清洗之后的PurchaseOrderNumber_clean是" + PurchaseOrderNumber_clean + "\n");
//                DataDO dataDO1 = new DataDO();
//                dataDO1.setCompanyCode(companycode);
//                dataDO1.setTotalAmount(TotalAmount_clean);
//                dataDO1.setAmount(Amount_clean);
//                dataDO1.setTaxAmount(TaxAmount_clean);
//                dataDO1.setPurchaseOrderNumber(PurchaseOrderNumber_clean);
//                dataDO1.setInvoiceReferenceNumber(InvoiceReferenceNumber_clean);
//                dataDO1.setUnitPrice(UnitPrice_clean);
//                String Poshorttext = thailand.cleanShortText(dataDO);
//                dataDO1.setPoshorttext(Poshorttext.replace("\n", ""));
//                dataDO1.setQuantity(Quantity_clean);
//                dataDO1.setInvoicedate(InvoiceDate_clean);
//                dataDOList1.add(dataDO1);
//            }
//
//            List<DataDO> dataDO1 = thailand.invoiceVerification(dataDOList1);
//            //对于数据无法清洗的字段进行过滤和筛选
//            List<DataDO> dataDOS_wrong = new ArrayList<>();
//
//            for (DataDO dataDO : dataDO1) {
//                String status = "";
//                String Amount_index = dataDO.getAmount();
//                String Uniteprice_index = dataDO.getUnitPrice();
//                String Totalamount_index = dataDO.getTotalAmount();
//                String Taxamount_index = dataDO.getTaxAmount();
//                String InvoiceReferenceNumber_index = dataDO.getInvoiceReferenceNumber();
//                String Ponumber_index = dataDO.getPurchaseOrderNumber();
//                if (Amount_index.contains(" ") || Uniteprice_index.contains(" ") || Totalamount_index.contains(" ") || Taxamount_index.contains(" ") || InvoiceReferenceNumber_index.contains(" ") || Ponumber_index.contains(" ")) {
//                    if (Amount_index.contains(" ")) {
//                        status = "amount字段OCR无法识别!";
//                        dataDO.setAmount(Amount);
//                    } else if (Uniteprice_index.contains(" ")) {
//                        status = status + " uniteprice字段OCR无法识别!";
//                        dataDO.setUnitPrice(UnitPrice);
//                    } else if (Totalamount_index.contains(" ")) {
//                        status = status + " totalamount字段OCR无法识别!";
//                        dataDO.setTotalAmount(TotalAmount);
//                    } else if (Taxamount_index.contains(" ")) {
//                        status = status + " Taxamount字段OCR无法识别!";
//                        System.out.println("运行到这里的taxamount" + TaxAmount + "\n");
//                        dataDO.setTaxAmount(TaxAmount);
//                    } else if (InvoiceReferenceNumber_index.contains("")) {
//                        status = status + " InvoiceReferenceNumber字段OCR无法识别!";
//                        dataDO.setTotalAmount(InvoiceReferenceNumber_index);
//                    } else if (Ponumber_index.contains(" ")) {
//                        status = status + " Ponumber_index字段OCR无法识别!";
//                        System.out.println("运行到这里的Ponumber_index" + TaxAmount + "\n");
//                        dataDO.setTaxAmount(Ponumber_index);
//                    }
//
//
//                }
//                dataDO.setStatus(status);
//                dataDOS_wrong.add(dataDO);
//            }
//            //若没有字段清洗异常，则进行SAP输出，如果有，则返回OCR表，标明问题字段
//            if (status.equals("")) {
//                thailand.excelOutput(dataDO1, SAP_filepath, filename, 2, companycode);
//                System.out.println("json" + json);
//                System.out.println("sap生成成功！");
//            } else {
//                thailand.excelOutput(dataDOS_wrong, OCR_filepath, filename, 0, companycode);
//            }
//
//        } else {
//            List<DataDO> dataDOList = new ArrayList<>();
//            DataDO dataDO = new DataDO();
//            dataDO.setStatus(OCR_status);
//            dataDOList.add(dataDO);
//            thailand.excelOutput(dataDOList, OCR_filepath, filename, 0, companycode);
//        }
//    }
//
//    public DataDO cleanAmount(DataDO dataDO) {
//        Thailand thailand = new Thailand();
//        DecimalFormat df = new DecimalFormat("0.00");
//        String amount = dataDO.getAmount();
//        String price = dataDO.getUnitPrice();
//        String totalamount = dataDO.getTotalAmount();
//        System.out.println("原始的price 是" + price);
//        amount = amount.trim();
//        amount = amount.replace(",", "");
//        amount = amount.replace(".", "");
//        amount = amount.replace("S", "5");
//        amount = amount.replace("I", "1");
//        amount = amount.replaceAll("(?:I|!)", "1");
//        amount = amount.replace("(", "1");
//        amount = amount.replace(")", "1");
//        amount = amount.replace("（", "1");
//        amount = amount.replace("）", "1");
//        amount = amount.replace("&", "8");
//        amount = amount.replace("o", "0");
//        amount = amount.replace("O", "0");
//        amount = amount.replace("（", "1");
//        amount = amount.replace("）", "1");
//        amount = amount.replace("|", "1");
//        amount = amount.replace("\\", "1");
//        amount = amount.replace("/", "1");
//        amount = amount.replace("\n", "");
//        amount = amount.replace("↓", "1");
//        amount = amount.trim();
//        amount = amount.substring(0, amount.length() - 2) + "." + amount.substring(amount.length() - 2);
//        try {
//            Float amount_parse = Float.parseFloat(amount);
//        } catch (Exception e) {
//            amount = amount + " ";
//        }
//        price = price.trim();
//        price = price.replace(",", "");
//        price = price.replace(".", "");
//        price = price.replace("S", "5");
//        price = price.replace("I", "1");
//        price = price.replaceAll("(?:I|!)", "1");
//        price = price.replace("(", "1");
//        price = price.replace(")", "1");
//        price = price.replace("（", "1");
//        price = price.replace("）", "1");
//        price = price.replace("&", "8");
//        price = price.replace("o", "0");
//        price = price.replace("O", "0");
//        price = price.replace("（", "1");
//        price = price.replace("）", "1");
//        price = price.replace("|", "1");
//        price = price.replace("\\", "1");
//        price = price.replace("/", "1");
//        price = price.replace("\n", "");
//        price = price.replace("↓", "1");
//        price = price.substring(0, price.length() - 2) + "." + price.substring(price.length() - 2);
//        price = price.replace("\n", "");
//        try {
//            Float price_parse = Float.parseFloat(price);
//        } catch (Exception e) {
//            price = price + " ";
//        }
//
//        totalamount = totalamount.trim();
//        totalamount = totalamount.replace(",", "");
//        totalamount = totalamount.replace(".", "");
//        totalamount = totalamount.replace("S", "5");
//        totalamount = totalamount.replace("I", "1");
//        totalamount = totalamount.replaceAll("(?:I|!)", "1");
//        totalamount = totalamount.replace("(", "1");
//        totalamount = totalamount.replace(")", "1");
//        totalamount = totalamount.replace("（", "1");
//        totalamount = totalamount.replace("）", "1");
//        totalamount = totalamount.replace("&", "8");
//        totalamount = totalamount.replace("o", "0");
//        totalamount = totalamount.replace("O", "0");
//        totalamount = totalamount.replace("（", "1");
//        totalamount = totalamount.replace("）", "1");
//        totalamount = totalamount.replace("|", "1");
//        totalamount = totalamount.replace("\\", "1");
//        totalamount = totalamount.replace("/", "1");
//        totalamount = totalamount.replace("\n", "");
//        totalamount = totalamount.replace("↓", "1");
//        totalamount = totalamount.substring(0, totalamount.length() - 2) + "." + totalamount.substring(totalamount.length() - 2);
//        try {
//            Float totalamount_parse = Float.parseFloat(totalamount);
//        } catch (Exception e) {
//            totalamount = totalamount + " ";
//        }
//        System.out.println("totalamount" + totalamount + "\n");
//        String taxAmount = dataDO.getTaxAmount().replace("|", "");
//        taxAmount = taxAmount.trim();
//        taxAmount = taxAmount.replace(",", "");
//        taxAmount = taxAmount.replace(".", "");
//        taxAmount = taxAmount.replace("S", "5");
//        taxAmount = taxAmount.replace("I", "1");
//        taxAmount = taxAmount.replaceAll("(?:I|!)", "1");
//        taxAmount = taxAmount.replace("(", "1");
//        taxAmount = taxAmount.replace(")", "1");
//        taxAmount = taxAmount.replace("（", "1");
//        taxAmount = taxAmount.replace("）", "1");
//        taxAmount = taxAmount.replace("&", "8");
//        taxAmount = taxAmount.replace("o", "0");
//        taxAmount = taxAmount.replace("O", "0");
//        taxAmount = taxAmount.replace("（", "1");
//        taxAmount = taxAmount.replace("）", "1");
//        taxAmount = taxAmount.replace("|", "1");
//        taxAmount = taxAmount.replace("\\", "1");
//        taxAmount = taxAmount.replace("/", "1");
//        taxAmount = taxAmount.replace("\n", "");
//        taxAmount = taxAmount.replace("↓", "1");
//        taxAmount = taxAmount.replace("S", "8");
//        taxAmount = taxAmount.substring(0, taxAmount.length() - 2) + "." + taxAmount.substring(taxAmount.length() - 2);
//        taxAmount = taxAmount.replace(",", "");
//        taxAmount = taxAmount.replace("，", "");
//        taxAmount= taxAmount.replace(":","");
//        System.out.println("taxamount清理的结果是："+taxAmount);
//        try {
//            Float taxAmount_parse = Float.parseFloat(taxAmount);
//        } catch (Exception e) {
//            taxAmount = taxAmount + " ";
//        }
//
//
//        dataDO.setAmount(amount);
//        dataDO.setTaxAmount(taxAmount);
//        dataDO.setTotalAmount(totalamount);
//        dataDO.setUnitPrice(price);
//        return dataDO;
//
//    }
//
//    public String cleanInvoiceDate(DataDO dataDO) {
//        DecimalFormat df = new DecimalFormat("0.00");
//        String invoiceDate = dataDO.getInvoicedate();
//        String comapanycode = dataDO.getCompanyCode();
//        invoiceDate = invoiceDate.trim();
//        invoiceDate = invoiceDate.replace(",", "");
//        invoiceDate = invoiceDate.replace(".", "");
//        invoiceDate = invoiceDate.replace("S", "5");
//        invoiceDate = invoiceDate.replace("I", "1");
//        invoiceDate = invoiceDate.replaceAll("(?:I|!)", "1");
//        invoiceDate = invoiceDate.replace("(", "1");
//        invoiceDate = invoiceDate.replace(")", "1");
//        invoiceDate = invoiceDate.replace("（", "1");
//        invoiceDate = invoiceDate.replace("）", "1");
//        invoiceDate = invoiceDate.replace("&", "8");
//        invoiceDate = invoiceDate.replace("o", "0");
//        invoiceDate = invoiceDate.replace("O", "0");
//        invoiceDate = invoiceDate.replace("（", "1");
//        invoiceDate = invoiceDate.replace("）", "1");
//        invoiceDate = invoiceDate.replace("|", "1");
//        invoiceDate = invoiceDate.replace("\\", "1");
//        invoiceDate = invoiceDate.replace("/", "1");
//        invoiceDate = invoiceDate.replace("\n", "");
//        invoiceDate = invoiceDate.replace("↓", "1");
//        invoiceDate = invoiceDate.trim();
//        String invoiceDate_clean = "";
//            String invoiceDate_month = invoiceDate.substring(2, 4);
//
//            if (Integer.parseInt(invoiceDate_month) >= 1 && Integer.parseInt(invoiceDate_month) <= 11) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(new Date());
//                String year = String.valueOf(calendar.get(Calendar.YEAR));
//                invoiceDate_clean = invoiceDate.substring(0, 4) + year;
//            } else {
//                invoiceDate_clean = invoiceDate;
//
//            }
//            invoiceDate_clean = invoiceDate_clean.substring(2, 4) + "/" + invoiceDate_clean.substring(0, 2) + "/" + invoiceDate_clean.substring(4, 8);
//
//            invoiceDate_clean = invoiceDate_clean.replace("\n", "");
//        invoiceDate_clean = invoiceDate_clean.replace("\n", "");
//        System.out.print("发票日期是：" + invoiceDate_clean);
//        return invoiceDate_clean;
//    }
//
//    public String cleanInvoiceReferenceNumber(String invoiceReferenceNumber) {
//        try {
//            invoiceReferenceNumber = invoiceReferenceNumber.trim();
//            invoiceReferenceNumber = invoiceReferenceNumber.replace(",", "");
//            invoiceReferenceNumber = invoiceReferenceNumber.replace(".", "");
//            invoiceReferenceNumber = invoiceReferenceNumber.replace("S", "5");
//            invoiceReferenceNumber = invoiceReferenceNumber.replace("I", "1");
//            invoiceReferenceNumber = invoiceReferenceNumber.replaceAll("(?:I|!)", "1");
//            invoiceReferenceNumber = invoiceReferenceNumber.replace("(", "1");
//            invoiceReferenceNumber = invoiceReferenceNumber.replace(")", "1");
//            invoiceReferenceNumber = invoiceReferenceNumber.replace("（", "1");
//            invoiceReferenceNumber = invoiceReferenceNumber.replace("）", "1");
//            invoiceReferenceNumber = invoiceReferenceNumber.replace("&", "8");
//            invoiceReferenceNumber = invoiceReferenceNumber.replace("o", "0");
//            invoiceReferenceNumber = invoiceReferenceNumber.replace("O", "0");
//            invoiceReferenceNumber = invoiceReferenceNumber.replace("（", "1");
//            invoiceReferenceNumber = invoiceReferenceNumber.replace("）", "1");
//            invoiceReferenceNumber = invoiceReferenceNumber.replace("|", "1");
//            invoiceReferenceNumber = invoiceReferenceNumber.replace("\\", "1");
//            invoiceReferenceNumber = invoiceReferenceNumber.replace("/", "1");
//            invoiceReferenceNumber = invoiceReferenceNumber.replace("\n", "");
//            invoiceReferenceNumber = invoiceReferenceNumber.replace("↓", "1");
//            invoiceReferenceNumber = invoiceReferenceNumber.trim();
//        } catch (Exception e) {
//            invoiceReferenceNumber = " ";
//        }
//
//        if (invoiceReferenceNumber.length() == 9 && invoiceReferenceNumber.substring(0).equals("9")) {
//            invoiceReferenceNumber = "1" + invoiceReferenceNumber;
//        }
//        invoiceReferenceNumber = invoiceReferenceNumber.replace("\n", "");
//        return invoiceReferenceNumber;
//
//    }
//
//    public String cleanPurchaseOrderNumber(String purchaseOrderNumber) {
//        String purchaseOrderNumber_clean = "";
//        try {
//            purchaseOrderNumber = purchaseOrderNumber.trim();
//            purchaseOrderNumber = purchaseOrderNumber.replace("'", "");
//            purchaseOrderNumber = purchaseOrderNumber.replace(",", "");
//            purchaseOrderNumber = purchaseOrderNumber.replace(".", "");
//            purchaseOrderNumber = purchaseOrderNumber.replace("S", "5");
//            purchaseOrderNumber = purchaseOrderNumber.replace("I", "1");
//            purchaseOrderNumber = purchaseOrderNumber.replaceAll("(?:I|!)", "1");
//            purchaseOrderNumber = purchaseOrderNumber.substring(purchaseOrderNumber.indexOf("■") + 1);
//            purchaseOrderNumber = purchaseOrderNumber.replace("（", "1");
//            purchaseOrderNumber = purchaseOrderNumber.replace("）", "1");
//            purchaseOrderNumber = purchaseOrderNumber.replace("&", "8");
//            purchaseOrderNumber = purchaseOrderNumber.replace("o", "0");
//            purchaseOrderNumber = purchaseOrderNumber.replace("O", "0");
//            purchaseOrderNumber = purchaseOrderNumber.replace("（", "1");
//            purchaseOrderNumber = purchaseOrderNumber.replace("）", "1");
//            purchaseOrderNumber = purchaseOrderNumber.replace("|", "1");
//            purchaseOrderNumber = purchaseOrderNumber.replace("\\", "1");
//            purchaseOrderNumber = purchaseOrderNumber.replace("/", "1");
//            purchaseOrderNumber = purchaseOrderNumber.replace("\n", "");
//            purchaseOrderNumber = purchaseOrderNumber.replace("↓", "1");
//            purchaseOrderNumber = purchaseOrderNumber.replace("l", "1");
//            purchaseOrderNumber_clean = purchaseOrderNumber.trim();
//            purchaseOrderNumber = purchaseOrderNumber_clean.replace(" ", "");
//            System.out.println("正则校验的时候的" + purchaseOrderNumber.substring(0, 10) + "\n");
//            boolean result2 = Pattern.matches("\\d{10}", purchaseOrderNumber.substring(0, 10));
//            boolean result3 = purchaseOrderNumber.substring(0, 9).matches("\\d{9}");
//            System.out.println("正则校验的结果的前10位为" + result2 + "\n");
//            System.out.println("正则校验的结果的前9位为" + result3 + "\n");
//            System.out.println("PO号清洗到第一阶段" + purchaseOrderNumber + "\n");
//            if (purchaseOrderNumber.contains("TH")) {
//                System.out.println("PO号清洗到第二阶段" + purchaseOrderNumber + "\n");
//                boolean result = purchaseOrderNumber.substring(6).substring(0, 10).matches("[0-9]+");
//                boolean result1 = purchaseOrderNumber.substring(6).substring(0, 9).matches("[0-9]+");
//                if (result == true && purchaseOrderNumber.substring(6).substring(0, 2).equals("56")) {
//                    purchaseOrderNumber_clean = purchaseOrderNumber.substring(6).substring(0, 10);
//                } else if (result1 == true && purchaseOrderNumber.substring(6).substring(0, 1).equals("1")) {
//                    purchaseOrderNumber_clean = "5" + purchaseOrderNumber.substring(6);
//                    purchaseOrderNumber_clean = purchaseOrderNumber_clean.substring(0, 10);
//                } else {
//                    System.out.print("Error!");
//                }
//            } else {
//                System.out.println("PO号清洗到第三阶段" + purchaseOrderNumber + "\n");
//                System.out.println("PO号清洗到第四阶段" + purchaseOrderNumber + "\n");
//                if (result2 == true && purchaseOrderNumber.substring(0, 2).equals("56")) {
//                    purchaseOrderNumber_clean = purchaseOrderNumber.substring(0, 10);
//                    System.out.println("PO号清洗到第五阶段" + purchaseOrderNumber + "\n");
//                    return purchaseOrderNumber_clean;
//                } else if (result3 == true && purchaseOrderNumber.substring(0, 1).equals("1")) {
//                    purchaseOrderNumber_clean = "5" + purchaseOrderNumber;
//                    purchaseOrderNumber_clean = purchaseOrderNumber_clean.substring(0, 10);
//                    return purchaseOrderNumber_clean;
//                }
//                purchaseOrderNumber_clean = purchaseOrderNumber.substring(0, 10);
//            }
//            purchaseOrderNumber_clean = purchaseOrderNumber_clean.replace("\n", "");
//
//        } catch (Exception e) {
//            purchaseOrderNumber_clean = " ";
//            return purchaseOrderNumber_clean;
//
//        }
//        return null;
//
//    }
//
//    public String cleanQuantity(DataDO dataDO) {
//        DecimalFormat df = new DecimalFormat("0.000");
//        String quantity = dataDO.getQuantity();
//        System.out.print("泰国原始的quantiy是" + quantity);
//        quantity = quantity.trim();
//        quantity = quantity.replace(",", "");
//        quantity = quantity.replace(".", "");
//        quantity = quantity.replace("I", "1");
//        quantity = quantity.replaceAll("(?:I|!)", "1");
//        quantity = quantity.replace("(", "1");
//        quantity = quantity.replace(")", "1");
//        quantity = quantity.replace("（", "1");
//        quantity = quantity.replace("）", "1");
//        quantity = quantity.replace("&", "8");
//        quantity = quantity.replace("o", "0");
//        quantity = quantity.replace("O", "0");
//        quantity = quantity.replace("（", "1");
//        quantity = quantity.replace("）", "1");
//        quantity = quantity.replace("|", "1");
//        quantity = quantity.replace("\\", "1");
//        quantity = quantity.replace("/", "1");
//        quantity = quantity.replace("\n", "");
//        quantity = quantity.replace("↓", "1");
//        quantity = quantity.trim();
//        String quantity_clean = quantity.substring(0, quantity.length() - 3) + "." + quantity.substring(quantity.length() - 3);
////            int S = quantity.indexOf("S");
////            quantity_clean=quantity_clean.substring(0,S);
//
//        System.out.print("泰国的quantity是" + quantity_clean);
//        return quantity_clean;
//
//    }
//
//    public List<DataDO> invoiceVerification(List<DataDO> dataDOS) {
//        return getDataDOS(dataDOS);
//    }
//
//    public String cleanShortText(DataDO dataDO) {
//        String poshorttext = dataDO.getPoshorttext();
//        poshorttext = poshorttext.replace(",", "");
//        poshorttext = poshorttext.replace(".", "");
//        poshorttext = poshorttext.replace(" ", "");
//        StringBuffer stringBuffer = new StringBuffer(poshorttext);
//
//        System.out.println("poshorttext的清洗运行到这里的前三位:" + poshorttext.substring(0, 3));
//        if (poshorttext.substring(0, 3).equals("FIR") || poshorttext.substring(0, 3).equals("l-I") || poshorttext.substring(0, 3).equals("IIR") || poshorttext.substring(0, 3).equals("liR") || poshorttext.substring(0, 3).equals("TIR")) {
//            poshorttext = "HR" + poshorttext.substring(3);
//        } else if (poshorttext.substring(0, 3).contains("HR-")) {
//            System.out.println("poshorttext的清洗进入到HR-:");
//            poshorttext = getString_HR(poshorttext);
//        } else if (poshorttext.substring(0, 4).equals("HRF-")) {
//            stringBuffer.insert(stringBuffer.length() - 2, " ");
//            poshorttext=new String(stringBuffer);
//            poshorttext =poshorttext.replace("\n","");
//        } else if (poshorttext.substring(0, 3).equals("HCF")) {
//            System.out.println("字符运行到检测HCF");
//            poshorttext = poshorttext.replace("WW", "");
//            poshorttext =poshorttext.replace("\n", "");
//        } else if (poshorttext.substring(0, 3).equals("HWM")) {
//            if (poshorttext.substring(poshorttext.length() - 4).equals("2015")) {
//                stringBuffer.replace(stringBuffer.length() - 4, stringBuffer.length(), "");
//            } else if (poshorttext.substring(poshorttext.length() - 4).equals("CPP)")) {
//                stringBuffer.replace(stringBuffer.length() - 4, stringBuffer.length(), "(PP)");
//            }
//            poshorttext=new String(stringBuffer);
//        } else {
//            poshorttext = getString_HSU(poshorttext);
//        }
//        return poshorttext;
//    }
//    public static String getString_HSU(String poshorttext) {
//        if (poshorttext.substring(0, 3).equals("HSU")) {
//            System.out.println("HSU的清理开始了" + poshorttext + "\n");
//            poshorttext = poshorttext.replace("O3T", "03T");
//            poshorttext = poshorttext.replace("^^", "-");
//            poshorttext = poshorttext.replace("\n", "");
//            System.out.println("HSU的的清理结果" + poshorttext + "\n");
//        } else {
//            poshorttext = poshorttext.replace("\n", "");
//        }
//        return poshorttext;
//    }
//    public static String getString_HR(String poshorttext) {
//        if (poshorttext.substring(6, 7).equals("I")) {
//            poshorttext = poshorttext.replace("\n", "");
//            StringBuffer stringBuffer = new StringBuffer(poshorttext);
//            stringBuffer.replace(6, 7, "1");
//            System.out.println("poshorttext的清洗运行到这里:" + stringBuffer);
//            poshorttext = new String(stringBuffer);
//
//        } else {
//            StringBuffer stringBuffer = new StringBuffer(poshorttext);
//            stringBuffer.insert(stringBuffer.length() - 2, " ");
//            poshorttext = new String(stringBuffer);
//            System.out.println("poshorttext的清洗运行到加空格:" + poshorttext);
//        }
//        return poshorttext;
//    }
//    public static List<DataDO> getDataDOS(List<DataDO> dataDOS) {
//        Date date=new Date();
//        DateFormat dFormat = new SimpleDateFormat("MM/dd/yyyy");
//        String formatDate = null;
//        formatDate=dFormat.format(date);
//        List<DataDO> dataDOS1 =new ArrayList<>();
//        for (DataDO dataDO:dataDOS){
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(new Date());
//            dataDO.setPostingDate(formatDate);
//            dataDO.setTaxCode("V7 (Input VAT Receivable 7%)");
//            dataDO.setText("Pengding");
//            dataDO.setBaselineDate("Pending");
//            dataDO.setExchangeRate("Pengding");
//            dataDO.setBaselineDate("Pengding");
//            dataDO.setPaymentBlock("Pengding");
//            dataDO.setAssignment("Pengding");
//            dataDO.setHeaderText("Pengding");
//            dataDOS1.add(dataDO);
//        }
//
//        return dataDOS1;
//    }
//    public void excelOutput(List<DataDO> dataDOS, String filename, String filePath, int index, String comapnycode) {
//        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
//        XSSFSheet xssfSheet = xssfWorkbook.createSheet("Sheet1");
//        String[] headers = new String[]{};
//        if (index == 0 || index == 1) {
//
//            if (comapnycode.contains("62F0-fAC")) {
//                headers = new String[]{"FileName", "DownloadStatus", "CompanyCode", "Amount", "InvoiceDate", "InvoiceReferenceNumber", "InvoiceReferenceNumber2", "POShortText", "PurchaseOrderNumber", "Quantity", "TaxAmount", "TotalAmount", "UnitPrice", "Currency", "SONumber", "GoodsDescription", "OCRStatus", "POShortText2", "Quantity2", "TaxAmount2", "TotalAmount2", "UnitPrice2"};
//            } else {
//                headers = new String[]{"FileName", "DownloadStatus", "CompanyCode", "Amount", "InvoiceDate", "InvoiceReferenceNumber", "InvoiceReferenceNumber2", "POShortText", "PurchaseOrderNumber", "Quantity", "TaxAmount", "TotalAmount", "UnitPrice", "Currency", "SONumber", "GoodsDescription", "OCRStatus"};
//            }
//        } else {
//            headers = new String[]{"FileName", "CompanyCode", "Amount", "InvoiceDate", "InvoiceReferenceNumber", "InvoiceReferenceNumber2", "POShortText", "PurchaseOrderNumber", "Quantity", "TaxAmount", "TotalAmount", "UnitPrice", "Currency", "SONumber", "GoodsDescription", "OCRStatus", "PostingDate", "TaxCode", "Text", "BaselineDate", "ExchangeRate", "PaymentBlock", "Assignment", "HeaderText"};
//        }
//
//        Row row0 = xssfSheet.createRow(0);
//        for (int i = 0; i < headers.length; i++) {
//            XSSFCell cell = (XSSFCell) row0.createCell(i);
//            XSSFRichTextString text = new XSSFRichTextString(headers[i]);
//            cell.setCellValue(text);
//        }
//        int rowNum = 1;
//        for (DataDO dataDO : dataDOS) {
//            XSSFRow row1 = xssfSheet.createRow(rowNum);
//            if (index == 0 || index == 1) {
//                row1.createCell(0).setCellValue(filePath);
//                row1.createCell(1).setCellValue("OK");
//                row1.createCell(2).setCellValue(dataDO.getCompanyCode());
//                row1.createCell(3).setCellValue(dataDO.getAmount());
//                row1.createCell(4).setCellValue(dataDO.getInvoicedate());
//                row1.createCell(5).setCellValue(dataDO.getInvoiceReferenceNumber());
//                row1.createCell(6).setCellValue(dataDO.getInvoiceReferenceNumber2());
//                row1.createCell(7).setCellValue(dataDO.getPoshorttext());
//                row1.createCell(8).setCellValue(dataDO.getPurchaseOrderNumber());
//                row1.createCell(9).setCellValue(dataDO.getQuantity());
//                row1.createCell(10).setCellValue(dataDO.getTaxAmount());
//                row1.createCell(11).setCellValue(dataDO.getTotalAmount());
//                row1.createCell(12).setCellValue(dataDO.getUnitPrice());
//                if (comapnycode.contains("6560-FAC")) {
//                    row1.createCell(13).setCellValue("THB");
//                } else if (comapnycode.contains("62F0-FAC")) {
//                    row1.createCell(13).setCellValue("RUB");
//                } else {
//                    row1.createCell(13).setCellValue(dataDO.getCurrency());
//                }
//                row1.createCell(14).setCellValue(dataDO.getSOnumber());
//                row1.createCell(15).setCellValue(dataDO.getGoodDescription());
//                if (index == 0) {
//                    row1.createCell(16).setCellValue(dataDO.getStatus());
//                } else {
//                    row1.createCell(16).setCellValue("OK");
//                }
//            } else {
//                row1.createCell(0).setCellValue(filePath);
//                row1.createCell(1).setCellValue(dataDO.getCompanyCode());
//                row1.createCell(2).setCellValue(dataDO.getAmount().replace("\n", ""));
//                row1.createCell(3).setCellValue(dataDO.getInvoicedate().replace("\n", ""));
//                try {
//                    row1.createCell(4).setCellValue(dataDO.getInvoiceReferenceNumber().replace("\n", ""));
//                } catch (NullPointerException e) {
//                    row1.createCell(4).setCellValue(dataDO.getInvoiceReferenceNumber());
//                }
//                try {
//                    row1.createCell(5).setCellValue(dataDO.getInvoiceReferenceNumber2().replace("\n", ""));
//                } catch (NullPointerException e) {
//                    row1.createCell(5).setCellValue(dataDO.getInvoiceReferenceNumber2());
//                }
//                row1.createCell(6).setCellValue(dataDO.getPoshorttext().replace("\n", ""));
//                try {
//                    row1.createCell(7).setCellValue(dataDO.getPurchaseOrderNumber().replace("\n", ""));
//                } catch (NullPointerException e) {
//                    row1.createCell(7).setCellValue(dataDO.getPurchaseOrderNumber());
//                }
//                row1.createCell(8).setCellValue(dataDO.getQuantity().replace("\n", ""));
//                row1.createCell(9).setCellValue(dataDO.getTaxAmount().replace("\n", ""));
//                row1.createCell(10).setCellValue(dataDO.getTotalAmount().replace("\n", ""));
//                row1.createCell(11).setCellValue(dataDO.getUnitPrice().replace("\n", ""));
//                if (comapnycode.equals("6560-FAC")) {
//                    row1.createCell(12).setCellValue("THB");
//                } else if (comapnycode.equals("62F0-FAC")) {
//                    row1.createCell(12).setCellValue("RUB");
//                } else {
//                    try {
//                        row1.createCell(12).setCellValue(dataDO.getCurrency().replace("\n", ""));
//                    } catch (Exception e) {
//                        row1.createCell(12).setCellValue(dataDO.getCurrency());
//                    }
//                }
//                if (comapnycode.equals("6560-FAC")) {
//                    row1.createCell(13).setCellValue(dataDO.getSOnumber());
//                    row1.createCell(14).setCellValue(dataDO.getGoodDescription());
//                }
//                row1.createCell(15).setCellValue("OK");
//                try {
//                    row1.createCell(16).setCellValue(dataDO.getPostingDate().replace("\n", ""));
//                } catch (Exception e) {
//                    row1.createCell(16).setCellValue(dataDO.getPostingDate());
//                }
//                row1.createCell(17).setCellValue(dataDO.getTaxCode());
//                row1.createCell(18).setCellValue(dataDO.getPurchaseOrderNumber());
//
//
//                row1.createCell(19).setCellValue(dataDO.getInvoicedate().replace("\n", ""));
//                row1.createCell(20).setCellValue("");
//                row1.createCell(21).setCellValue("");
//                row1.createCell(22).setCellValue(dataDO.getInvoiceReferenceNumber2());
//                row1.createCell(23).setCellValue("");
//
//            }
//            rowNum++;
//        }
//        try {
//            FileOutputStream fileOutputStream = new FileOutputStream(filename);
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
//
//}
