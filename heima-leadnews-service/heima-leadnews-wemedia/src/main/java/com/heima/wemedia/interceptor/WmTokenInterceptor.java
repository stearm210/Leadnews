package com.heima.wemedia.interceptor;

import com.heima.model.wemedia.pojos.WmUser;
import com.heima.utils.thread.WmThreadLocalUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.wemedia.interceptor
 * @Author: yanhongwei
 * @CreateTime: 2025-01-11  19:38
 * @Description: TODO
 * @Version: 1.0
 */

/*
* 由于之前已经在token中得到了对应的信息并存入了header中，这时需要将对应的信息从header中取出来，这些取出的信息将会放在线程中，方便后续使用。下面是两个对线程的操作方法
* */
public class WmTokenInterceptor implements HandlerInterceptor {

     /*
      * @Title: preHandle
      * @Author: pyzxW
      * @Date: 2025-01-11 19:40:16
      * @Params:
      * @Return: null
      * @Description: 用于得到header中的用户信息
      */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //得到header中的信息
        String userId = request.getHeader("userId");
        //判断对应的userid是否为空
        if (userId != null){
            //把用户id存入threadloacl中
            WmUser wmUser = new WmUser();
            wmUser.setId(Integer.valueOf(userId));
            WmThreadLocalUtil.setUser(wmUser);
        }
        Optional<String> optional = Optional.ofNullable(userId);


        return true;
    }


     /*
      * @Title: postHandle
      * @Author: pyzxW
      * @Date: 2025-01-11 19:41:00
      * @Params:
      * @Return: null
      * @Description: 清理线程中的用户信息
      */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        WmThreadLocalUtil.clear();
    }
}
