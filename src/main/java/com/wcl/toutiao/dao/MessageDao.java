package com.wcl.toutiao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.wcl.toutiao.model.Message;

/**
 * @ClassName: MessageDao 
 * @Description: TODO 
 * @author Lulu
 * @date 2017年12月13日 上午9:17:17 
 */
@Mapper
public interface MessageDao {
    String TABLE_NAME = "message";
    String INSERT_FIELDS = "content, from_id, to_id, conversation_id, created_date, has_read";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;
    
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") values (#{content}, #{fromId}, #{toId}, #{conversationId}, #{createdDate}, #{hasRead})"})
    int addMessage(Message message);
    
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where conversation_id = #{conversationId} order by created_date desc"})
    List<Message> selectMessageByConversationId(@Param("conversationId") String conversationId);
    
    /**   
     * @Title: selectConversationList
     * @Description: 同一个conversation中取最新的一条，group操作
     * @param: @param userId
     * @param: @return      
     * @return: List<Message>      
     * @throws   
     */
    //@Select({"select *, count(id) as cn from (select * from message where from_id = 2 or to_id =2 order by id asc)tt group by conversation_id desc"})
    @Select({"select ", SELECT_FIELDS, ", count(id) as id from (select ", SELECT_FIELDS, " from ",
        TABLE_NAME, " where from_id = #{userId} or to_id = #{userId} order by id desc)tt group by conversation_id desc"})
    List<Message> selectConversationList(@Param("userId") int userId);
    
    @Select({"select count(id) from ", TABLE_NAME, " where conversation_id = #{conversationId}"})
    int countMsgByConversationId(@Param("conversationId") String conversationId);
    
    // conversation中发给当前用户的消息中，未读消息的数量
    @Select({"select count(id) from ", TABLE_NAME, " where to_id = #{userId} and conversation_id = #{conversationId}"})
    int countUnreadMsg(@Param("userId") int userId, @Param("conversationId") String conversationId);
}
