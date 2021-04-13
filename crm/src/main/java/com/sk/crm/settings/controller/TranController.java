package com.sk.crm.settings.controller;

import com.sk.crm.settings.domain.Tran;
import com.sk.crm.settings.domain.TranHistory;
import com.sk.crm.settings.domain.User;
import com.sk.crm.settings.service.CustomerService;
import com.sk.crm.settings.service.TranService;
import com.sk.crm.settings.service.UserService;
import com.sk.crm.utils.DateTimeUtil;
import com.sk.crm.utils.UUIDUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/workbench/transaction")
public class TranController {

    @Resource
    private UserService userService;
    @Resource
    private TranService tranService;
    @Resource
    private CustomerService customerService;


    /**
     * 跳转到交易页面交易添加页
     */
    @RequestMapping(value = "/add.do")
    public ModelAndView skipTransactionSave(){

        ModelAndView mv = new ModelAndView();
        List<User> uList = userService.getUserList();
        mv.addObject("uList",uList);
        mv.setViewName("/workbench/transaction/save.jsp");

        return mv;
    }

    /**
     * 取得客户名称列表，按照客户名称进行模糊查询
     * @return
     */
    @RequestMapping(value = "/getCustomerName.do")
    @ResponseBody
    public List<String> getCustomerName(String name){

        List<String> sList = customerService.getCustomerName(name);

        return sList;
    }

    /**
     * 添加交易
     * @return
     */
    @RequestMapping(value = "/save.do")
    public ModelAndView save(Tran t, HttpServletRequest request){

        ModelAndView mv = new ModelAndView();

        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        User user = (User) request.getSession().getAttribute("user");
        String createBy = user.getName();
        String customerName = request.getParameter("customerName");

        t.setId(id);
        t.setCreateBy(createBy);
        t.setCreateTime(createTime);

        boolean flag = tranService.save(t,customerName);
        if (flag){
            //如果添加交易成功,跳转到列表页
            mv.setViewName("redirect:/workbench/transaction/index.jsp");
        }
        return mv;
    }

    /**
     * 跳转到交易页面详细信息页
     */
    @RequestMapping(value = "/detail.do")
    public ModelAndView skipTransactionDetail(String id,HttpServletRequest request){

        ModelAndView mv = new ModelAndView();

        Tran t = tranService.detail(id);
        /**
         * 取可能性
         */
        String stage = t.getStage();
        Map<String,String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);
        t.setPossibility(possibility);

        mv.addObject("t",t);
        //mv.addObject("possibility",possibility);
        mv.setViewName("/workbench/transaction/detail.jsp");

        return mv;
    }

    /**
     * 根据交易 id 取得相应的历史列表
     * @param tranId
     * @return
     */
    @RequestMapping(value = "/getHistoryListByTranId.do")
    @ResponseBody
    public List<TranHistory> getHistoryListByTranId(String tranId, HttpServletRequest request){

        List<TranHistory> thList = tranService.getHistoryListByTranId(tranId);

        // 阶段和可能性之间的对应关系
        Map<String,String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");
        // 将交易历史列表遍历
        for(TranHistory th : thList){

            //根据每条交易历史,取出每一个阶段
            String stage = th.getStage();
            String possibility = pMap.get(stage);
            th.setPossibility(possibility);
        }

        return thList;
    }

    @RequestMapping(value = "/changeStage.do")
    @ResponseBody
    public Map<String,Object> changeStage(Tran t,HttpServletRequest request){
        String editTime = DateTimeUtil.getSysTime();
        User user = (User) request.getSession().getAttribute("user");
        String editBy = user.getName();
        t.setEditBy(editBy);
        t.setEditTime(editTime);
        boolean flag = tranService.changeStage(t);

        // 阶段和可能性之间的对应关系
        Map<String,String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");
        t.setPossibility(pMap.get(t.getStage()));

        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("t",t);
        return map;
    }

    /**
     * 交易统计图
     */
    @RequestMapping(value = "/getCharts.do")
    @ResponseBody
    public Map<String,Object> getCharts(){

        Map<String,Object> map = tranService.getCharts();

        return map;
    }

}
