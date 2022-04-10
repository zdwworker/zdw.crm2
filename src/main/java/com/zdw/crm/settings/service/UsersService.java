package com.zdw.crm.settings.service;

import com.zdw.crm.exception.LoginException;
import com.zdw.crm.settings.domain.Users;

public interface UsersService {

    Users login(String account, String passwordmd5, String ip) throws LoginException;


}
