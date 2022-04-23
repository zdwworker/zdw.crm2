package com.zdw.crm.workbench.service;

import com.zdw.crm.vo.PaginationVo;
import com.zdw.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    Boolean savaAcitivity(Activity a);

    PaginationVo<Activity> selectActivity(Map<String, Object> map);


    Boolean delActivity(String[] ids);

    Activity selectIdActivity(String id);


    boolean updataActivity(Map<String, String> map);

    Boolean delDetailActivity(String id);

    List<Activity> getActivity(List<String> lists);

    List<Activity> seachactivity(Map<String, String> map);

    List<Activity> getActivityListByName(String name);
}
