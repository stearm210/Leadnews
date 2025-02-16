package com.heima.search.interceptor;

import com.heima.model.user.pojos.ApUser;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.utils.thread.AppThreadLocalUtil;
import com.heima.utils.thread.WmThreadLocalUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
*@BelongsProject: heima-leadnews
*@BelongsPackage: com.heima.search.interceptor
*@Author: yanhongwei
*@CreateTime: 2025-02-16  20:21
*@Description: TODO
*@Version: 1.0
*/

//拦截器之编写操作
public class AppTokenInterceptor implements HandlerInterceptor {
     /*
      * @Title: preHandle
      * @Author: pyzxW
      * @Date: 2025-02-16 20:25:12
      * @Params:  
      * @Return: null
      * @Description: 
      */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //得到header中的信息
        String userId = request.getHeader("userId");
        //判断对应的userid是否为空
        if (userId != null){
            //把用户id存入threadloacl中
            ApUser apUser = new ApUser();
            apUser.setId(Integer.valueOf(userId));
            AppThreadLocalUtil.setUser(apUser);
        }
        Optional<String> optional = Optional.ofNullable(userId);
        return true;
    }

     /*
      * @Title: afterCompletion
      * @Author: pyzxW
      * @Date: 2025-02-16 20:29:50
      * @Params:
      * @Return: null
      * @Description: 清理内存
      */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AppThreadLocalUtil.clear();
    }




     /*
      * @Title: postHandle
      * @Author: pyzxW
      * @Date: 2025-02-16 20:25:16
      * @Params:  
      * @Return: null
      * @Description: 
      */
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        AppThreadLocalUtil.clear();
//    }
}
