package Internet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class URLdemo {
    public static void main(String[] args) throws IOException {
        URL url = new URL("http://static.doc88.com/assets/images/page-loading.gif");
        URLConnection urlConnection =url.openConnection();
        InputStream inputStream =urlConnection.getInputStream();
        /**
         * 使用scanner扫描类进行文本的读取，此时scanner有点类似Iterator，如果存在下一行，则进行行的文本读取，输出行
         */
//        Scanner scanner =new Scanner(inputStream);
//        while (scanner.hasNext()){
//            String line =scanner.nextLine();
//            System.out.println(line);
//        }
//        scanner.close();
        /**
         * 使用bufferReader 进行缓存读取数据字节，需要将字节流转换成字符流，缓存读取的时候类似buffer
         */
//        BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
//        String buffer = bufferedReader.readLine();
//        while (buffer!=null){
//            System.out.println(buffer);
//            buffer=bufferedReader.readLine();
//        }
//        bufferedReader.close();
        /**
         * 时用buffer缓存进行数据读取，读取的和bufferReader类似的想过
         */
//        byte[] buffer =new byte[1024];
//        int len =inputStream.read(buffer);
//        while (len!=-1){
//            System.out.println(new String(buffer));
//            len=inputStream.read(buffer);
//        }
        Files.copy(inputStream, Paths.get("C:\\Users\\songyu\\Desktop\\爬虫测试文件\\2.gif"));
        inputStream.close();

    }
}
