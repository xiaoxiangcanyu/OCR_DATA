package Internet;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpDemo {
    public static void main(String[] args) throws UnknownHostException {
        InetAddress ip =Inet6Address.getByName("DESKTOP-EJG02N2");
        System.out.println(ip);
        System.out.println(ip.getHostName());
        System.out.println(ip.getHostAddress());
        System.out.println(ip.getCanonicalHostName());

    }
}
