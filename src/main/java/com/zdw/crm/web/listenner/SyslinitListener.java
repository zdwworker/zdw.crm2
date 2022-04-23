package com.zdw.crm.web.listenner;


import com.zdw.crm.settings.dao.DicValueDao;
import com.zdw.crm.settings.domain.DicValue;
import com.zdw.crm.settings.service.DicService;
import com.zdw.crm.settings.service.impl.DicServiceImpl;
import com.zdw.crm.utils.ServiceFactory;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@WebListener
public class SyslinitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("监听器创建成功了！");
        ServletContext application = event.getServletContext();
        DicService ds= (DicService) ServiceFactory.getService(new DicServiceImpl());

         Map<String, List<DicValue>> map= ds.getAll();

         Set<String> set=map.keySet();

         for(String key:set){
             application.setAttribute(key,map.get(key));
         }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("监听器销毁了！！！");
    }
}
