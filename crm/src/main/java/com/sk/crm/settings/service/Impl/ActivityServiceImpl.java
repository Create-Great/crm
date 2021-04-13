package com.sk.crm.settings.service.Impl;

import com.sk.crm.settings.dao.ActivityDao;
import com.sk.crm.settings.dao.ActivityRemarkDao;
import com.sk.crm.settings.dao.UserDao;
import com.sk.crm.settings.domain.Activity;
import com.sk.crm.settings.domain.ActivityRemark;
import com.sk.crm.settings.domain.User;
import com.sk.crm.settings.service.ActivityService;
import com.sk.crm.vo.PaginationVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {


    @Resource
    private ActivityDao activityDao;
    //@Resource
    //private PaginationVO<Activity> vo;
    @Resource
    private ActivityRemarkDao activityRemarkDao;

    @Resource
    private UserDao userDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(Activity a) {

        boolean flag = true;
        int count = activityDao.save(a);

        if(count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public PaginationVO<Activity> pageList(HashMap<String, Object> map) {

        //取得total
        int total = activityDao.getTotalByCondition(map);

        //取得dataList
        List<Activity> dataList = activityDao.getActivityListByCondition(map);

        //将total和dataList封装到vo中
        PaginationVO<Activity> vo = new PaginationVO<>();

        vo.setTotal(total);
        vo.setDataList(dataList);

        //将vo返回
        return vo;
    }

    @Override
    public boolean delete(String[] ids) {

        boolean flag = true;

        //查询出需要删除的备注的数量
        int count1 = activityRemarkDao.getCountByAids(ids);

        //删除备注,返回受到影响的条数(实际删除的数量)
        int count2 = activityRemarkDao.deleteByAids(ids);

        if(count1!=count2){

            flag = false;

        }

        //删除市场活动
        int count3 = activityDao.delete(ids);
        if(count3 != ids.length){

            flag = false;

        }

        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {

        //取uList
        List<User> uList = userDao.getUserList();

        //取a
        Activity a = activityDao.getById(id);

        //将uList和a打包到map中
        HashMap<String, Object> map = new HashMap<>();
        map.put("uList",uList);
        map.put("a",a);

        //返回map
        return map;
    }

    @Override
    public boolean update(Activity a) {

        boolean flag = true;
        int count = activityDao.update(a);

        if(count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Activity detail(String id) {

        Activity a = activityDao.detail(id);
        return  a;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {

        List<ActivityRemark> arList = activityRemarkDao.getRemarkListByAid(activityId);

        return arList;
    }

    @Override
    public boolean deleteRemark(String id) {

        boolean flag = true;

        int count = activityRemarkDao.deleteRemarkById(id);

        if(count!=1){
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark ar) {

        boolean flag = true;

        int count = activityRemarkDao.saveRemark(ar);

        if(count!=1){
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean updateRemark(ActivityRemark ar) {

        boolean flag = true;

        int count = activityRemarkDao.updateRemark(ar);

        if(count!=1){
            flag = false;
        }

        return flag;

    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {

        List<Activity> aList = activityDao.getActivityListByClueId(clueId);

        return aList;
    }

    @Override
    public List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map) {

        List<Activity> aList= activityDao.getActivityListByNameAndNotByClueId(map);

        return aList;
    }

    @Override
    public List<Activity> getActivityByName(String aName) {

        List<Activity> aList = activityDao.getActivityByName(aName);

        return aList;
    }
}
