package FileIo;

import java.util.Properties;

public class PropertyDemo {
    public static void main(String[] args) {
        Properties properties =new Properties();
        properties.setProperty("username","songyu");
        properties.setProperty("password","123456");
        System.out.println(properties);
        System.out.println(properties.getProperty("username"));
        String value =properties.getProperty("username111","23465356453634");
        System.out.println(value);
    }
}
