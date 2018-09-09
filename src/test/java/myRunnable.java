/**
 * Created by 12274 on 2018/5/2.
 */
public class myRunnable implements Runnable{
    private Thread t;
    private String t_name;
    public myRunnable(String name){
        this.t_name=name;
    }

    public void run(){
        try{
            for(int i=0;i<5;i++){
                System.out.println(t_name+":"+String.valueOf(i));
            }
            System.out.println(t_name+":执行结束");
        }catch(Exception e){
            System.out.println(t_name+":"+e.getMessage());
        }
    }
    public void start(){
        if(t==null){
            t=new Thread(this,t_name);
            t.start();
        }
    }
}
