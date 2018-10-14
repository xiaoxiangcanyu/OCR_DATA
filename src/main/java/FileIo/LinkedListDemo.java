package FileIo;

import java.util.LinkedList;

public class LinkedListDemo {
    public static void main(String[] args) {
        LinkedList linkedList =new LinkedList();
        linkedList.addFirst("A");
        linkedList.addFirst("B");
        linkedList.add(1,"C");
        System.out.println(linkedList);
        linkedList.removeFirst();
        System.out.println(linkedList.get(0));

    }
}
