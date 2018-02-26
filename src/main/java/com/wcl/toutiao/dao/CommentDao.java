package com.wcl.toutiao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.wcl.toutiao.model.Comment;

/**
 * @ClassName: CommentDao 
 * @Description: TODO 
 * @author Lulu
 * @date 2017年12月10日 上午11:10:11 
 */
@Mapper
public interface CommentDao {
    
    String TABLE_NAME = "comment";
    String INSERT_FIELDS = "content, user_id, entity_type, entity_id, created_date, status";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;
    
    @Insert({"insert into " + TABLE_NAME + "(" + INSERT_FIELDS
            + ") values(#{content}, #{userId}, #{entityType}, #{entityId}, #{createdDate}, #{status})"})
    int addComment(Comment comment);
    
    @Select({"select " + SELECT_FIELDS + " from " + TABLE_NAME + " where entity_type = #{entityType} and entity_id = #{entityId} order by id desc"})
    List<Comment> selectByEntityIndex(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Select({"select count(id) from ", TABLE_NAME, " where entity_type = #{entityType} and entity_id = #{entityId}"})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);
    
    @Update({"update ", TABLE_NAME, " set status = 1 where id = #{id}"})
    void deleteComment(@Param("id") int commentId);
}
