package com.sk.crm.settings.controller;


import com.sk.crm.settings.domain.User;
import com.sk.crm.settings.service.UserService;
import com.sk.crm.utils.MD5Util;
import com.sk.crm.utils.PrintJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Controller
public class UserController {

    //自动注入
    @Resource
    private UserService service;

    @RequestMapping(value = "/settings/user/login.do")
    private void login(String loginAct, String loginPwd, HttpServletRequest request, HttpServletResponse response)  {

        //将密码的明文形式转换为MD5的密文形式
        String pwd = MD5Util.getMD5(loginPwd);
        //接收浏览器端的ip地址
        String ip = request.getRemoteAddr();
        System.out.println("=========" + ip);

        try{
            User user = service.login(loginAct,pwd,ip);
            //把user对象保存到session域中
            request.getSession().setAttribute("user",user);

            /*
                如果程序执行到此处,说明业务层没有为controller抛出任何的异常
                //表示登录成功
                {"success":true}
             */
            PrintJson.printJsonFlag(response,true);
        }catch(LoginException e){

            e.printStackTrace();

            //一旦程序执行了catch快的信息,说明业务层为我们验证登录失败,为controller抛出了异常,表示登录失败
            /*
                {"success":false,"msg":?}
             */

            String msg = e.getMessage();

            /*
                我们现在作为controller,需要为ajax提供多项信息

                可以有两种手段来处理
                    1.将多项信息打包为map,将map解析为json串
                    2.创建一个Vo
                       private boolean success;
                       private String msg;

                       如果对于展现的信息将来还会大量的使用,我们创建一个Vo类,使用方便
                       如果对于展现的信息只有在这个需求中使用,我们使用map就可以了
             */

            HashMap<String, Object> map = new HashMap<>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);

        }

    }
}
