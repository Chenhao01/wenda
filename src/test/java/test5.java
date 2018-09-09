import org.junit.Test;

import java.io.File;

/**
 * Created by 12274 on 2018/9/4.
 */
public class test5 {


    @Test
    public void test(){
        File file=new File("d://Test1/");
        System.out.println(file.exists());

    }
}
