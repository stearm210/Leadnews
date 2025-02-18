package com.heima.user.service.impl;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.dtos.UserRelationDto;
import com.heima.model.user.pojos.ApUser;
import com.heima.user.service.ApUserRelationService;
import com.heima.utils.thread.AppThreadLocalUtil;
import org.springframework.stereotype.Service;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.user.service.impl
 * @Author: yanhongwei
 * @CreateTime: 2025-02-18  16:02
 * @Description: TODO
 * @Version: 1.0
 */
@Service
public class ApUserRelationServiceImpl implements ApUserRelationService
{
     /*
      * @Title: follow
      * @Author: pyzxW
      * @Date: 2025-02-18 16:02:54
      * @Params:
      * @Return: null
      * @Description: 关注与取消关注
      */
    @Override
    public ResponseResult follow(UserRelationDto dto) {
        //1.参数校验
        //判断文章的参数是否出现问题
        if (dto.getOperation() == null || dto.getOperation() < 0 || dto.getOperation() > 1){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //2.判断用户是否登录
        //线程中获取用户信息
        ApUser user = AppThreadLocalUtil.getUser();
        if (user == null){
            //回复信息：需要登录操作
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        //获得对应的用户id信息
        Integer apUserId = user.getId();

        //3.关注操作与取消关注操作

        //4.返回操作

        return null;
    }
}
