package com.heima.user.interceptor;

import com.heima.model.user.pojos.ApUser;
import com.heima.utils.thread.AppThreadLocalUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.user.interceptor
 * @Author: yanhongwei
 * @CreateTime: 2025-02-19  15:25
 * @Description: TODO
 * @Version: 1.0
 */
 /*
  * @Title:
  * @Author: pyzxW
  * @Date: 2025-02-19 15:27:15
  * @Params:  
  * @Return: null
  * @Description: 新增对应的拦截器操作
  */
public class AppTokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("userId");
        if(userId != null){
            //存入到当前线程中
            ApUser apUser = new ApUser();
            apUser.setId(Integer.valueOf(userId));
            AppThreadLocalUtil.setUser(apUser);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AppThreadLocalUtil.clear();
    }
}
