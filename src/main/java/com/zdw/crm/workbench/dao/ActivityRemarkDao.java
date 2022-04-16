package com.zdw.crm.workbench.dao;

import com.zdw.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityRemarkDao {
    int getContRemark(String[] ids);

    int delactivityRemark(String[] ids);

    int getDatailContRemark(String id);

    int delDatailactivityRemark(String id);

    List<ActivityRemark> getdetailRemark(String id);

    int delRemark(String id);

    int updataRemark(Map<String, String> map);

    int savaRemark(Map<String, String> map);
}
