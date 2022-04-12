package com.zdw.crm.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter("*.do")
public class EncodingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        System.out.println("进入到过滤字符编码的过滤器");

        //过滤post请求中文参数乱码
        servletRequest.setCharacterEncoding("UTF-8");
        //过滤响应流响应中文乱码
        servletResponse.setContentType("text/html;charset=utf-8");

        //将请求放行
        filterChain.doFilter(servletRequest,servletResponse);

    }
}
