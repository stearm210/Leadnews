package com.heima.user.service.impl;

import com.heima.common.constants.BehaviorConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.dtos.UserRelationDto;
import com.heima.model.user.pojos.ApUser;
import com.heima.user.service.ApUserRelationService;
import com.heima.utils.thread.AppThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
    //redis操作，获取redis操作对象
    @Autowired
    private CacheService cacheService;

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
        //这里查找的是当前登录的用户的id
        Integer apUserId = user.getId();

        //3.关注操作与取消关注操作
        //查找文章作者id,作为一个值进行对应
        Integer followUserId = dto.getAuthorId();
        //加入redis内存中，方便快速读取
        //加入的参数为：key、value、score
        if(dto.getOperation() == 0) {
            // 将对方写入我的关注中
            cacheService.zAdd(BehaviorConstants.APUSER_FOLLOW_RELATION + apUserId, followUserId.toString(), System.currentTimeMillis());
            // 将我写入对方的粉丝中
            cacheService.zAdd(BehaviorConstants.APUSER_FANS_RELATION + followUserId, apUserId.toString(), System.currentTimeMillis());
        } else {
            // 取消关注
            cacheService.zRemove(BehaviorConstants.APUSER_FOLLOW_RELATION + apUserId, followUserId.toString());
            cacheService.zRemove(BehaviorConstants.APUSER_FANS_RELATION + followUserId, apUserId.toString());
        }

        //4.返回操作
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
