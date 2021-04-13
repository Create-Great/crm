package com.sk.crm.settings.service;

import com.sk.crm.settings.domain.Activity;
import com.sk.crm.settings.domain.ActivityRemark;
import com.sk.crm.vo.PaginationVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ActivityService {

    boolean save(Activity a);

    PaginationVO<Activity> pageList(HashMap<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    boolean update(Activity a);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    boolean deleteRemark(String id);

    boolean saveRemark(ActivityRemark ar);

    boolean updateRemark(ActivityRemark ar);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map);

    List<Activity> getActivityByName(String aName);
}
