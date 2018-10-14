package InvokeDemo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

class User{
    public User(){
        System.out.println("不带参数的构造器");
    };
    public User(String name){
        System.out.println("带实参的构造器"+name);
    };
    private User(String name,int age){
        System.out.println("带实参的构造器"+name+"年龄"+age);
    };
    public void dowork(){
        System.out.println("无参数正在工作!");
    }
    public static void dowork(String user){
        System.out.println(user+"正在工作!");
    }
    private void dowork(String user,int age){
        System.out.println(user+"年龄是"+age+"正在工作！");
    }
}
public class ClassInstanceDemo {
    private static Object all;

    public static void main(String[] args) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        /**
         * 反射就是运行时期，获取类中的方法，构造器等
         * 1.首先测试8大数据类型的class
         */
//        Class intclass =int.class;
//        Class boolenclass =boolean.class;
//        Class byteclass =byte.class;
//        Class shortclass =short.class;
//        Class longclass =long.class;
//        System.out.println(intclass);
//        System.out.println(boolenclass);
//        System.out.println(byteclass);
//        System.out.println(shortclass);
//        System.out.println(longclass);
        /**
         * 通过反射获取构造器创建对象
         */
//        getall();
//        getone();
//        createone();
        Method method=User.class.getDeclaredMethod("dowork", String.class);
        method.invoke(null,"111");

    }

    private static void createone() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

       Method method =User.class.getDeclaredMethod("dowork",String.class,int.class);
       method.setAccessible(true);
       method.invoke(User.class.newInstance(),"小黑",18);

    }

    public static void getall() {
        Class<User> userClass=User.class;
        Constructor<?>[] constructors =userClass.getDeclaredConstructors();
        for (Constructor constructor:
             constructors) {
            System.out.println(constructor);
        }

    }

    public static void getone() throws NoSuchMethodException {
        Class<User> userClass=User.class;
        System.out.println(userClass.getDeclaredConstructor(String.class,int.class));
    }
}
