package com.zdw.crm.workbench.service;

import com.zdw.crm.workbench.domain.Clue;
import com.zdw.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface ClueService {
    List<Clue> getClue();

    Boolean savaClue(Clue clue);

    Clue getDetail(String id);


    Boolean convert(String createBy, Tran tran, String clueId);
}
