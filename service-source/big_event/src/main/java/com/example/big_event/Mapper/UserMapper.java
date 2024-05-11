package com.example.big_event.Mapper;

import com.example.big_event.entry.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    /**
     * @param userName 待查找用户的用户名
     * @return 若返回值不为null，则表示找得到，否则表示找不到对应的用户
     * @apiNote 按照给定的用户名查找用户信息
     */
    @Select("SELECT * FROM user WHERE userName=#{userName}")
    User findByUserName(String userName);

    /**
     * @param userName 用户名
     * @param password 密码
     * @apiNote 将用户信息写入数据库中
     */
    @Insert("INSERT INTO user(userName,password,createTime,updateTime) VALUES(#{userName},#{password},now(),now())")
    void addUser(String userName, String password);

    /**
     * @apiNote 更新用户昵称
     * @param nickName 用户昵称
     * @param userName 用户名
     */
    @Update("UPDATE user SET nickName = #{nickName} WHERE userName = #{userName}")
    void updateUserNickname(String nickName, String userName);

    /**
     * @apiNote 更新邮箱
     * @param email 用户邮箱
     * @param userName 用户名
     */
    @Update("UPDATE user SET email = #{email} WHERE userName = #{userName}")
    void updateUserEmail(String email,String userName);

    /**
     * @apiNote 更新用户头像
     * @param userName 用户名
     * @param picture 用户头像URL
     */
    @Update("UPDATE user SET pic = #{picture} WHERE userName = #{userName}")
    void updateUserPicture(String userName, String picture);

    /**
     * @apiNote 修改用户密码
     * @param userName 用户名
     * @param password 新密码
     */
    @Update("UPDATE user SET password = #{password} WHERE userName = #{userName}")
    void changePassword(String userName, String password);
}
