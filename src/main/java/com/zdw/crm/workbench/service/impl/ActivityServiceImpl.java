package com.zdw.crm.workbench.service.impl;
import com.zdw.crm.utils.SqlSessionUtil;
import com.zdw.crm.vo.PaginationVo;
import com.zdw.crm.workbench.dao.ActivityDao;
import com.zdw.crm.workbench.dao.ActivityRemarkDao;
import com.zdw.crm.workbench.domain.Activity;
import com.zdw.crm.workbench.domain.ActivityRemark;
import com.zdw.crm.workbench.service.ActivityService;

import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
     private ActivityDao activityDao= SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);


    @Override
    public Boolean savaAcitivity(Activity a) {
        int count=activityDao.savaActivity(a);
        boolean flag=false;
        if(count==1){
            flag=true;
        }
        return flag;

    }

    @Override
    public PaginationVo<Activity> selectActivity(Map<String, Object> map) {
        List<Activity> activityList=activityDao.selectActivity(map);
        int count = activityDao.selectCount(map);
        PaginationVo<Activity> paginationVo=new PaginationVo<Activity>();
        paginationVo.setDataList(activityList);
        paginationVo.setTotal(count);
        return paginationVo;
    }

    @Override
    public Boolean delActivity(String[] ids) {
        ActivityRemarkDao activityRemarkDao= SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
        boolean success=false;
        //查询影响备份数据库的条数
        int cont=activityRemarkDao.getContRemark(ids);
        //放回删除备份数据库的条数
        int cont2=activityRemarkDao.delactivityRemark(ids);
        if(cont==cont2){
            success=true;
        }
        int flag=activityDao.delActivity(ids);
        if(flag==ids.length){
            success=true;
        }
        return success;
    }

    @Override
    public Activity selectIdActivity(String id) {
        Activity activity=activityDao.selectIdActivity(id);

        return activity;
    }

    @Override
    public boolean updataActivity(Map<String, String> map) {
        boolean success=false;
        int acont=activityDao.updataActivity(map);
        if(acont==1){
            success=true;
        }
        return success;
    }

    @Override
    public Boolean delDetailActivity(String id) {
        ActivityRemarkDao activityRemarkDao= SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
        boolean success=false;
        //查询影响备份数据库的条数
        int cont=activityRemarkDao.getDatailContRemark(id);
        //返回删除备份数据库的条数
        int cont2=activityRemarkDao.delDatailactivityRemark(id);
        if(cont==cont2){
            success=true;
        }
        int flag=activityDao.delDetailActivity(id);
       if(flag!=1){
           success=false;
       }
        return success;

    }


}
