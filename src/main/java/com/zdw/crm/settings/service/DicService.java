package com.zdw.crm.settings.service;

import com.zdw.crm.settings.domain.DicType;
import com.zdw.crm.settings.domain.DicValue;
import jakarta.servlet.http.HttpServlet;

import java.util.List;
import java.util.Map;

public interface DicService  {

    Map<String, List<DicValue>> getAll();
}
