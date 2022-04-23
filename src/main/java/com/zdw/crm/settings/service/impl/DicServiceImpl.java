package com.zdw.crm.settings.service.impl;

import com.zdw.crm.settings.dao.DicTypeDao;
import com.zdw.crm.settings.dao.DicValueDao;
import com.zdw.crm.settings.dao.UsersDao;
import com.zdw.crm.settings.domain.DicType;
import com.zdw.crm.settings.domain.DicValue;
import com.zdw.crm.settings.service.DicService;
import com.zdw.crm.utils.ServiceFactory;
import com.zdw.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {
    private DicValueDao dicValueDao= SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);
    private DicTypeDao dicTypeDao= SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    @Override
    public Map<String, List<DicValue>> getAll() {
        Map<String, List<DicValue>> map=new HashMap<String, List<DicValue>>();
        List<DicType> listType= dicTypeDao.getType();
        for(DicType dicType:listType){
            String typeCode=dicType.getCode();
            List<DicValue> listD=dicValueDao.getAll(typeCode);
            map.put(typeCode,listD);
        }

       return map;
    }
}
