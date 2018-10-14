package Internet;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class EncoderDemo {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String str ="宋宇是个大帅哥";
        String code = URLEncoder.encode(str,"GBK");
        System.out.println(code);
        String decode = URLDecoder.decode(code,"GBK");
        System.out.println(decode);
    }
}
