package com.zdw.crm.workbench.dao;

import com.zdw.crm.workbench.domain.ClueActivityRelation;

import java.util.List;
import java.util.Map;

public interface ClueActivityRelationDao {


    List<String> getActivityId(String clueId);

    int delRelation(Map<String,Object> map);

    int relevance(Map<String, String> map);

    int delbyClueId(String clueId);
}
