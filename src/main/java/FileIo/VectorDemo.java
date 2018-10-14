package FileIo;

import java.util.Date;
import java.util.Vector;

public class VectorDemo {
    public static void main(String[] args) {
//        testadd();
//        testdelete();
//        testsearch();
        testupdate();

    }

    private static void testupdate() {
        Vector vector =new Vector();
        vector.add(1);
        vector.add(2);
        vector.add(3);
        System.out.println(vector);
        Vector vector1 =new Vector();
        vector1.add(4);
        vector1.add(5);
        vector1.add(6);
        vector.set(1,vector1);
        System.out.println(vector.elementAt(1));

    }

    private static void testsearch() {
        Vector vector =new Vector();
        vector.add("A");
        vector.add("B");
        vector.add("C");
        System.out.println(vector.get(2));
    }

    private static void testdelete() {
         Vector vector =new Vector();
         vector.add("A");
         vector.add("B");
         vector.add("C");
//         vector.add("B");
         vector.add("D");
         Vector vector1 =new Vector();
         vector1.add("A");
         vector1.add("B");
         vector.retainAll(vector1);
        System.out.println(vector);
    }

    private static void testadd() {
        Vector<Object> vector =new Vector<Object>(5);
        vector.add("23");
        vector.add(new Date());
        vector.add(123);

        Vector<Integer> vector1 =new Vector<>();
        vector1.add(1);
        vector1.add(2);
        vector1.add(3);
        vector.addAll(vector1);
        Vector<Integer> vector2 =new Vector<>();
        vector2.add(3);
        vector2.add(4);
        vector2.add(5);
        vector.addAll(vector2);
        System.out.println(vector);
    }
}
