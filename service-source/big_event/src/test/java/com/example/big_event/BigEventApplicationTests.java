package com.example.big_event;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@SpringBootTest
class BigEventApplicationTests {


    @Test
    void createJWT() {
        Map<String,Object> event = new HashMap<>();
        event.put("name","1");
        event.put("age",20);

        String token = JWT.create()
                .withClaim("user", event) //添加载荷
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*10)) //当前时间+10minutes为令牌的失效时间
                .sign(Algorithm.HMAC256("private key"));    //加密方式以及密钥

        System.out.println(token);
    }

    @Test
    void analysisJWT() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7Im5hbWUiOiIxIiwiYWdlIjoyMH0sImV4cCI6MTcxNDE0NTU5NH0.ERwyJqy1XrP0srDy6SX-9Lq3sHA0sb0QJ4j7aD9dtwc";

        try{
            Verification verification = JWT.require(Algorithm.HMAC256("private key"));
            JWTVerifier build = verification.build();
            DecodedJWT verify = build.verify(token);
            Map<String, Claim> claims = verify.getClaims();
            Claim user = claims.get("user");    //获取载荷
            System.out.println(user);
        }catch (TokenExpiredException tokenExpiredException){
            System.out.println("令牌过期");
        }catch (SignatureVerificationException signatureVerificationException){
            System.out.println("签名非法");
        }catch (JWTDecodeException jtDecodeException){
            System.out.println("令牌头部或载荷非法");
        }
        //tip：密钥错误时可以解密，但是解密内容一定不匹配

    }

    @Test
    void test1(){
        Pattern r = Pattern.compile(".png$");
        Matcher m = r.matcher("123.png");
        System.out.println(m.find());
    }

}
