package com.heima.behavior.service.Impl;

import com.heima.behavior.service.ApLikesBehaviorService;
import com.heima.common.redis.CacheService;
import com.heima.model.behavior.dtos.LikesBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.pojos.ApUser;
import com.heima.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.behavior.service.Impl
 * @Author: yanhongwei
 * @CreateTime: 2025-02-19  16:10
 * @Description: TODO
 * @Version: 1.0
 */
@Service
@Transactional
@Slf4j
public class ApLikesBehaviorServiceImpl implements ApLikesBehaviorService {
    @Autowired
    private CacheService cacheService;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    /**
     * 点赞
     *
     * @param dto 0:点赞 1:取消点赞
     * @return
     */
    @Override
    public ResponseResult like(LikesBehaviorDto dto) {
        // 1. 检查参数
        if(dto == null || dto.getArticleId() == null || checkParam(dto)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        // 2. 是否登录
        ApUser apUser = AppThreadLocalUtil.getUser();
        if(apUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        return null;
    }

     /*
      * @Title: checkParam
      * @Author: pyzxW
      * @Date: 2025-02-19 16:12:59
      * @Params:
      * @Return: null
      * @Description: 检查参数操作
      */
    private boolean checkParam(LikesBehaviorDto dto) {
        //参数有误
        if(dto.getType() > 2 || dto.getType() < 0 || dto.getOperation() > 1 || dto.getOperation() < 0) {
            return true;
        }
        return false;
    }
}
