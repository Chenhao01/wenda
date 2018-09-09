package com.nowcoder.dao;

import com.nowcoder.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by 12274 on 2018/1/15.
 */
@Mapper
public interface QuestionDAO {
    String TABLE_NAME = "question";
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"INSERT INTO",TABLE_NAME,"(",INSERT_FIELDS,
            ") VALUES(#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    int addQuesttion(Question question);

    @Select({"SELECT",SELECT_FIELDS,"FROM",TABLE_NAME})
    List<Question> selectAllQuestion();

    @Select({"SELECT",SELECT_FIELDS,"FROM",TABLE_NAME,"WHERE id=#{id}"})
    Question selectById(@Param("id")int id);

    List<Question> selectLatestQuestions(@Param("userId")int userId,@Param("offset")int offset,@Param("limit")int limit);

    @Update({"UPDATE",TABLE_NAME,"SET comment_count=#{commentCount} WHERE id=#{id}"})
    void updateCommentCount(@Param("id")int id,@Param("commentCount")int commentCount);
}
