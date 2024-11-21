package com.hzx.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoggerInterceptor implements HandlerInterceptor {

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        log.info("++++++++++++++++++++++++++++++++++++++++++++ 日志拦截器：LoggerInterceptor begin ++++++++++++++++++++++++++++++++++++++++++++");

        // 记录请求信息
        log.info("请求地址: {}, 方法类型: {}", request.getRequestURL(), request.getMethod());
        return true; // 返回true表示继续执行后续的Interceptor和Controller，false则中断
    }

    /**
     * 在Controller方法调用之后，且解析视图之前进行调用
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if (modelAndView != null) {
            // 这里可以对ModelAndView进行操作
            log.debug("视图名称: {}", modelAndView.getViewName());
        }
    }

    /**
     * 在整个请求结束之后（DispatcherServlet渲染了对应的视图）执行
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        // 执行完毕后记录日志
        log.info("请求结束. 状态码: {}", response.getStatus());
        if (ex != null) {
            // 记录异常信息
            log.error("运行中发生异常: ", ex);
        }

        log.info("++++++++++++++++++++++++++++++++++++++++++++ 日志拦截器：LoggerInterceptor end ++++++++++++++++++++++++++++++++++++++++++++");
    }
}