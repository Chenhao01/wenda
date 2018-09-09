package com.nowcoder.Util;
import org.apache.commons.lang.CharUtils;
import org.apache.struts.tiles.TilesRequestProcessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by 12274 on 2018/1/18.
 */
@Component
public class SensitiveFilter implements InitializingBean{
    private TireNode rootNode =new TireNode();//根节点
    private String replaceWords="***";
    @Override
    public void afterPropertiesSet() throws Exception {
        InputStream is=Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
        InputStreamReader read=new InputStreamReader(is);
        BufferedReader bufferedReader=new BufferedReader(read);
        String lineText;
        while ((lineText=bufferedReader.readLine())!=null){
            addSensitiveWords(lineText.trim());
        }
    }

    public class TireNode{
        private boolean isEnd=false;
        private Map<Character,TireNode> subNode=new HashMap<Character,TireNode>();

        public void setSubNode(char key, TireNode node){
            this.subNode.put(key,node);
        }
        public TireNode getSubNode(char key){
            return subNode.get(key);
        }

        public boolean isSensitiveEnd(){
            return this.isEnd;
        }
        public void setEnd(boolean end){
            this.isEnd=end;
        }
    }

    public void addSensitiveWords(String text){
        TireNode pointNode=null;
        pointNode=rootNode;//初始指向根节点
        for(int i=0;i<text.length();i++){
            Character c=text.charAt(i);
            if(pointNode.subNode.containsKey(c)){
                pointNode=pointNode.getSubNode(c);
            }else{
                TireNode tempNode=new TireNode();
                pointNode.setSubNode(c,tempNode);
                pointNode=tempNode;
            }
            if(i==text.length()-1){
                pointNode.setEnd(true);
            }
        }
    }

    public String filter(String text){
        TireNode pointNode=rootNode;
        int begin=0;
        int position=0;

        StringBuilder sb=new StringBuilder();
        if(text==null){
            return text;
        }
        for(int i=0;i<text.length();i++){
            Character c=text.charAt(i);
            if(pointNode.subNode.containsKey(c)){
                if(pointNode.getSubNode(c).isEnd){
                    position++;
                    begin=position;
                    sb.append(replaceWords);
                    pointNode=rootNode;
                }else{
                    position++;
                    pointNode=pointNode.getSubNode(c);
                }

            }else{
                if(isSymbol(c)){
                    if(position==begin){
                        sb.append(c);
                        begin++;
                    }
                    position++;
                }else{
                    sb.append(c);
                    begin++;
                    position++;
                }
            }
        }
        return sb.toString();
    }

    private boolean isSymbol(char c) {
        int ic = (int) c;
        // 0x2E80-0x9FFF 东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }
    public static void main(String[] argv) {
        SensitiveFilter s=new SensitiveFilter();
        s.addSensitiveWords("色情");
        StringBuilder sb=new StringBuilder("qwert");
        System.out.print(sb.replace(0,2,"***"));
        System.out.print(s.filter("*  *  *你好X******色* *情XX"));
    }
}
