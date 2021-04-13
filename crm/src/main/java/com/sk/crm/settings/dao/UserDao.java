package com.sk.crm.settings.dao;

import com.sk.crm.settings.domain.User;

import java.util.HashMap;
import java.util.List;

public interface UserDao {

    User login(HashMap<String, String> map);

    List<User> getUserList();
}
