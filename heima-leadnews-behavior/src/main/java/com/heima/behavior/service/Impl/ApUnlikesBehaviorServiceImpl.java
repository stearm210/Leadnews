package com.heima.behavior.service.Impl;

import com.alibaba.fastjson.JSON;
import com.heima.behavior.service.ApUnlikesBehaviorService;
import com.heima.common.constants.BehaviorConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.behavior.dtos.UnLikesBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.pojos.ApUser;
import com.heima.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.behavior.service.Impl
 * @Author: yanhongwei
 * @CreateTime: 2025-02-20  15:13
 * @Description: TODO
 * @Version: 1.0
 */
@Slf4j
@Service
@Transactional
public class ApUnlikesBehaviorServiceImpl implements ApUnlikesBehaviorService {
    @Autowired
    private CacheService cacheService;
     /*
      * @Title: unlike
      * @Author: pyzxW
      * @Date: 2025-02-20 15:13:35
      * @Params:
      * @Return: null
      * @Description: 用户行为之不喜欢操作
      */
    @Override
    public ResponseResult unlike(UnLikesBehaviorDto dto) {
        // 1. 检查参数
        if(dto.getArticleId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        // 2. 获取用户信息
        ApUser user = AppThreadLocalUtil.getUser();
        if(user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        //3.对应操作
        if (dto.getType() == 0){
            //不喜欢操作
            log.info("保存当前key: {} {} {}", dto.getArticleId(), user.getId(), dto);
            cacheService.hPut(BehaviorConstants.UN_LIKE_BEHAVIOR + dto.getArticleId().toString(), user.getId().toString(), JSON.toJSONString(dto));
        }else {
            log.info("删除当前key:{} ,{}, {}", dto.getArticleId(), user.getId(), dto);
            cacheService.hDelete(BehaviorConstants.UN_LIKE_BEHAVIOR + dto.getArticleId().toString(), user.getId().toString());
        }

        //4.返回结果
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
