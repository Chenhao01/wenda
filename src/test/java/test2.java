import jdk.nashorn.internal.ir.WhileNode;
import org.junit.Test;

import javax.swing.text.html.HTMLDocument;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 12274 on 2018/8/30.
 */
public class test2 {
    public static void print(Object obj){
        System.out.println(obj);
    }
    @Test
    public void test1()throws IOException{
        /*List<String> list = new ArrayList<String>();
        list.add("java");
        list.add("c++");
        Iterator i=list.iterator();
        while(i.hasNext()){
            print(i.next());
        }*/
        FileOutputStream fos=new FileOutputStream(new File("D:\\File.txt"));
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        demo d=new demo("小明",20,"男",13000);
        oos.writeObject(d);
    }

    @Test
    public void test2()throws Exception{
        FileInputStream fis=new FileInputStream(new File("D:\\File.txt"));
        ObjectInputStream ois=new ObjectInputStream(fis);
        demo d=(demo)ois.readObject();
        print(d.getName()+"-"+d.getSex()+"-"+d.getAge());
    }
}
