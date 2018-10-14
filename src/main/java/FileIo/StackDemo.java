package FileIo;

import java.util.ArrayDeque;

public class StackDemo {
    public static void main(String[] args) {
        ArrayDeque arrayDeque =new ArrayDeque();
        arrayDeque.push("A");
        arrayDeque.push("B");
        arrayDeque.push("C");
        System.out.println(arrayDeque.peek());
        arrayDeque.pop();
        System.out.println(arrayDeque.peek());
    }
}
