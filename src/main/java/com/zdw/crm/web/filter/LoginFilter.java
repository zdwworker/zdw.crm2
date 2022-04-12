package com.zdw.crm.web.filter;
import com.zdw.crm.settings.domain.Users;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Author 北京动力节点
 */
@WebFilter({"*.do","*.jsp"})
public class LoginFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request= (HttpServletRequest) req;
        HttpServletResponse response= (HttpServletResponse) resp;
        String path = request.getServletPath();

        if("/settings/user/login.do".equals(path) || "/login.jsp".equals(path)){
            chain.doFilter(req,resp);
        }else{
            Users users = (Users) request.getSession().getAttribute("users");

            if(users!=null ){
                //表示登录成功  放行
                chain.doFilter(req,resp);
            }else{
                //表示登录失败  重定向到登录页面
                response.sendRedirect(request.getContextPath()+"/login.jsp");
            }
        }

    }


}
