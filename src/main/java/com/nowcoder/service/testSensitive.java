package com.nowcoder.service;

import org.apache.commons.lang.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 12274 on 2018/5/16.
 */
@Service
public class testSensitive implements InitializingBean{
    private static final Logger logger= LoggerFactory.getLogger(testSensitive.class);
    private treeNode rootNode=new treeNode();
    private static final String SPLIT="***";
    @Override
    public void afterPropertiesSet() throws Exception {
        try{
            InputStream is=Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader reader=new InputStreamReader(is);
            BufferedReader bufferedReader=new BufferedReader(reader);
            String lineText;
            while((lineText=bufferedReader.readLine())!=null){
                lineText=lineText.trim();
                buildTree(lineText);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    private class treeNode{
        private Map<Character,treeNode> subNodes=new HashMap<>();
        private boolean end=false;
        public void addNode(Character c,treeNode node){
            subNodes.put(c,node);
        }
        public treeNode getSubNode(Character c){
            return subNodes.get(c);
        }
        public void setEnd(boolean isEnd){
            this.end=isEnd;
        }
        public boolean isEnd(){
            return end;
        }
        public long getNodeCount(){
            return subNodes.size();
        }
    }

    private void buildTree(String text){
        treeNode pointNode=rootNode;
        for(int i=0;i<text.length();i++){
            char c=text.charAt(i);
            if(isSymbol(c)){
                continue;
            }
            if(!pointNode.subNodes.containsKey(c)){
                treeNode node=new treeNode();
                pointNode.addNode(c,node);
                pointNode=node;
            }else{
                pointNode=pointNode.getSubNode(c);
            }
            if(i==text.length()-1){
                pointNode.setEnd(true);
            }
        }

    }

    public String filter(String text){
        if(text==null){
            return null;
        }
        StringBuilder result=new StringBuilder();
        int start=0;
        int point=0;
        treeNode pointNode=rootNode;
        while(point<text.length()){
            char c=text.charAt(point);
            if(isSymbol(c)){
                if(pointNode==rootNode){
                    result.append(c);
                    start++;
                }
                point++;
                continue;
            }
            if(pointNode.getSubNode(c)==null){
                result.append(text.charAt(start));
                point=start+1;
                start=point;
                pointNode=rootNode;
            }else{
                pointNode=pointNode.getSubNode(c);
                if(pointNode.isEnd()){
                    result.append(SPLIT);
                    point++;
                    start=point;
                    pointNode=rootNode;
                }else{
                    point++;
                }
            }
        }
        result.append(text.substring(start));
        return result.toString();
    }

    //判断是否是一个符号
    private boolean isSymbol(char c) {
        int ic = (int) c;
        // 0x2E80-0x9FFF 东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }
    public static void main(String[] argv) {
        testSensitive s=new testSensitive();
        s.buildTree("色se情");
        s.buildTree("色情");
        System.out.print(s.filter("色se色情 * 色*情"));
    }
}
