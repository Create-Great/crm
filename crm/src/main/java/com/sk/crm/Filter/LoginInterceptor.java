package com.sk.crm.Filter;

import com.sk.crm.settings.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("验证有没有登录过");

        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");

        //不应该拦截的资源,自动放行请求
        //String path = request.getServletPath();

        //如果user不为null,说明登录过
        if(user != null){
            return true;

            //没有登录过
        }else{

            //重定向到登录页

            /*
                重定向的路径写法：
                在实际项目开发中,对于路径的使用,无论是转发还是重定向,都使用绝对路径
                转发:
                    使用的是一种特殊的绝对路径的使用方式,这种绝对路径前面不加项目名,也称为内部路径
                    /login.jsp
                重定向:
                    使用的是传统绝对路径的写法,前面必须以/项目名开头,后面跟具体的资源路径
                    /crm/login.jsp

                为什么使用重定向
                    转发之后,路径会停留在老路径上,而不是跳转之后最新资源的路径
                    我们应该在为用户跳转到登录页的同时,浏览器的地址应该自动设置为登录页的路径
             */

            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }

        return false;
    }
}
