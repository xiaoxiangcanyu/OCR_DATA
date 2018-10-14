package FileIo;

import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("all")
public class GenericTypeDemo {
    public static void main(String[] args) {
        List<Number> list =new ArrayList<>();
        dowork(list);
        List<Integer> integers =new ArrayList<>();
        List<Object> objects =new ArrayList<>();
        dowork(objects);
    }
    @Deprecated
    private static void dowork(List<? super Number> list) {

    }
}
