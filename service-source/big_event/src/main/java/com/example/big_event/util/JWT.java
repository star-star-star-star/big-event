package com.example.big_event.util;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.big_event.Mapper.UserMapper;
import com.example.big_event.entry.User;
import com.example.big_event.exception.JWTAnalysisException;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @apiNote 生成JWT令牌以及对JWT令牌进行解析
 * @since 1.0.0
 */
@Component
public class JWT {

    @Value("${JWT-token.private-key}")
    private String private_key;

    @Value("${JWT-token.duration}")
    private Integer duration;

    @Value("${upload.absolute-path}")
    String absolutePath;

    @Resource
    UserMapper userMapper;

    /**
     * @param userName 用户名
     * @param password 密码
     * @param life     令牌有效时间，若该参数为空则使用默认值
     * @return 生成的JWT串
     * @apiNote 生成JWT
     */
    public String createJWT(String userName, String password, Integer life) {
        Map<String, String> claims = new HashMap<>();
        claims.put("userName", userName);
        claims.put("password", password);

        return com.auth0.jwt.JWT.create()
                .withClaim("userIdentity", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * (life == null ? duration : life)))
                .sign(Algorithm.HMAC256(private_key));
    }


    /**
     * @param token JWT token
     * @return 如果存在对应的用户，返回用户实体，否则返回null
     * @throws JWTAnalysisException 如果令牌与用户信息不匹配，则抛出该异常
     * @apiNote 解析JWT，判断用户信息是否匹配
     */
    public User analysisJWT(String token) throws JWTAnalysisException {
        try {
            Map<String, Object> map = com.auth0.jwt.JWT.require(Algorithm.HMAC256(private_key)).build().verify(token).getClaims().get("userIdentity").asMap();
            String userName = map.get("userName").toString();
            String password = map.get("password").toString();
            User user = userMapper.findByUserName(userName);
            if (user == null) {
                throw new JWTAnalysisException("The user does not exist");
            }
            if (!password.equals(user.getPassword())) {
                throw new JWTAnalysisException("Not the user's token");
            }
            user.setPic(absolutePath + user.getPic());
            return user;
        } catch (TokenExpiredException tokenExpiredException) {
            throw new JWTAnalysisException("Token expiration");
        } catch (SignatureVerificationException signatureVerificationException) {
            throw new JWTAnalysisException("The signature is invalid");
        } catch (JWTDecodeException jtDecodeException) {
            throw new JWTAnalysisException("The token header or payload is illegal");
        }
    }
}
