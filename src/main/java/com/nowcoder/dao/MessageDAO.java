package com.nowcoder.dao;

import com.nowcoder.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by 12274 on 2018/3/8.
 */
@Mapper
public interface MessageDAO {
    String TABLE_NAME="message";
    String INSERT_FIELD="from_id, to_id, content, has_read, conversation_id, created_date ";
    String SELECT_FIELD="id,"+INSERT_FIELD;

    @Insert({"INSERT INTO",TABLE_NAME,"(",INSERT_FIELD
            ,") VALUES(#{fromId},#{toId},#{content},#{hasRead},#{conversationId},#{createdDate})"})
    int addMessage(Message message);

    @Select({"SELECT",SELECT_FIELD,"FROM",TABLE_NAME,
            "WHERE conversation_id=#{conversationId}",
            "ORDER BY created_date DESC",
            "LIMIT #{offset},#{limit}"})
    List<Message> selectByConversationId(@Param("conversationId")String conversationId,
                                         @Param("offset")int offset,
                                         @Param("limit")int limit);

    @Select({"SELECT",SELECT_FIELD,"FROM",TABLE_NAME,
            "WHERE from_id=#{userId} OR to_id=#{userId}",
            "GROUP BY conversation_id DESC",
            "ORDER BY created_date DESC",
            "LIMIT #{offset},#{limit}"})
    List<Message> selectByUser(@Param("userId")int userId,
                                         @Param("offset")int offset,
                                         @Param("limit")int limit);
    @Select({"SELECT COUNT(id) FROM",TABLE_NAME,
            "WHERE has_read=0 AND to_id=#{userId} AND conversation_id=#{conversationId}"})
    int getUnreadCount(@Param("userId")int userId,
                       @Param("conversationId")String conversationId);

    @Select({"SELECT COUNT(id) FROM",TABLE_NAME,
            "WHERE conversation_id=#{conversationId}"})
    int getConversationCount(String conversationId);
}
