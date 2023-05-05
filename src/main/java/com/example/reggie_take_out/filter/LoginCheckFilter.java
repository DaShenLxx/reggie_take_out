package com.example.reggie_take_out.filter;

import com.alibaba.fastjson.JSON;

import com.example.reggie_take_out.common.R;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录检查过滤器
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse  response = (HttpServletResponse) servletResponse;

//        获取本次请求的URI
        String requestURI = request.getRequestURI();
//        如果是登录请求，直接放行
        if (requestURI.contains("/login") || requestURI.contains("/logout") || requestURI.contains("/front") || requestURI.contains("/backend") || requestURI.contains("/images")){
            filterChain.doFilter(request,response);
            return;
        }
//        如果是其他请求，判断是否登录
        Object employee = request.getSession().getAttribute("loginEmployee");
        if (employee != null) {
            filterChain.doFilter(request,response);
            return;
        }
//        如果未登录，通过response返回未登录的信息
        log.info("拦截到请求:{}",request.getRequestURI());
        System.out.println("未登录");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

    }
}
