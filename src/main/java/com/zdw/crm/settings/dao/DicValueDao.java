package com.zdw.crm.settings.dao;

import com.zdw.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {



    List<DicValue> getAll(String typeCode);
}
