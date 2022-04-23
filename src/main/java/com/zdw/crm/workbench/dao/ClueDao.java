package com.zdw.crm.workbench.dao;

import com.zdw.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    List<Clue> getClue();

    int savaClue(Clue clue);

    Clue getDetail(String id);

    int delRelation(Map<String, String> map);

    Clue getCluebyClueId(String clueId);

    int delbyClueId(String clueId);
}
