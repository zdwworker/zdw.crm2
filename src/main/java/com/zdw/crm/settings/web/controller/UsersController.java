package com.zdw.crm.settings.web.controller;

import com.zdw.crm.settings.domain.Users;
import com.zdw.crm.settings.service.UsersService;
import com.zdw.crm.settings.service.impl.UsersServiceImpl;
import com.zdw.crm.utils.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/settings/user/login.do")
public class UsersController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();//  /settings/user/login.do

        if("/settings/user/login.do".equals(servletPath)){
            logins(request,response);
        }

    }


    private void logins(HttpServletRequest request, HttpServletResponse response)
           {
               //过滤响应流响应中文乱码
               //response.setContentType("text/html;charset=utf-8");

        String account = request.getParameter("account");
        String password = request.getParameter("password");
        String passwordmd5 = MD5Util.getMD5(password);
        String ip = request.getRemoteAddr();//获取ip地址

        UsersService us= (UsersService) ServiceFactory.getService(new UsersServiceImpl());

        try {

            Users users=us.login(account,passwordmd5,ip);

            request.getSession().setAttribute("users",users);
            //如果程序执行到了此处  表示登录成功
            PrintJson.printJsonFlag(response,true);
        }catch (Exception e){
            e.printStackTrace();

            String arg=e.getMessage();
            Map<String,Object> map=new HashMap<String,Object>();

            map.put("success",false);
            map.put("arg",arg);

            PrintJson.printJsonObj(response,map);
        }

    }


}

