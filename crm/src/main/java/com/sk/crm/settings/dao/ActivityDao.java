package com.sk.crm.settings.dao;

import com.sk.crm.settings.domain.Activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int save(Activity a);

    List<Activity> getActivityListByCondition(HashMap<String, Object> map);

    int getTotalByCondition(HashMap<String, Object> map);

    int delete(String[] ids);

    Activity getById(String id);

    int update(Activity a);

    Activity detail(String id);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map);

    List<Activity> getActivityByName(String aName);
}
