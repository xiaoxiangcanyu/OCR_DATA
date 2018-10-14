package ThreadDemo;

public class SleepDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("begin--------");
        for (int i=5;i>=0;i--){
            System.out.println("i="+i);
            Thread.sleep(1000);
        }
        System.out.println("BONG-------------");
    }
}
