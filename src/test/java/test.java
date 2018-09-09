/**
 * Created by 12274 on 2018/5/2.
 */
public class test {
    public static void t1(){
        myRunnable r=new myRunnable("r");
        Thread t=new myThread("t");
        r.start();
        t.start();
    }
    public static synchronized void t2(){
        Thread t=new myThread("t2");
        t.start();
    }
    public static void t3(){
        t2();
    }
    public static void t4(){
        t2();
    }
    public static void t5(){
        int array[][]={{1,2,3},{4,5,6},{7,8,9}};
        /*for(int i=0;i<array.length;i++){
            for(int j=0;j<array[i].length;j++){
                array[i][j]=(i+1)*(j+1);
            }
        }*/
        for(int i=0;i<array.length;i++){
            for(int j=0;j<array[i].length;j++){
                System.out.print(array[i][j]);
            }
            System.out.print("\n");
        }

    }
    public static void t6(){
        StringBuffer str=new StringBuffer("ad asda asd a");
        while (str.indexOf(" ")!=-1){
            int i=str.indexOf(" ");
            str.replace(i,i+1,"%20");
        }
        System.out.println(str);
    }
    public static Thread t1=new Thread(){
      public void run(){
          for(int i=0;i<=10;i++){
              System.out.println("t1:正在下载图片:"+i*10+"%");
              try{
                  Thread.sleep(100);
              }catch (InterruptedException e){

              }
          }
          System.out.println("t1:图片下载完成");
      }
    };
    public static Thread t2=new Thread(){
      public void run(){
          System.out.println("t2:等待图片下载");
          try{
              t1.join();
          }catch (InterruptedException e){

          }
          System.out.println("t2:显示图片");
      }
    };
    public static void main(String[] args){
        t1.start();
        t2.start();
    }
}
