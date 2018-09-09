import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by 12274 on 2018/9/3.
 */
public class test3 {
    public static void print(Object obj){
        System.out.println(obj);
    }

    @Test
    public void test1(){
        Stack<String> stack=new Stack<String>();
        print("now Stack is"+stack.isEmpty());
        stack.push("1");
        stack.push("2");
        stack.push("3");
        stack.push("4");
        stack.push("5");
        print("now Stack is"+stack.isEmpty());
        print("Stack pop:"+stack.pop());
        print("Stack peek:"+stack.peek());
        print(stack.search("3"));
    }

    @Test
    public void test2(){
        Queue<String> queue=new LinkedList<String>();
        queue.offer("1");
        queue.offer("2");
        queue.offer("3");
        print(queue.size());
        print(queue.element());
        print(queue.poll());
        print(queue.size());
    }
}
