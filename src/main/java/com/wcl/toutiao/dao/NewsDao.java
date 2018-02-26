package com.wcl.toutiao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.wcl.toutiao.model.News;

/**
 * @ClassName: NewsDao 
 * @Description: 通过配置文件映射mapper
 * @author Lulu
 * @date 2017年11月19日 下午10:25:19 
 */
@Mapper
public interface NewsDao  {
    String TABLE_NAME = "news";
    String INSERT_FIELDS = "title, link, image, like_count, comment_count, created_date, user_id";
    String SELECT_FIELDS = "id, title, link, image, like_count, comment_count, created_date, user_id";
    
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
        ") values(#{title}, #{link}, #{image}, #{likeCount}, #{commentCount}, #{createdDate}, #{userId})"})
    int addNews(News news);
    
    List<News> selectByUserIdAndOffset(@Param("userId") int userId,
            @Param("offset") int offset, @Param("limit") int limit);
    
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id = #{id}"})
    News selectById(@Param("id") int id);
    
    @Update({"update ", TABLE_NAME, " set comment_count = #{commentCount} where id = #{id}"})
    void updateCommentCount(@Param("commentCount") int commentCount, @Param("id") int newsId);
    
    @Update({"update " + TABLE_NAME + " set like_count = #{likeCount} where id = #{id}"})
    void updateLikeCount(@Param("likeCount") int likeCount, @Param("id") int newsId);
}
