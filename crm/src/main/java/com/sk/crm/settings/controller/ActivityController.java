package com.sk.crm.settings.controller;

import com.sk.crm.settings.domain.Activity;
import com.sk.crm.settings.domain.ActivityRemark;
import com.sk.crm.settings.domain.User;
import com.sk.crm.settings.service.ActivityService;
import com.sk.crm.settings.service.UserService;
import com.sk.crm.utils.DateTimeUtil;
import com.sk.crm.utils.PrintJson;
import com.sk.crm.utils.UUIDUtil;
import com.sk.crm.vo.PaginationVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ActivityController {

    @Resource
    private UserService service;

    @Resource
    private ActivityService activityService;

    @RequestMapping(value = "/workbench/activity/getUserList.do")
    private void getUserList(HttpServletRequest request, HttpServletResponse response){

        System.out.println("取得用户信息列表");

        List<User> uList = service.getUserList();

        //将uList解析成json格式
        PrintJson.printJsonObj(response,uList);
    }

    @RequestMapping(value = "/workbench/activity/save.do")
    private void save(HttpServletRequest request, HttpServletResponse response,Activity a){

        System.out.println("执行市场活动添加操作");

        String id = UUIDUtil.getUUID();
        //创建时间,当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人,当前登录用户
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        a.setId(id);
        a.setCreateTime(createTime);
        a.setCreateBy(createBy);

        boolean flag = activityService.save(a);

        PrintJson.printJsonFlag(response,flag);

    }

    @RequestMapping(value = "/workbench/activity/pageList.do")
    private void pageList(HttpServletResponse response,Activity a,String pageNo,String pageSize){

        System.out.println("进入市场活动信息列表(记录条数+分页查询)");

        //计算出略过的记录数
        int pageNoStr = Integer.valueOf(pageNo);
        int pageSizeStr = Integer.valueOf(pageSize);

        int skipCount = (pageNoStr-1)*pageSizeStr;

        HashMap<String, Object> map = new HashMap<>();
        map.put("name",a.getName());
        map.put("owner",a.getOwner());
        map.put("startDate",a.getStartDate());
        map.put("endDate",a.getEndDate());
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSizeStr);

        /*
            前端要:市场活动信息列表
                    查询的总条数

                    使用vo
                    PaginationVO<T>
                        private int total;
                        private List<T> dataList;

                    PaginationVO<Activity> vo = new PaginationVO();
                    vo.setTotal(total);
                    vo.setDataList(dataList);
                    PrintJSON vo ---> json
                    {"total":total,"dataList":[{市场活动1},{2},{3}]}

                    将来分页查询,每个模块都有,所以我们选择使用一个通用vo,操作起来比较方便

         */
        PaginationVO<Activity> vo = activityService.pageList(map);
        PrintJson.printJsonObj(response,vo);
    }

    @RequestMapping(value = "/workbench/activity/delete.do")
    private void delete(HttpServletRequest request,HttpServletResponse response){

        System.out.println("执行市场活动的删除操作");

        String[] ids = request.getParameterValues("id");
        boolean flag = activityService.delete(ids);
        PrintJson.printJsonFlag(response,flag);
    }

    @RequestMapping(value = "/workbench/activity/getUserListAndActivity.do")
    private void getUserListAndActivity(HttpServletResponse response,String id){

        System.out.println("进入到查询用户信息列表和根据市场活动id查询单条记录的操作");

        Map<String,Object> map = activityService.getUserListAndActivity(id);;
        PrintJson.printJsonObj(response,map);

    }

    @RequestMapping(value = "/workbench/activity/update.do")
    private void update(HttpServletRequest request, HttpServletResponse response,Activity a){

        System.out.println("执行市场活动修改操作");

        //修改时间,当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        //修改人,当前登录用户
        String editBy = ((User)request.getSession().getAttribute("user")).getName();

        a.setEditTime(editTime);
        a.setEditBy(editBy);

        boolean flag = activityService.update(a);

        PrintJson.printJsonFlag(response,flag);

    }

    @RequestMapping(value = "/workbench/activity/detail.do")
    public ModelAndView detail(String id) {

        System.out.println("进入到跳转详细信息页的操作");

        ModelAndView mv = new ModelAndView();

        Activity a = activityService.detail(id);

        //相当于request.setAttribute();
        mv.addObject("a",a);

        mv.setViewName("/workbench/activity/detail.jsp");

        return mv;
    }

    @RequestMapping(value = "/workbench/activity/getRemarkListByAid.do")
    public void getRemarkListByAid(String activityId,HttpServletResponse response){

        System.out.println("根据市场活动id取得备注信息列表");

        List<ActivityRemark> arList = activityService.getRemarkListByAid(activityId);

        PrintJson.printJsonObj(response,arList);

    }

    @RequestMapping(value = "/workbench/activity/deleteRemark.do")
    public void deleteRemark(String id,HttpServletResponse response){

        System.out.println("删除市场活动信息记录");

        boolean flag = activityService.deleteRemark(id);

        PrintJson.printJsonFlag(response,flag);

    }

    @RequestMapping(value = "/workbench/activity/saveRemark.do")
    public void saveRemark(ActivityRemark ar,HttpServletRequest request,HttpServletResponse response){

        System.out.println("添加备注操作");

        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        ar.setId(id);
        ar.setCreateTime(createTime);
        ar.setCreateBy(createBy);
        ar.setEditFlag(editFlag);

        boolean flag = activityService.saveRemark(ar);

        HashMap<String, Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("ar",ar);

        PrintJson.printJsonObj(response,map);
    }

    @RequestMapping(value = "/workbench/activity/updateRemark.do")
    public void updateRemark(ActivityRemark ar,HttpServletRequest request,HttpServletResponse response){

        System.out.println("修改备注信息的操作");

        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        ar.setEditTime(editTime);
        ar.setEditBy(editBy);
        ar.setEditFlag(editFlag);

        boolean flag = activityService.updateRemark(ar);

        HashMap<String, Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("ar",ar);

        PrintJson.printJsonObj(response,map);
    }
}
