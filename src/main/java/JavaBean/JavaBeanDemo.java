package JavaBean;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

public class JavaBeanDemo {
    public static void main(String[] args) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(UserDO.class,Object.class);
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor:pds){
            System.out.println("Bean的名称"+propertyDescriptor.getName());
            System.out.println("Bean的get方法"+propertyDescriptor.getReadMethod());
            System.out.println("Bean的set方法"+propertyDescriptor.getWriteMethod());
            System.out.println("-----------------------------------------");

        }
    }
}
