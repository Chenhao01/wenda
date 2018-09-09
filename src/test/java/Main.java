/**
 * Created by 12274 on 2018/3/12.
 */
import javax.swing.text.html.HTMLDocument;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;
public class Main{
   /* public static void main(String[] args){
        Scanner input=new Scanner(System.in);
        System.out.println("请输入：");
        String s=input.next();
        System.out.println(s);
        System.out.println("请输入n：");
        int n=input.nextInt();
        int[] x=new int[n];
        System.out.println("请输入：");
        for(int i=0;i<n;i++){
            x[i]=input.nextInt();
        }
        int temp=0;
        for(int i=0;i<n;i++){
            for(int j=i;j<n;j++){
                if(x[i]>x[j]){
                    temp=x[i];
                    x[i]=x[j];
                    x[j]=temp;
                }
            }
        }
        for(int i=0;i<n;i++){
            System.out.println(x[i]);
        }
String s="";
        long[] i=new long[10];
        long[] t=new long[i.length];
    }*/
   public static void main(String[] args){
       Scanner input=new Scanner(System.in);
       int n=input.nextInt();
       ArrayList list=new ArrayList();
       for(int i=0;i<n;i++){
           list.add(input.nextLong());
           Collections.reverse(list);
       }
       Iterator i=list.iterator();
       while (i.hasNext()){
           System.out.print(String.valueOf(i.next())+" ");
       }
       /*for(int i=0;i<n;i++){
           if(i<n-1){
               System.out.print(String.valueOf(list.get(i))+" ");
           }else{
               System.out.print(String.valueOf(list.get(i)));
           }
       }*/
   }
}
