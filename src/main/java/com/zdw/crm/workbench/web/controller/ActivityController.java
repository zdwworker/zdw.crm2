package com.zdw.crm.workbench.web.controller;


import com.zdw.crm.settings.domain.Users;
import com.zdw.crm.settings.service.UsersService;
import com.zdw.crm.settings.service.impl.UsersServiceImpl;
import com.zdw.crm.utils.*;
import com.zdw.crm.vo.PaginationVo;
import com.zdw.crm.workbench.domain.Activity;
import com.zdw.crm.workbench.domain.ActivityRemark;
import com.zdw.crm.workbench.service.ActivityRemarkService;
import com.zdw.crm.workbench.service.ActivityService;

import com.zdw.crm.workbench.service.impl.ActivityRemarkServiceImpl;
import com.zdw.crm.workbench.service.impl.ActivityServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet({"/workbench/activity/getUserList.do","/workbench/activity/detail.do",
        "/workbench/activity/savaActivity.do","/workbench/activity/selectusers.do",
        "/workbench/activity/selectActivity.do","/workbench/activity/selectIdActivity.do",
        "/workbench/activity/delActivity.do","/workbench/activity/updataActivity.do",
        "/workbench/activity/delDetailActivity.do","/workbench/activity/getRemarkList.do"})
public class ActivityController extends HttpServlet {
    //private AcitivyService as= (AcitivyService) ServiceFactory.getService(new AcitivyServiceImpl());
    //这个方式不可用
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();//  /settings/user/login.do

        if("/workbench/activity/getUserList.do".equals(servletPath)){
            getUserList(request,response);
        }else if("/workbench/activity/savaActivity.do".equals(servletPath)){
            savaActivity(request,response);
        }else if("/workbench/activity/selectActivity.do".equals(servletPath)){
            selectActivity(request,response);
        }else if("/workbench/activity/delActivity.do".equals(servletPath)){
            delActivity(request,response);
        }else if("/workbench/activity/updataActivity.do".equals(servletPath)){
            updataActivity(request,response);
        }else if("/workbench/activity/selectIdActivity.do".equals(servletPath)){
            selectIdActivity(request,response);
        }else if("/workbench/activity/selectusers.do".equals(servletPath)){
            selectusers(request,response);
        }else if("/workbench/activity/detail.do".equals(servletPath)){
            getdetail(request,response);
        }else if( "/workbench/activity/delDetailActivity.do".equals(servletPath)){
            delDetailActivity(request,response);
        }else if( "/workbench/activity/getRemarkList.do".equals(servletPath)){
            getRemarkList(request,response);
        }

    }

    private void getRemarkList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("查询备注操作执行了");
        String id=request.getParameter("id");
        ActivityRemarkService ars= (ActivityRemarkService) ServiceFactory.getService(new ActivityRemarkServiceImpl());
        List<ActivityRemark> r=  ars.getdetailRemark(id);

        PrintJson.printJsonObj(response,r);
    }

    private void delDetailActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("在详细页面删除操作执行了");
        String id = request.getParameter("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean success=as.delDetailActivity(id);
        PrintJson.printJsonFlag(response,success);
    }

    private void getdetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity a=as.selectIdActivity(id);
        request.setAttribute("a",a);

        request.getRequestDispatcher("detail.jsp").forward(request,response);

        }


    private void selectusers(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("在活动模板获得users执行了");
      UsersService us= (UsersService) ServiceFactory.getService(new UsersServiceImpl());

      List<Users> usersList=us.getUsersList();
        System.out.println("usersList="+usersList);
      PrintJson.printJsonObj(response,usersList);
    }

    private void selectIdActivity(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        System.out.println("使用id查询数据执行了");
           ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
           Activity activity=as.selectIdActivity(id);
        System.out.println(activity);
           PrintJson.printJsonObj(response,activity);
    }

    private void updataActivity(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        System.out.println("修改操作执行了");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String description = request.getParameter("description");
        String cost = request.getParameter("cost");
        String id = request.getParameter("id");
        String editTime = DateTimeUtil.getSysTime();
        //获得修改时间和修改人名字  即登录人的名字
        Users users = (Users) request.getSession().getAttribute("users");
        String editBy=users.getName();

        Map<String,String> map=new HashMap<String, String>();
        map.put("owner",owner);
        map.put("name",name);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("description",description);
        map.put("cost",cost);
        map.put("id",id);
        map.put("editTime",editTime);
        map.put("editBy",editBy);
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean success=as.updataActivity(map);
       PrintJson.printJsonFlag(response,success);
    }

    private void delActivity(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        System.out.println("删除操作执行了！");
        String ids[] = request.getParameterValues("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean success=as.delActivity(ids);
        PrintJson.printJsonFlag(response,success);

    }

    private void selectActivity(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        String pageno = request.getParameter("pageno");
        String pagesize = request.getParameter("pagesize");
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startTime");
        String endDate = request.getParameter("endTime");
        int pageNo=Integer.valueOf(pageno);
        int pageSize=Integer.valueOf(pagesize);

        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map=new HashMap<String,Object>();

        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        System.out.println(map);
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        PaginationVo<Activity> pv=as.selectActivity(map);

        PrintJson.printJsonObj(response,pv);
    }

    private void savaActivity(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((Users)request.getSession().getAttribute("users")).getName();
        Activity a=new Activity();
        a.setCost(cost);
        a.setId(id);
        a.setStartDate(startDate);
        a.setCreateBy(createBy);
        a.setDescription(description);
        a.setCreateTime(createTime);
        a.setName(name);
        a.setOwner(owner);
        a.setEndDate(endDate);


        Boolean success= as.savaAcitivity(a);
       PrintJson.printJsonFlag(response,success);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UsersService us = (UsersService) ServiceFactory.getService(new UsersServiceImpl());
        List<Users> uList =us.getUsersList();
        PrintJson.printJsonObj(response,uList);
    }
}
