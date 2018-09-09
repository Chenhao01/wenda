package com.nowcoder.Util;

import org.apache.commons.lang.CharUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 12274 on 2018/9/5.
 */
@Component
public class sensitive implements InitializingBean{
    private Tree rootNode=new Tree();
    private String replaceWords="***";
    @Override
    public void afterPropertiesSet() throws Exception {
        InputStream is=Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
        InputStreamReader isr=new InputStreamReader(is);
        BufferedReader reader=new BufferedReader(isr);
        String lineText=null;
        while((lineText=reader.readLine())!=null){
            buildTree(reader.readLine());
        }
    }
    class Tree{
        private boolean isEnd=false;
        private Map<Character,Tree> subNodes=new HashMap<>();
         public void setSubNode(char c,Tree node){
             subNodes.put(c,node);
         }
         public Tree getSubNode(char c){
             return subNodes.get(c);
         }

         public void setEnd(boolean end){
             this.isEnd=end;
         }
         public boolean isEnd(){
             return this.isEnd;
         }
    }

    public void buildTree(String text){
        Tree pointNode=null;
        pointNode=rootNode;
        for(int i=0;i<text.length();i++){
            char c=text.charAt(i);
            if(!pointNode.subNodes.containsKey(c)){
                Tree newNode=new Tree();
                pointNode.setSubNode(c,newNode);
                pointNode=newNode;
            }else{
                pointNode=pointNode.getSubNode(c);
            }
            if(i==text.length()-1){
                pointNode.setEnd(true);
            }
        }
    }

    public String filter(String text){
        StringBuilder result=new StringBuilder();
        Tree pointNode=rootNode;
        int start=0;
        int point=0;
        if(text==null){
            return text;
        }
        while(point<text.length()){
            char c=text.charAt(point);
            if(pointNode.subNodes.containsKey(c)){
                if(!pointNode.getSubNode(c).isEnd()){
                    pointNode=pointNode.getSubNode(c);
                    point++;
                }else{
                    result.append(replaceWords);
                    point++;
                    start=point;
                    pointNode=rootNode;
                }
            }else{
                if(isSymbol(c)){
                    if(start==point){
                        result.append(c);
                        start++;
                        point++;
                    }else{
                        point++;
                    }
                }else{
                    result.append(text.charAt(start));
                    start++;
                    point=start;
                    pointNode=rootNode;
                }
            }
        }
        return result.toString();
    }

    private boolean isSymbol(char c) {
        int ic = (int) c;
        // 0x2E80-0x9FFF 东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }
    public static void main(String[] args){
        sensitive s=new sensitive();
        s.buildTree("赌博");
        s.buildTree("色情");
        System.out.println(s.filter("88* * 赌 *du 博 色*情"));
    }
}
