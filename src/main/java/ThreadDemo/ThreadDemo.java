package ThreadDemo;


class Apple implements Runnable{
    private static int totalnum =500;
    @Override
    public void run() {
        this.eat();
    }

    /**
     * 使用了synchronized的
     */
    private synchronized void eat(){
        for (int i=0; i<50; i++){

            if (totalnum>0) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "线程吃了第" + totalnum-- + "个苹果");
            }
        }
    }

}

class MusicThread extends Thread{
    @Override
    public void run() {
        for (int i =0;i<5000;i++){
            System.out.println("听音乐");

        }
    }
}
public class ThreadDemo {
    public static void main(String[] args) {
        /**
         * 多个线程并发抢占一个资源的时候，可能出现线程不安全的问题
         *
         */
        Apple a = new Apple();
        new Thread(a,"小A").start();
        new Thread(a,"小B").start();
        new Thread(a,"小C").start();



    }
}
