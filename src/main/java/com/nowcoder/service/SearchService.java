package com.nowcoder.service;

import com.nowcoder.model.Question;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 12274 on 2018/5/10.
 */
@Service
public class SearchService {
    private static final String SOLR_URL="http://127.0.0.1:8983/solr/wenda";
    private SolrClient solrClient=new HttpSolrClient.Builder(SOLR_URL).build();
    private static final String QUESTION_TITLE_FIELD="question_title";
    private static final String QUESTION_CONTENT_FIELD="question_content";

    public List<Question> search(String keyWords,int offset,int limit,String hlPre,String hlPos)throws Exception{
        List<Question> questions=new ArrayList<>();
        SolrQuery solrQuery=new SolrQuery(keyWords);
        solrQuery.setStart(offset);
        solrQuery.setRows(limit);
        solrQuery.setHighlight(true);
        solrQuery.setHighlightSimplePre(hlPre);
        solrQuery.setHighlightSimplePost(hlPos);
        solrQuery.set("hl.fl", QUESTION_TITLE_FIELD + "," + QUESTION_CONTENT_FIELD);
        QueryResponse queryResponse=solrClient.query(solrQuery);
        for(Map.Entry<String,Map<String,List<String>>> entry : queryResponse.getHighlighting().entrySet()){
            Question question=new Question();
            question.setId(Integer.valueOf(entry.getKey()));
            if(entry.getValue().containsKey(QUESTION_TITLE_FIELD)){
                List<String> titles=entry.getValue().get(QUESTION_TITLE_FIELD);
                if(titles.size()>0){
                    question.setTitle(titles.get(0));
                }
            }
            if(entry.getValue().containsKey(QUESTION_CONTENT_FIELD)){
                List<String> comtents=entry.getValue().get(QUESTION_CONTENT_FIELD);
                if(comtents.size()>0){
                    question.setTitle(comtents.get(0));
                }
            }
            questions.add(question);
        }
        return questions;
    }
    public boolean indexQuestion(int qid, String title, String content) throws Exception {
        SolrInputDocument doc =  new SolrInputDocument();
        doc.setField("id", qid);
        doc.setField(QUESTION_TITLE_FIELD, title);
        doc.setField(QUESTION_CONTENT_FIELD, content);
        UpdateResponse response = solrClient.add(doc, 1000);
        return response != null && response.getStatus() == 0;
    }
}
