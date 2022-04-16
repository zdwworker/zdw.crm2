package com.zdw.crm.workbench.service;

import com.zdw.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityRemarkService {

    List<ActivityRemark> getdetailRemark(String id);

    boolean delRemark(String id);

    boolean updataRemark(Map<String, String> map);

    Boolean savaRemark(Map<String, String> map);
}
