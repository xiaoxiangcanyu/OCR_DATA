package FileIo;

import DataClean.DataDO;

import java.io.*;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
 class FileIO{
     public static void main(String[] args) throws IOException {

            test();

//
     }
     public static void test()   {
         String filepath = "C:\\Users\\songyu\\Desktop\\IO_Test\\";
         String outfilepath = "C:\\Users\\songyu\\Desktop\\IO_Test\\";
         File[] files = new File(filepath).listFiles(new FileFilter() {
             @Override
             public boolean accept(File pathname) {
                 return pathname.getName().endsWith(".txt");
             }
         });
         for (File file:files) {
             String filename=file.getName();
             System.out.println(filename);
             File outputfile = new File(outfilepath,filename.substring(0,filename.indexOf("."))+".xls");
                 try(
                         FileInputStream fileInputStream = new FileInputStream(file);
                         FileOutputStream fileOutputStream =new FileOutputStream(outputfile);
                 ) {
                    byte[] buffer =new byte[1024];
                    int len=fileInputStream.read(buffer);
                    while (len!=-1){
                        System.out.println("刚开始的buffer是"+Arrays.toString(buffer));
                        fileOutputStream.write(buffer,0,len);
                        len=fileInputStream.read(buffer);
                        System.out.println("结束时候的buffer是"+Arrays.toString(buffer));
//                        System.out.println("运行到这里的时候:lem为"+len);
                    }

                 }catch (IOException e){
                     e.printStackTrace();
                 }

         }


     }
 }