package com.hzx.filter;

import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * OncePerRequestFilter是在一次外部请求中只过滤一次。
 * 对于服务器内部之间的forward等请求，不会再次执行过滤方法。
 */
@Configuration
@WebFilter(urlPatterns = "/*")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LogFilter extends OncePerRequestFilter {

    private final static String MDC_TRACE_ID = "traceId";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String traceIdStr = getMacTraceId();
        MDC.put(MDC_TRACE_ID, traceIdStr);
        filterChain.doFilter(request, response);
        MDC.clear();
    }


    /**
     * 生产traceId
     * @return
     */
    private String getMacTraceId() {
        long currentTime = System.nanoTime();
        return String.join("_", MDC_TRACE_ID, String.valueOf(currentTime));
    }

}






