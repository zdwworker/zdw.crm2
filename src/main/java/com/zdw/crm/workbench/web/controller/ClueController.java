package com.zdw.crm.workbench.web.controller;

import com.zdw.crm.settings.domain.Users;
import com.zdw.crm.settings.service.UsersService;
import com.zdw.crm.settings.service.impl.UsersServiceImpl;
import com.zdw.crm.utils.DateTimeUtil;
import com.zdw.crm.utils.PrintJson;
import com.zdw.crm.utils.ServiceFactory;
import com.zdw.crm.utils.UUIDUtil;
import com.zdw.crm.workbench.domain.Activity;
import com.zdw.crm.workbench.domain.Clue;
import com.zdw.crm.workbench.domain.Tran;
import com.zdw.crm.workbench.service.ActivityService;
import com.zdw.crm.workbench.service.ClueActivityRelationService;
import com.zdw.crm.workbench.service.ClueService;
import com.zdw.crm.workbench.service.impl.ActivityServiceImpl;
import com.zdw.crm.workbench.service.impl.ClueActivityRelationServiceImpl;
import com.zdw.crm.workbench.service.impl.ClueServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet({"/workbench/clue/getuser.do","/workbench/clue/getClue.do",
            "/workbench/clue/savaClue.do","/workbench/clue/getdetail.do",
            "/workbench/clue/getActivity.do","/workbench/clue/delRelation.do",
            "/workbench/clue/seachactivity.do","/workbench/clue/relevance.do",
            "/workbench/clue/getActivityListByName.do","/workbench/clue/convert.do"})
public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if("/workbench/clue/getuser.do".equals(servletPath)){
            getusers(request,response);
        }else if("/workbench/clue/getClue.do".equals(servletPath)){
            getClue(request,response);
        }else if("/workbench/clue/savaClue.do".equals(servletPath)){
            savaClue(request,response);
        }else if("/workbench/clue/getdetail.do".equals(servletPath)){
            getDetail(request,response);
        }else if("/workbench/clue/getActivity.do".equals(servletPath)){
            getActivity(request,response);
        }else if("/workbench/clue/delRelation.do".equals(servletPath)){
            delRelation(request,response);
        }else if("/workbench/clue/seachactivity.do".equals(servletPath)){
            seachactivity(request,response);
        }else if("/workbench/clue/relevance.do".equals(servletPath)){
            relevance(request,response);
        }else if("/workbench/clue/getActivityListByName.do".equals(servletPath)){
            getActivityListByName(request,response);
        }else if("/workbench/clue/convert.do".equals(servletPath)){
            convert(request,response);
        }
    }

    private void convert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String clueId = request.getParameter("clueId");
        String flag = request.getParameter("flag");
        Users users = (Users) request.getSession().getAttribute("users");
        String createBy =users.getName();
        String createTime = DateTimeUtil.getSysTime();

        Tran tran=null;
        if("a".equals(flag)){
            //表示要创建交易
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            tran=new Tran();
            tran.setStage(stage);
            tran.setName(name);
            tran.setMoney(money);
            tran.setActivityId(activityId);
            tran.setExpectedDate(expectedDate);
            tran.setCreateBy(createBy);
            tran.setCreateTime(createTime);
            tran.setId(UUIDUtil.getUUID());
        }

        ClueService clueService= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Boolean flag1=clueService.convert(createBy,tran,clueId);
        if(flag1){
            //转化成功
            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");
        }else {

        }
    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("模糊查询市场活动列表执行了");
        String name = request.getParameter("aname");
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> activityList=as.getActivityListByName(name);
        PrintJson.printJsonObj(response,activityList);
    }

    private void relevance(HttpServletRequest request, HttpServletResponse response) {
        String clueId = request.getParameter("clueId");
        String[] activityIds = request.getParameterValues("activityId");
        ClueActivityRelationService crs= (ClueActivityRelationService) ServiceFactory.getService(new ClueActivityRelationServiceImpl());
        Boolean success=false;
        for (int i=0;i<activityIds.length;i++){
            Map<String,String> map=new HashMap<String, String>();
            String id = UUIDUtil.getUUID();
            map.put("id",id);
            map.put("clueId",clueId);
            map.put("activityId",activityIds[i]);
            success=crs.relevance(map);
        }
        PrintJson.printJsonFlag(response,success);
    }

    private void seachactivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("模糊查询市场活动列表执行了");
        String name = request.getParameter("name");
        String clueId = request.getParameter("clueId");
        Map<String,String> map=new HashMap<String, String>();
        map.put("name",name);
        map.put("clueId",clueId);
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> activityList=as.seachactivity(map);
        PrintJson.printJsonObj(response,activityList);
    }

    private void delRelation(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("解除关联执行了");
        String clueId = request.getParameter("clueId");
        String activityId=request.getParameter("activityId");
        ClueActivityRelationService cs= (ClueActivityRelationService) ServiceFactory.getService(new ClueActivityRelationServiceImpl());

        Map<String,Object> map=new HashMap<String,Object>();
        map.put("clueId",clueId);
        map.put("activityId",activityId);

        boolean success= cs.delRelation(map);
        PrintJson.printJsonFlag(response,success);

    }

    private void getActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("在线索详细页面刷新关联市场活动执行了！");
        String clueId=request.getParameter("clueId");
        ClueActivityRelationService csr= (ClueActivityRelationService) ServiceFactory.getService(new ClueActivityRelationServiceImpl());
        List<String> lists=csr.getActivityId(clueId);
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> activityList=as.getActivity(lists);
        PrintJson.printJsonObj(response,activityList);

    }

    private void getDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("打开详细页面操作执行了");
       String id= request.getParameter("id");
       ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
       Clue clue= cs.getDetail(id);
       request.setAttribute("c",clue);

       request.getRequestDispatcher("detail.jsp").forward(request,response);
    }

    private void savaClue(HttpServletRequest request, HttpServletResponse response) {
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String appellation=request.getParameter("appellation");
        String fullname = request.getParameter("fullname");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");
        String id = UUIDUtil.getUUID();
        Users users=(Users)request.getSession().getAttribute("users");
        String createBy=users.getName();
        String createTime = DateTimeUtil.getSysTime();

        Clue clue = new Clue();

        clue.setAddress(address);
        clue.setAppellation(appellation);
        clue.setCompany(company);
        clue.setContactSummary(contactSummary);
        clue.setCreateBy(createBy);
        clue.setDescription(description);
        clue.setCreateTime(createTime);
        clue.setEmail(email);
        clue.setFullname(fullname);
        clue.setId(id);
        clue.setJob(job);
        clue.setMphone(mphone);
        clue.setOwner(owner);
        clue.setPhone(phone);
        clue.setState(state);
        clue.setWebsite(website);
        clue.setSource(source);
        clue.setNextContactTime(nextContactTime);
        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Boolean success=cs.savaClue(clue);
        PrintJson.printJsonFlag(response,success);

    }

    private void getClue(HttpServletRequest request, HttpServletResponse response) {
        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
         List<Clue> clueList= cs.getClue();
         PrintJson.printJsonObj(response,clueList);
    }

    private void getusers(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("线索模块获得使用者执行了");
        UsersService us= (UsersService) ServiceFactory.getService(new UsersServiceImpl());
        List<Users> usersList = us.getUsersList();
        PrintJson.printJsonObj(response,usersList);
    }
}
