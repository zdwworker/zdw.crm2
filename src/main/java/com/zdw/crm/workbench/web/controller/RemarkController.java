package com.zdw.crm.workbench.web.controller;

import com.zdw.crm.settings.domain.Users;
import com.zdw.crm.utils.*;
import com.zdw.crm.workbench.service.ActivityRemarkService;
import com.zdw.crm.workbench.service.impl.ActivityRemarkServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet({"/workbench/activity/delRemark.do","/workbench/activity/updataRemark.do",
            "/workbench/activity/savaRemark.do"})
public class RemarkController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();//  /settings/user/login.do

        if("/workbench/activity/delRemark.do".equals(servletPath)){
            delRemark(request,response);
        }else if("/workbench/activity/updataRemark.do".equals(servletPath)){
            updataRemark(request,response);
        }else if("/workbench/activity/savaRemark.do".equals(servletPath)){
            savaRemark(request,response);
        }
    }

    private void savaRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("增加备注操作执行了");
        String activityId = request.getParameter("activityId");
        String noteContent = request.getParameter("savanoteContent");
        String id = UUIDUtil.getUUID();
        String createTime=DateTimeUtil.getSysTime();
        String editFlag="0";
        Users users= (Users) request.getSession().getAttribute("users");
        String createBy=users.getName();
        Map<String,String> map=new HashMap<String, String>();
        map.put("createBy",createBy);
        map.put("activityId",activityId);
        map.put("noteContent",noteContent);
        map.put("id",id);
        map.put("createTime",createTime);
        map.put("editFlag",editFlag);

        ActivityRemarkService activityRemarkService= (ActivityRemarkService) ServiceFactory.getService(new ActivityRemarkServiceImpl());
        Boolean success=activityRemarkService.savaRemark(map);
        PrintJson.printJsonFlag(response,success);
    }

    private void updataRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("修改备注操作执行了");
        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editFlag="1";
        Users users = (Users) request.getSession().getAttribute("users");
        String editBy=users.getName();
        Map<String,String> map=new HashMap<String, String>();
        map.put("id",id);
        map.put("noteContent",noteContent);
        map.put("editTime",editTime);
        map.put("editFlag",editFlag);
        map.put("editBy",editBy);
        System.out.println(map);
        ActivityRemarkService ars = (ActivityRemarkService) ServiceFactory.getService(new ActivityRemarkServiceImpl());
        boolean success=ars.updataRemark(map);

        PrintJson.printJsonFlag(response,success);
    }

    private void delRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("删除备注执行了");
        String id = request.getParameter("id");
        ActivityRemarkService ars = (ActivityRemarkService) ServiceFactory.getService(new ActivityRemarkServiceImpl());
        boolean success=ars.delRemark(id);
        PrintJson.printJsonFlag(response,success);
    }
}
