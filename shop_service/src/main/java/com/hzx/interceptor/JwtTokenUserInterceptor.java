package com.hzx.interceptor;

import com.hzx.common.constant.JwtClaimsConstant;
import com.hzx.common.context.BaseContext;
import com.hzx.common.properties.JwtProperties;
import com.hzx.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 校验jwt
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getUserTokenName());
        log.info("当前线程的id：{}，jwt校验:{}", Thread.currentThread().getId(), token);


        //2、校验令牌
        if (token != null) {
            try {
                //解密jwt令牌，获得claims里的数据
                Claims claims = jwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
                Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
                //将操作当前线程的用户id存入到请求域中
                BaseContext.setCurrentId(userId);
                log.info("当前用户id：{}，jwt校验:{}", userId, token);
                //3、通过，放行
                return true;
            } catch (Exception ex) {
                //4、不通过，响应401状态码
                response.setStatus(401);
                return false;
            }
        }else{
            //5、令牌为空，响应401状态码
            response.setStatus(401);
            return false;
        }
    }
}
