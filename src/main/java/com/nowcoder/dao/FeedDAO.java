package com.nowcoder.dao;

import com.nowcoder.model.Feed;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by 12274 on 2018/3/28.
 */
@Mapper
public interface FeedDAO {
    String TABLE_NAME="feed";
    String INSERT_FILED="user_id,data,created_date,type";
    String SELECT_FILED="id,"+INSERT_FILED;

    @Insert({"INSERT INTO",TABLE_NAME,"(",INSERT_FILED,
            ") VALUE (#{userId},#{data},#{createdDate},#{type})"})
    int addFeed(Feed feed);

    @Select({"SELECT",SELECT_FILED,"FROM",TABLE_NAME,"WHERE id=#{id}"})
    public Feed getFeed(@Param("id")int id);

    @Select({"SELECT",SELECT_FILED,"FROM",TABLE_NAME,"WHERE user_id=#{userId}"})
    public List<Feed> getFeedByUserId(int userId);

    List<Feed> selectUserFeeds(@Param("maxId")int maxId,
                               @Param("userIds")List<Integer> userIds,
                               @Param("count")int count);

}
