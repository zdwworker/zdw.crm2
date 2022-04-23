package com.zdw.crm.workbench.service.impl;

import com.zdw.crm.utils.SqlSessionUtil;
import com.zdw.crm.workbench.dao.ClueActivityRelationDao;
import com.zdw.crm.workbench.service.ClueActivityRelationService;

import java.util.List;
import java.util.Map;

public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    @Override
    public List<String> getActivityId(String clueId) {
        List<String> list=clueActivityRelationDao.getActivityId(clueId);
        return list;
    }

    @Override
    public boolean delRelation(Map<String,Object> map) {
        int acont=clueActivityRelationDao.delRelation(map);
        Boolean success=false;
        if(acont!=0){
            success=true;
        }
        return success;
    }

    @Override
    public Boolean relevance(Map<String, String> map) {
        int cont=clueActivityRelationDao.relevance(map);
        Boolean success=false;
        if(cont==1){
            success=true;
        }
        return success;
    }
}
