package com.sk.crm.settings.service.Impl;


import com.sk.crm.settings.dao.UserDao;
import com.sk.crm.settings.domain.User;
import com.sk.crm.settings.service.UserService;
import com.sk.crm.utils.DateTimeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    //自动注入
    @Resource
    private UserDao userDao;

    @Override
    public User login(String loginAct, String pwd, String ip) throws LoginException {

        HashMap<String, String> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",pwd);

        User user = userDao.login(map);

        if(user == null){
            throw new LoginException("帐号或密码错误!");
        }

        //如果程序能够执行到这里,说明帐号密码正确
        //继续向下验证其他3项信息

        //判断帐号是否失效
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if(expireTime.compareTo(currentTime) < 0){
            throw new LoginException("帐号已失效!");
        }
        
        //判断锁定状态
        String lockState = user.getLockState();
        if("0".equals(lockState)){
            throw new LoginException("帐号已锁定!");
        }

        //判断ip地址
        String allowIps = user.getAllowIps();
        if(allowIps.contains(ip)){
            throw new LoginException("ip地址受限!");
        }

        return user;
    }

    @Override
    public List<User> getUserList() {

        List<User> uList = userDao.getUserList();
        return  uList;
    }
}
