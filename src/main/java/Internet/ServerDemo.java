package Internet;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerDemo {
    public static void main(String[] args) throws IOException {
        //创建服务端，制定端口为8888
     ServerSocket serverSocket= new ServerSocket(8888);
        System.out.println("连接就绪");
        Socket client =serverSocket.accept();
        System.out.println("连接过来的主机ip是:"+client.getInetAddress());
    }
}
