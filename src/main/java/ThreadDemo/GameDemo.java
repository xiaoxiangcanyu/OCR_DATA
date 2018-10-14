package ThreadDemo;

public class GameDemo {
    public static void main(String[] args) {
        /**
         * 并行和并发
         * 并行是指两个或者多个事情在同一时刻发生
         * 并行是指两个或多个事情在同一时间段内发生
         * 进程指的就是运行的程序，一个进程包含多线程，线程是指进程中的一个运行单元
         * 一个进程至少得有一个线程
         * Runtime类
         */
        PlayGame();
        Playmusic();
    }

    private static void Playmusic() {
        for (int i=0;i<50;i++) {
            System.out.println("播放音乐-------------------");
        }
    }

    private static void PlayGame() {
        for (int i=0;i<50;i++){
            System.out.println("打游戏---------------------");
        }
    }
}
