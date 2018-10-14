package FileIo;

import java.util.Arrays;
import java.util.List;

public class ArraysDemo {
    public static void main(String[] args) {
        //数组转换对象
        List<String> list = Arrays.asList("A","B","C","D");
        String[] strings = new String[]{"A","B","C","D"};
        System.out.println(Arrays.toString(strings));
        System.out.println(list);
    }
}
