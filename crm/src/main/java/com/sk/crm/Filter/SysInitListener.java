package com.sk.crm.Filter;

import com.sk.crm.settings.domain.DicValue;
import com.sk.crm.settings.service.DicService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {

        //System.out.println("上下文作用域对象创建");
        ServletContext application = event.getServletContext();

        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(application);
        DicService dicService  =  ctx.getBean(DicService.class);
        Map<String, List<DicValue>> map = dicService.getAll();
        //将map解析为上下文域对象的键值对
        Set<String> set = map.keySet();
        for (String key : set) {
            application.setAttribute(key,map.get(key));
        }

        //数据字典处理完毕后,处理Stage2Possibility.properties文件
        /*
            步骤:
                解析该文件,将该属性文件中的键值对关系处理成为java中键值对关系(map)
         */
        Map<String,String> pMap = new HashMap<>();

        ResourceBundle rb = ResourceBundle.getBundle("/conf/Stage2Possibility");

        Enumeration<String> e = rb.getKeys();
        while (e.hasMoreElements()){
            //阶段
            String key = e.nextElement();
            //可能性
            String value = rb.getString(key);

            pMap.put(key,value);
        }
        application.setAttribute("pMap",pMap);

    }



    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
