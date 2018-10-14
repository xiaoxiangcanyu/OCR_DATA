package FileIo;

import java.util.*;

public class Mapdemo {
    public static void main(String[] args) {
        Map<Character,Integer> map =new HashMap<>();
        String str ="sadjaskbjashdjashkdjaklsdjklasj";
        char[] chars =str.toCharArray();
        for (char c:
             chars) {
            if (map.containsKey(c)){
              int value = map.get(c);
              value++;
              map.put(c,value);
            }else {
                map.put(c,1);
            }
        }
        System.out.println(map);

    }
}
