package FileIo;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class encode {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String code ="宇哥";
        byte[] bytes =code.getBytes("UTF-8");
        System.out.println(Arrays.toString(bytes));
        String encode = new String(bytes,"UTF-8");
        System.out.println(encode);
    }
}
