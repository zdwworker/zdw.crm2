package com.zdw.crm.settings.service.impl;

import com.zdw.crm.exception.LoginException;
import com.zdw.crm.settings.dao.UsersDao;
import com.zdw.crm.settings.domain.Users;
import com.zdw.crm.settings.service.UsersService;
import com.zdw.crm.utils.DateTimeUtil;
import com.zdw.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersServiceImpl implements UsersService {
    private UsersDao usersDao= SqlSessionUtil.getSqlSession().getMapper(UsersDao.class);

    @Override
    public Users login(String account, String passwordmd5, String ip)
            throws LoginException {
        Map<String,String> map =new HashMap<String,String>();
        map.put("account",account);

        map.put("passwordmd5",passwordmd5);
        Users users= usersDao.logins(map);
        if(users==null){
            //表示账号或密码不正确
            throw new LoginException("账号或密码不正确！");
        }

           //表示账号密码正确  继续验证账号是否失效
        String exprieTime=users.getExpireTime();
        String sysTime = DateTimeUtil.getSysTime();
        if(exprieTime.compareTo(sysTime)<0){
            //说明账号已失效
            throw new LoginException("账号已失效！");
        }
        //验证IP地址是否有效

        String allowIps = users.getAllowIps();
        if(allowIps!=null && allowIps!= ""){
            if(!allowIps.contains(ip)){
                throw new LoginException("IP地址无效");
            }
        }

        //验证账号是否锁定
        String lockState = users.getLockState();
        if("0".equals(lockState)){
            throw new LoginException("账户已锁定，请联系管理员！");
        }

        return users;
    }

    @Override
    public List<Users> getUsersList() {
       /* UsersDao usersDao = SqlSessionUtil.getSqlSession().getMapper(UsersDao.class);*/
        List<Users> uList=usersDao.getUsersList();
        return uList;
    }
}
