package com.zdw.crm.workbench.dao;


import com.zdw.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;


public interface ActivityDao {
    int savaActivity(Activity activity);


    List<Activity> selectActivity(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    int delActivity(String[] ids);

    Activity selectIdActivity(String id);

    int updataActivity(Map<String, String> map);

    int delDetailActivity(String id);
}
