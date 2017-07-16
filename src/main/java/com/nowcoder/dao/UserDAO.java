package com.nowcoder.dao;

import com.nowcoder.model.User;
import org.apache.ibatis.annotations.*;

/**
 * Created by Shirley on 2017/7/13.
 */
@Mapper
public interface UserDAO {
    String TABLE_NAME="user";
    String INSERT_FIELDS="name,password,salt,head_url";
    String SELECT_FIELDS="id,name,password,salt,head_url";

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,        //通过注解的方式配置
            ") values(#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user) ;//添加用户信息

    @Select({"select ", SELECT_FIELDS," from ",TABLE_NAME," where id=#{id}"})
    User selectById(int id);//用户信息查询  通过id

    @Select({"select ", SELECT_FIELDS," from ",TABLE_NAME," where name=#{name}"})
    User selectByName(String name);//用户信息查询 通过姓名

    @Update({"update ",TABLE_NAME, " set password=#{password} where id=#{id}"})
    void updatePassword(User user);//更改用户密码

    @Delete({"delete from ",TABLE_NAME," where id=#{id}"})
    void deleteById(int id);//删除用户信息


}
