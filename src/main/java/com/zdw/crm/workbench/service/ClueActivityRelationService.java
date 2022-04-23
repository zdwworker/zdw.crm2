package com.zdw.crm.workbench.service;

import java.util.List;
import java.util.Map;

public interface ClueActivityRelationService {
    List<String> getActivityId(String clueId);

    boolean delRelation(Map<String,Object> map);

    Boolean relevance(Map<String, String> map);
}
