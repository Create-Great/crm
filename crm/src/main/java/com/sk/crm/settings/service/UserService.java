package com.sk.crm.settings.service;

import com.sk.crm.settings.domain.User;

import javax.security.auth.login.LoginException;
import java.util.List;

public interface UserService {

    User login(String loginAct, String pwd, String ip) throws LoginException;

    List<User> getUserList();
}
