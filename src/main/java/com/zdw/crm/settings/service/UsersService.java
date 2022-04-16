package com.zdw.crm.settings.service;

import com.zdw.crm.exception.LoginException;
import com.zdw.crm.settings.domain.Users;

import java.util.List;

public interface UsersService {

    Users login(String account, String passwordmd5, String ip) throws LoginException;


    List<Users> getUsersList();
}
