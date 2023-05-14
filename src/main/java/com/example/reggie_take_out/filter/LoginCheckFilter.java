package com.example.reggie_take_out.filter;

import com.alibaba.fastjson.JSON;

import com.example.reggie_take_out.common.BaseContext;
import com.example.reggie_take_out.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

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
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

//        获取本次请求的URI
        String requestURI = request.getRequestURI();
        log.info("拦截到请求：{}", requestURI);
        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login",
                "/"

        };

        //2、判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

        //3、如果不需要处理，则直接放行
        if (check) {
            log.info("本次请求{}不需要处理", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

            //  4-1 后台管理端，如果是其他请求，判断是否登录
            if (request.getSession().getAttribute("loginEmployeeId") != null) {
                log.info("用户已登录，用户id为：{}", request.getSession().getAttribute("loginEmployeeId"));

                Long empId = (Long) request.getSession().getAttribute("loginEmployeeId");
                BaseContext.setCurrentId(empId);

                filterChain.doFilter(request, response);
                return;
            }

            //4-2 移动端判断登陆状态，若已经登陆直接放行(移动端)
            if (request.getSession().getAttribute("userid") != null) {
                log.info("用户已登录，id为：{}", request.getSession().getAttribute("userid"));
//            此为使用BaseContext的保存登录用户id的方法
                Long userId = (Long) request.getSession().getAttribute("userid");
                BaseContext.setCurrentId(userId);
                long id = Thread.currentThread().getId();
                log.info("线程ID为：{}", id);
                filterChain.doFilter(request, response);
                return;
            }

//        如果未登录，通过response返回未登录的信息
        log.info("用户未登录");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     *
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }


}
