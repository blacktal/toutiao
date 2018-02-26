package com.wcl.toutiao.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.wcl.toutiao.model.LoginTicket;

/**
 * @ClassName: TicketDao
 * @Description: TODO
 * @author Lulu
 * @date 2017年11月26日 下午8:06:45
 */
@Mapper
public interface TicketDao {
    String TABLE_NAME = "login_ticket";
    String INSERT_FIELDS = "user_id, ticket, expired, status";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;
    
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
             ") values (#{userId}, #{ticket}, #{expired}, #{status})"})
    int addTicket(LoginTicket ticket);
    
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where ticket = #{ticket}"})
    LoginTicket selectByTicket(String ticket);
    
    @Update({"update ", TABLE_NAME, "set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("ticket") String ticket, @Param("status") int status);
}
