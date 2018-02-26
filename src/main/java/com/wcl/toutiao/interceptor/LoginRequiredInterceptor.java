package com.wcl.toutiao.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.wcl.toutiao.model.HostHolder;

/**
 * @ClassName: LoginRequiredInterceptor 
 * @Description: TODO 
 * @author Lulu
 * @date 2017年11月28日 上午9:49:41 
 */
@Component
public class LoginRequiredInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private HostHolder hostholder;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 如果经过第一个拦截器，hostholder中没有User的话，则该用户未登录
        if (request.getAttribute("user") == null) {
            // 权限页面不能访问，通过response发送一个重定位请求，这里是一个写好的弹出框
            response.sendRedirect("/?pop=1");
            return false;// 结束当前的请求
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO Auto-generated method stub
        super.afterCompletion(request, response, handler, ex);
    }
    
}
