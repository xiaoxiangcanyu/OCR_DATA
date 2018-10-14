package InvokeDemo;

import JavaBean.UserDO;

public class ApacheDemo {
    public static void main(String[] args) {
        /**
         * 把多个class文件压缩在一起就形成了jar包
         */
        UserDO userDO1 =new UserDO();
        userDO1.setName("张三");
        userDO1.setGender("男");
        userDO1.setMan(true);
    }
}
