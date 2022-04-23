package com.zdw.crm.workbench.dao;

import com.zdw.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getRemarkByclueId(String clueId);

    int delRemarkbyClueId(String clueId);
}
