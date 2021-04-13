package com.sk.crm.settings.controller;

import com.sk.crm.settings.domain.Activity;
import com.sk.crm.settings.domain.Clue;
import com.sk.crm.settings.domain.Tran;
import com.sk.crm.settings.domain.User;
import com.sk.crm.settings.service.ActivityService;
import com.sk.crm.settings.service.ClueService;
import com.sk.crm.settings.service.UserService;
import com.sk.crm.utils.DateTimeUtil;
import com.sk.crm.utils.PrintJson;
import com.sk.crm.utils.UUIDUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/workbench/clue")
public class ClueController {

    @Resource
    private ClueService clueService;

    @Resource
    private UserService userService;

    @Resource
    private ActivityService activityService;

    @RequestMapping(value = "/getUserList.do")
    @ResponseBody
    private List<User> getUserList(){

        System.out.println("取得用户信息列表");

        List<User> uList = userService.getUserList();

        return uList;

    }

    /**
     * 添加线索
     * @param clue
     * @param session
     * @return
     */
    @RequestMapping(value = "/save.do")
    //@ResponseBody
    public void save(Clue clue, HttpSession session,HttpServletResponse response){
        clue.setId(UUIDUtil.getUUID());
        User user = (User) session.getAttribute("user");
        clue.setCreateBy(user.getName());
        clue.setCreateTime(DateTimeUtil.getSysTime());
        boolean flag = clueService.save(clue);
        PrintJson.printJsonFlag(response,flag);
    }

    /**
     * 跳转到线索活动详细信息页
     * @param id
     * @return
     */
    @RequestMapping(value = "/detail.do")
    public ModelAndView detail(String id) {

        System.out.println("进入到跳转详细信息页的操作");

        ModelAndView mv = new ModelAndView();

        Clue c = clueService.detail(id);

        //相当于request.setAttribute();
        mv.addObject("c",c);

        mv.setViewName("/workbench/clue/detail.jsp");

        return mv;
    }

    /**
     * 展现关联的市场活动信息列表
     * @param clueId
     * @return
     */
    @RequestMapping(value = "/getActivityListByClueId.do")
    @ResponseBody
    public List<Activity> getActivityListByClueId(String clueId){

        List<Activity> aList = activityService.getActivityListByClueId(clueId);

        return aList;
    }

    /**
     * 解除关联
     * @param id
     * @return
     */
    @RequestMapping(value = "/unBind.do")
    //@ResponseBody
    public void unBind(String id,HttpServletResponse response){

        boolean flag = clueService.unBind(id);

        PrintJson.printJsonFlag(response,flag);
    }

    /**
     * 查询市场活动列表，模糊查询
     * @param aName
     * @param clueId
     * @return
     */
    @RequestMapping(value = "/getActivityListByNameAndNotByClueId.do")
    @ResponseBody
    public List<Activity> getActivityListByNameAndNotByClueId(String aName, String clueId){

        Map<String,String> map = new HashMap<>();
        map.put("aName",aName);
        map.put("clueId",clueId);
        List<Activity> aList = activityService.getActivityListByNameAndNotByClueId(map);
        return aList;
    }

    /**
     * 关联市场活动
     * @return
     */
    @RequestMapping(value = "/bund.do")
    @ResponseBody
    public void bind(String cid,@RequestParam("aid") String[] aids,HttpServletResponse response) {

        boolean flag = clueService.bund(cid, aids);

        PrintJson.printJsonFlag(response, flag);
    }

    /**
     * 执行线索转换操作
     */
    @RequestMapping(value = "/getActivityByName.do")
    @ResponseBody
    public List<Activity> getActivityByName(String aName){
        List<Activity> aList = activityService.getActivityByName(aName);
        return aList;
    }

    /**
     * 执行线索转换操作
     */
    @RequestMapping(value = "/convertT.do")
    public ModelAndView convert(Tran t,String flag,String clueId,HttpSession session){

        ModelAndView mv = new ModelAndView();

        User user = (User) session.getAttribute("user");
        String createBy = user.getName();
        // 如果需要创建交易
        if ("a".equals(flag)){
            t.setId(UUIDUtil.getUUID());
            t.setCreateTime(DateTimeUtil.getSysTime());
            t.setCreateBy(createBy);
        } else {
            t = null;
        }
        boolean flag1 = clueService.convertT(clueId,t,createBy);
        if (flag1){
            mv.setViewName("redirect:/workbench/clue/index.jsp");
        }
        return mv;
    }

}


