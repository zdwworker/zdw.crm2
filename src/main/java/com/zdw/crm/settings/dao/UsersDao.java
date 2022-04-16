package com.zdw.crm.settings.dao;

import com.zdw.crm.settings.domain.Users;

import java.util.List;
import java.util.Map;

public interface UsersDao {
    Users logins(Map<String, String> map) ;

    List<Users> getUsersList();
}
