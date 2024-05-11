package com.example.big_event.controller;

import com.example.big_event.exception.UserException;
import com.example.big_event.message.Result;
import com.example.big_event.service.UserService;
import com.example.big_event.util.ThreadLocal;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Resource
    UserService userService;

    /**
     * @param userName 用户名
     * @param password 密码
     * @return 响应结果
     * @apiNote 用户注册
     */
    @PostMapping("/register")
    public Result register(@RequestParam("userName") String userName, @RequestParam("password") String password) {
        try {
            return new Result(true, "注册成功", userService.register(userName, password));
        } catch (UserException userException) {
            return new Result(false, userException.getMessage());
        }
    }

    /**
     * @param userName 用户名
     * @param password 密码
     * @return 响应结果
     * @apiNote 用户登录
     */
    @PostMapping("/login")
    public Result login(@RequestParam("userName") String userName, @RequestParam("password") String password, @RequestParam(value = "autoLogin", required = false) Integer life) {
        try {
            /*
             * only for debugging
             */
            System.out.println("username:"+userName+" password:"+password+" lifetime:"+life);

            return new Result(true, "登录成功", userService.userLogin(userName, password, life));
        } catch (UserException userException) {
            return new Result(false, userException.getMessage());
        }
    }

    /**
     * @return 响应结果
     * @apiNote 获取用户信息
     */
    @RequestMapping("/userInfo")
    public Result getUserInfo() {
        return (Result) ThreadLocal.getObject();
    }

    /**
     * @apiNote 更新用户昵称
     * @param nickname 用户昵称
     * @return 响应结果
     */
    @PostMapping("/updateNickname")
    public Result updateNickName(@RequestParam("nickname") String nickname){
        try{
            userService.updateUserNickName(nickname);
            return new Result(true,"用户昵称更新成功");
        }catch (UserException userException){
            return new Result(false,"用户昵称更新失败",userException.getMessage());
        }
    }

    /**
     * @apiNote 更新用户邮箱
     * @param email 用户邮箱
     * @return 响应结果
     */
    @PostMapping("/updateEmail")
    public Result updateUserEmail(@RequestParam("email") String email){
        try{
            userService.updateUserEmail(email);
            return new Result(true,"用户邮箱更新成功");
        }catch (UserException userException){
            return new Result(false,"用户邮箱更新失败",userException.getMessage());
        }
    }

    /**
     * @param pic 用户头像
     * @param absolutePath 服务器本地存储的绝对基路径
     * @param supportedFormatRegexp 支持的文件类型（是一个正则表达式）
     * @return 响应结果
     * @apiNote 更新用户头像
     */
    @PutMapping("/updateUserPicture")
    public Result updateUserPicture(MultipartFile pic,
                                    @Value("${upload.absolute-path}") String absolutePath,
                                    @Value("${upload.supported-format-regexp}") String supportedFormatRegexp) {
        return userService.updateUserPic(pic,absolutePath,supportedFormatRegexp);
    }

    /**
     * @param password     新密码
     * @param confirmation 再次确认的新密码
     * @return 响应结果
     * @apiNote 更新用户密码
     */
    @PutMapping("/changePassword")
    public Result changePassword(@RequestParam("newPassword") String password, @RequestParam("confirmation") String confirmation) {
        try {
            return new Result(true, "密码更新成功", userService.ChangePassword(password, confirmation));
        } catch (UserException userException) {
            return new Result(false, "密码更新失败", userException.getMessage());
        }
    }
}
