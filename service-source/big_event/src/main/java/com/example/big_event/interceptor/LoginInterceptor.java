package com.example.big_event.interceptor;

import com.example.big_event.exception.JWTAnalysisException;
import com.example.big_event.message.Result;
import com.example.big_event.util.JWT;
import com.example.big_event.util.ThreadLocal;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Resource
    JWT jwt;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            //解析存储于POST请求头部authorization字段中的JWT token。若能成功解析，则将解析结果存放于容器中。
            ThreadLocal.setObject(new Result(true, jwt.analysisJWT(request.getHeader("authorization"))));
            return true;
        } catch (JWTAnalysisException jwtAnalysisException) {
            System.out.println(jwtAnalysisException.getMessage());
            response.setHeader("msg", jwtAnalysisException.getMessage());
            ThreadLocal.setObject(new Result(false, jwtAnalysisException.getMessage()));
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //在完成操作后，清空容器，释放空间
        ThreadLocal.clear();
    }
}
