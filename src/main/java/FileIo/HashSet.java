package FileIo;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class HashSet {
    public static void main(String[] args) {

//        Set<String> strings =new java.util.LinkedHashSet<>();
//        strings.add("1");
//        strings.add("A");
//        strings.add("3");
//        System.out.println(strings);
        //SortSet可排序的集合,NavigableSet可做范围查询，TreeSet
        /**
         * TreeSet可以做范围查询和排序
         * 底层是红黑树算法
         */
        TreeSet<String> strings =new TreeSet<>();
        strings.add("A");
        strings.add("B");
        strings.add("C");
        strings.add("1");
        strings.add("9");
        System.out.println(strings);
        System.out.println(strings.first());
        System.out.println(strings.last());
        System.out.println(strings.headSet("B"));
        System.out.println(strings.tailSet("B"));


    }
}
