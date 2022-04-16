package com.zdw.crm.workbench.service.impl;


import com.zdw.crm.utils.SqlSessionUtil;
import com.zdw.crm.workbench.dao.ActivityRemarkDao;
import com.zdw.crm.workbench.domain.ActivityRemark;
import com.zdw.crm.workbench.service.ActivityRemarkService;

import java.util.List;
import java.util.Map;

public class ActivityRemarkServiceImpl implements ActivityRemarkService {
    private ActivityRemarkDao activityRemarkDao= SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);


    @Override
    public List<ActivityRemark> getdetailRemark(String id) {

        List<ActivityRemark> activityRemark=activityRemarkDao.getdetailRemark(id);

        return activityRemark;
    }

    @Override
    public boolean delRemark(String id) {
        int cont= activityRemarkDao.delRemark(id);
        boolean success=false;
        if(cont==1){
            success=true;
        }
        return success;
    }

    @Override
    public boolean updataRemark(Map<String, String> map) {
       int cont= activityRemarkDao.updataRemark(map);
       boolean success=false;
       if(cont==1){
           success=true;
       }
        return success;
    }

    @Override
    public Boolean savaRemark(Map<String, String> map) {
        int acont=activityRemarkDao.savaRemark(map);
        Boolean success=false;
        if(acont==1){
            success=true;
        }
        return success;
    }
}
