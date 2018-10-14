package FileIo;

public class EnumDemo {
    public static void main(String[] args) {
        finalsuper finalsuper =new finalsuper();
        System.out.println(finalsuper.toString());

    }
}
 class finalsuper{
    public  void dowork(){
        System.out.println("父类的a是"+a+",b是"+b);
    };
    private String a;
    private String b;
     finalsuper(String a, String b) {
         this.a = a;
         this.b = b;
     }

      finalsuper() {
     }
     public  void dowork(String a){
         System.out.println("父类的a是"+a+",b是"+b);
     };

 }

class finalson extends finalsuper{

    @Override
    public void dowork(){
        System.out.println("子类在工作！");
    }
    public void doother(){
        System.out.println("子类在做别的事情!");
    }
};