import javafx.collections.ArrayChangeListener;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by 12274 on 2018/7/11.
 */
public class Test1 {
    List<Map<String,String>> list=new ArrayList<>();
    BlockingQueue<String> q= new LinkedBlockingQueue<String>(10);

    public static int test(){
        try{
            System.out.println("try");
            int i=10/0;
            return 0;
        }catch (Exception e){
            System.out.println("catch");
            return 1;
        }finally {
            System.out.println("finally");
            //return 2;
        }
    }

    @Test
    public void main(){
        System.out.println(test());
    }
}
