package com.nowcoder.dao;

import com.nowcoder.model.User;
import com.nowcoder.model.testUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by 12274 on 2018/9/3.
 */
@Mapper
public interface testUserDao {
    String TABLE_NAME="user";
    String SELECT_FIELDS="id,name,password,salt,head_url";
    String INSERT_FIELDS="name,password,salt,head_url";

    @Insert({"INSERT INTO",TABLE_NAME,"(",INSERT_FIELDS,
            ") VALUES(#{name},#{password},#{salt},#{headUrl})"})
    int addUser(testUser user);

    @Select({"SELECT",SELECT_FIELDS,"FROM",TABLE_NAME})
    List<testUser> selectAllUser();

    @Select({"SELECT",SELECT_FIELDS,"FROM",TABLE_NAME,"WHERE id=#{id}"})
    testUser selectUserById(@Param("id") int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where name=#{name}"})
    testUser selectByName(String name);
}
