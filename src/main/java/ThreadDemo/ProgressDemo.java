package ThreadDemo;

import java.io.IOException;

public class ProgressDemo {
    public static void main(String[] args) throws IOException {
        //方式1
       Process process = Runtime.getRuntime().exec("notepad");
        //方式2
        ProcessBuilder processBuilder = new  ProcessBuilder("notepad");
        processBuilder.start();
    }
}
