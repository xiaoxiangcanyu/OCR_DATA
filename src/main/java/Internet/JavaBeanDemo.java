package Internet;

import JavaBean.UserDO;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class JavaBeanDemo {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IntrospectionException {
        UserDO userDO1 =new UserDO();
        userDO1.setName("张三");
        userDO1.setGender("男");
        userDO1.setMan(true);
        //JavaBean 转 map
       Map<String,Object> map = bean_map(userDO1);
//        System.out.println(map);
        //map转JavaBean
        UserDO obj =map_bean(map,UserDO.class);
        System.out.println(obj);
    }

    private static <T> T  map_bean(Map<String, Object> map,Class<T> Beantype) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, IntrospectionException {
        Object object =Beantype.newInstance();
        BeanInfo beanInfo =Introspector.getBeanInfo(Beantype,Object.class);
        PropertyDescriptor[] propertyDescriptors =beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor:propertyDescriptors){
            Object value = map.get(propertyDescriptor.getName());
            propertyDescriptor.getWriteMethod().invoke(object,value);
        }
        return (T)object;
    }

    /**
     * JavaBean转Map
     */
    private static Map<String,Object> bean_map(Object obj) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Map<String,Object> map =new HashMap<>();
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass(),Object.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor:propertyDescriptors){
            String name = propertyDescriptor.getName();
            Object value =propertyDescriptor.getReadMethod().invoke(obj);
            map.put(name,value);
        }
        return map;


    }
}
