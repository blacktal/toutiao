package com.wcl.toutiao.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.wcl.toutiao.model.User;

/**
 * @ClassName: UserDao 
 * @Description: TODO 
 * @author Lulu
 * @date 2017年11月19日 下午10:25:25 
 */
@Mapper
public interface UserDao {

    String TABLE_NAME = "user";
    String INSERT_FIELDS = "name, password, head_url, salt";
    String SELECT_FIELDS = "id, name, password, head_url, salt";

    //返回值为int，1是成功，0是失败
    @Insert({ "insert into ", TABLE_NAME, "(", INSERT_FIELDS, ")",
            "values(#{name}, #{password}, #{headUrl}, #{salt})" })
    int addUser(User user);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id = #{id}"})
    User getUserById(int id);
    
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where name = #{name}"})
    User getUserByName(String name);
    
    @Update({"update ", TABLE_NAME, " set password = #{password} where id = #{id}"})
    void updatePassword(User user);
    
    @Delete({"delete from ", TABLE_NAME, " where id = #{id}"})
    int deleteUserById(int id);

}
