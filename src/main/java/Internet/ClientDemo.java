package Internet;

import java.io.IOException;
import java.net.Socket;

public class ClientDemo {
    public static void main(String[] args) throws IOException {
        Socket socket =new Socket("localhost",8888);
    }
}
