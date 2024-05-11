package com.example.big_event.service;

import com.example.big_event.Mapper.UserMapper;
import com.example.big_event.entry.User;
import com.example.big_event.exception.UserException;
import com.example.big_event.message.Result;
import com.example.big_event.util.JWT;
import com.example.big_event.util.ThreadLocal;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@CrossOrigin
public class UserService {
    @Resource
    JWT jwt;

    @Resource
    UserMapper userMapper;



    /**
     * @param userName 指定的用户名
     * @return 如果存在用户信息，返回非null值，否则返回null值
     * @apiNote 指定用户名，查找用户信息
     */
    public User getByUserName(String userName) {
        return userMapper.findByUserName(userName);
    }

    /**
     * @param userName 用户名
     * @param password 密码
     * @return User 新用户的用户信息
     * @throws UserException 如果用户名不符合规则、密码不符合规则或者用户已经存在，则抛出该异常
     * @apiNote 将用户信息写入数据库中
     */
    public User register(String userName, String password) throws UserException {
        //验证用户名和密码是否符合规则
        if (!this.verifyUserName(userName)) {
            throw new UserException("用户名不符合规则");
        }
        if (!this.verifyPassword(password)) {
            throw new UserException("密码不符合规则");
        }

        //验证用户是否已经存在，因为用户名唯一
        if (this.getByUserName(userName) != null) {
            throw new UserException("用户已经存在");
        }

        //添加用户信息
        userMapper.addUser(userName, password);
        User user = this.getByUserName(userName);
        if (user == null) {
            throw new UserException("数据库问题");
        }
        return user;
    }

    /**
     * @param password 需要验证的密码
     * @return 如果密码符合规则，返回true，否则返回false
     * @apiNote 验证密码是否符合规则。规则：密码最少8字符，最多50字符，并且至少存在大写字母、小写字母、数字和可见字符。
     */
    private boolean verifyPassword(String password) {
        if (password.length() > 50 || password.length() < 8) {
            return false;
        }
        //记录大写字母数量
        int majuscule = 0;
        //记录小写字母数量
        int minucule = 0;
        //记录数字数量
        int numeral = 0;
        //记录其他可见字符数量
        int otherVisibleCharacters = 0;
        //密码（数组形式）
        byte[] passwordBytes = password.getBytes();

        //记录各类型字符的数量
        for (byte b : passwordBytes) {
            if (b >= (byte) 20 && b <= (byte) 126) {  //可见字符范围
                if (b >= (byte) 48 && b <= (byte) 57) {   //数字范围
                    numeral++;
                } else if (b >= (byte) 65 && b <= (byte) 90) { //大写字母范围
                    majuscule++;
                } else if (b >= (byte) 97 && b <= (byte) 122) {    //小写字母范围
                    minucule++;
                } else {  //其他可见字符范围
                    otherVisibleCharacters++;
                }
            } else {
                return false;   //不允许存在不可见字符
            }
        }

        //判断是否符合规则
        return ((majuscule != 0) && (minucule != 0) && (numeral != 0) && (otherVisibleCharacters != 0));
    }

    /**
     * @param userName 需要验证的用户名
     * @return 如果用户名符合规则，返回true，否则返回false。
     * @apiNote 验证用户名是否符合规则。规则：用户名不能为空，并且用户名最多有50个字符。
     */
    private boolean verifyUserName(String userName) {
        return (userName != null) && (userName.length() >= 1) && (userName.length() <= 50);
    }

    /**
     * @param nickName 用户昵称
     * @return 如果用户昵称符合规范，则返回true，否则返回false
     * @apiNote 校验用户的昵称是否符合规范
     */
    private boolean verifyNickName(String nickName) {
        return (nickName != null) && (nickName.length() >= 1) && (nickName.length() <= 50);
    }

    /**
     * @param email 用户邮箱
     * @return 如果用户邮箱符合规范，则返回true，否则返回false
     * @apiNote 校验用户的邮箱是否符合规范
     */
    private boolean verifyEmail(String email) {
        String pattern = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(email);
        return m.matches();
    }


    /**
     * @param nickname 用户昵称
     * @throws UserException 如果用户昵称不符合规则，则抛出该异常
     * @apiNote 更新用户昵称
     */
    public void updateUserNickName(String nickname) throws UserException {
        //验证用户昵称是否符合规范
        if (!this.verifyNickName(nickname)) {
            throw new UserException("用户昵称不符合规则");
        }

        //更新用户昵称
        userMapper.updateUserNickname(nickname, ((User) ((Result) (ThreadLocal.getObject())).getData()).getUserName());
    }

    /**
     * @param email 用户邮箱
     * @throws UserException 如果用户邮箱不符合规范，则抛出该异常
     * @apiNote 更新用户邮箱
     */
    public void updateUserEmail(String email) throws UserException {
        //验证用户邮箱是否符合规范
        if (!this.verifyEmail(email)) {
            throw new UserException("用户邮箱不符合规则");
        }

        //更新用户邮箱
        userMapper.updateUserEmail(email, ((User) ((Result) (ThreadLocal.getObject())).getData()).getUserName());
    }

    /**
     * @apiNote 更新用户头像
     * @param pic 用户头像
     * @param absolutePath 服务器本地存储的绝对基路径
     * @param supportedFormatRegexp 支持的文件格式（是一个正则表达式）
     * @return 响应结果
     */
    public Result updateUserPic(MultipartFile pic, String absolutePath, String supportedFormatRegexp
    ) {
        try {
            System.out.println(pic);
            //验证文件类型
            String originalFilename = pic.getOriginalFilename();
            Pattern r = Pattern.compile(supportedFormatRegexp);
            Matcher m = r.matcher(originalFilename);
            if (!m.find()) {
                return new Result(false, "不支持的文件类型",originalFilename);
            }
            //为每一个用户单独创建一个存储文件夹
            String UID = (((User) ((Result) (ThreadLocal.getObject())).getData()).getId()).toString();
            String relativePath = UID + "\\user-picture";   //用户文件的相对基路径
            String dirPath = absolutePath + relativePath;
            File file = new File(dirPath);
            //如果文件路径不存在，那么创建它
            if (!file.exists()) {
                //file.mkdir();
                file.mkdirs();
            }
            String filePath = dirPath + "\\" + originalFilename;
            pic.transferTo(new File(filePath));
            userMapper.updateUserPicture(((User) ((Result) (ThreadLocal.getObject())).getData()).getUserName(),relativePath + "\\" + originalFilename);
            return new Result(true, "图片上传成功", file);
        } catch (IOException e) {
            return new Result(false, "文件路径错误", e.getMessage());
        } catch (MaxUploadSizeExceededException maxUploadSizeExceededException) {
            return new Result(false, "上传文件太大，最大允许1GB的文件", maxUploadSizeExceededException.getMessage());
        }catch (NullPointerException nullPointerException){
            return new Result(false,"图片不能为空",nullPointerException.getMessage());
        }
    }

    /**
     * @param picture 用户头像
     * @return 如果用户头像字符串是一个标准的URL格式，返回true，否则返回false
     * @apiNote 验证用户头像是否为一个URL格式
     * @deprecated
     */
    private boolean verifyUserPic(String picture) {
        String pattern = "^((https|http|ftp|rtsp|mms)?:\\/\\/)[^\\s]+";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(picture);
        return m.matches();
    }

    /**
     * @param password     新密码
     * @param confirmation 二次确认的新密码
     * @return 修改密码成功后，生成的新JWT
     * @throws UserException 如果新密码和二次确认的密码不相同、新密码和旧密码相同、新密码不符合规则，抛出该异常
     * @apiNote 更新密码
     */
    public String ChangePassword(String password, String confirmation) throws UserException {
        //验证新密码和二次输入的新密码是否相同
        if (!password.equals(confirmation)) {
            throw new UserException("新密码和二次输入的新密码不相同");
        }

        //验证新密码是否符合规则
        if (!this.verifyPassword(password)) {
            throw new UserException("新密码不符合规则");
        }

        //验证新密码和旧密码是否相同
        if (password.equals(((User) (((Result) ThreadLocal.getObject()).getData())).getPassword())) {
            throw new UserException("新密码和旧密码不能相同");
        }

        //更新用户密码
        userMapper.changePassword(((User) ((Result) (ThreadLocal.getObject())).getData()).getUserName(), password);

        //重新生成JWT
        return jwt.createJWT(((User) ((Result) (ThreadLocal.getObject())).getData()).getUserName(), password, null);
    }

    /**
     * @param userName 用户名
     * @param password 密码
     * @param life     令牌的生命周期，若该参数为空，则使用默认值
     * @return 登录成功后生成的JWT
     * @apiNote 用户登录
     */
    public String userLogin(String userName, String password, Integer life) throws UserException {
        //验证用户名和密码是否符合规则
        if (!this.verifyUserName(userName)) {
            throw new UserException("用户名不符合规则");
        }
        if (!this.verifyPassword(password)) {
            throw new UserException("密码不符合规则");
        }

        //验证用户是否存在
        User user = this.getByUserName(userName);
        if (user == null) {
            throw new UserException("用户不存在");
        }

        //验证用户密码是否匹配
        if (!user.getPassword().equals(password)) {
            throw new UserException("用户密码错误");
        }

        return jwt.createJWT(userName, password, life);
    }
}
