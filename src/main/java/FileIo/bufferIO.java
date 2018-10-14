package FileIo;

import java.io.*;
import java.io.FileReader;

public class bufferIO {
    public static void main(String[] args) throws IOException {
        //字节缓冲输出流
        File srcPath1 =new File("a.txt");
        File srcPath2 =new File("b.txt");
        File outPath =new File("c.txt");

        test3(srcPath1,srcPath2,outPath);
    }

    private static void test3(File srcPath1, File srcPath2, File outPath) throws IOException {
        SequenceInputStream sequenceInputStream =new SequenceInputStream(new FileInputStream(srcPath2),new FileInputStream(srcPath1));
        FileOutputStream outputStream =new FileOutputStream(outPath);
        byte[] buffer =new byte[1024];
        int len =-1;
        while ((len=sequenceInputStream.read(buffer))!=-1){
            outputStream.write(buffer);
        }
    }


    private static void test2(File srcPath, File outPath) throws IOException {
        Long start =System.currentTimeMillis();
        InputStream inputStreamReader =new FileInputStream(srcPath);
        OutputStream outputStream =new FileOutputStream(outPath);
        BufferedInputStream bufferedInputStream =new BufferedInputStream(inputStreamReader);
        BufferedOutputStream bufferedOutputStream =new BufferedOutputStream(outputStream);
        byte[] buffer =new byte[1024];
        int len =-1;
        while ((len=bufferedInputStream.read(buffer))!=-1){
            bufferedOutputStream.write(buffer);
        }
        inputStreamReader.close();
        outputStream.close();
        System.out.println(System.currentTimeMillis()-start);


    }

    //节点流输入
    private static void test1(File srcPath, File outPath) throws IOException {
        Long start =System.currentTimeMillis();
        InputStream inputStreamReader =new FileInputStream(srcPath);
        OutputStream outputStream =new FileOutputStream(outPath);
        byte[] buffer =new byte[1024];
        int len = inputStreamReader.read(buffer);
        while (len!=-1){
            outputStream.write(buffer);
            len=inputStreamReader.read(buffer);
        }
        System.out.println(System.currentTimeMillis()-start);
    }
    //包装六输入

}
