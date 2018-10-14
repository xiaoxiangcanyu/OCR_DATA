package FileIo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayListDemo {
    public static void main(String[] args) {
        List list =new ArrayList();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        System.out.println(list);
//        for (Object e:list){
//            if ("C".equals(e)){
//                list.remove(e);
//            }
//        }


        for (Iterator iterator = list.iterator();iterator.hasNext();){
            Object e =iterator.next();
           if ("B".equals(e)){
               iterator.remove();
           }
        }
        System.out.println(list);
    }
}
