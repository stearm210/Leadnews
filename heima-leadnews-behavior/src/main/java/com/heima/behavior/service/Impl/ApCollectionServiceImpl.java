package com.heima.behavior.service.Impl;

import com.alibaba.fastjson.JSON;
import com.heima.behavior.service.ApCollectionService;
import com.heima.common.constants.BehaviorConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.behavior.dtos.CollectionBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.pojos.ApUser;
import com.heima.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.behavior.service.Impl
 * @Author: yanhongwei
 * @CreateTime: 2025-02-20  15:33
 * @Description: TODO
 * @Version: 1.0
 */
@Slf4j
@Service
@Transactional
public class ApCollectionServiceImpl implements ApCollectionService {
    @Autowired
    private CacheService cacheService;
     /*
      * @Title: collect
      * @Author: pyzxW
      * @Date: 2025-02-20 15:34:01
      * @Params:
      * @Return: null
      * @Description: 收藏对应的文章
      */
    @Override
    public ResponseResult collect(CollectionBehaviorDto dto) {
        // 1. 检查参数
        if(dto.getEntryId() == null || dto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        // 2. 判断是否登录
        ApUser user = AppThreadLocalUtil.getUser();
        if(user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        //3.查询是否有收藏？
        String collectionJson = (String) cacheService.hGet(BehaviorConstants.COLLECTION_BEHAVIOR + user.getId(), dto.getEntryId().toString());
        if(StringUtils.isNotBlank(collectionJson) && dto.getOperation() == 0) {
            // 已收藏
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "已收藏");
        }

        //4.收藏操作
        if (dto.getOperation() == 0){
            log.info("文章收藏，保存key: {}, {}, {}", dto.getEntryId(), user.getId().toString(), JSON.toJSONString(dto));
            cacheService.hPut(BehaviorConstants.COLLECTION_BEHAVIOR + user.getId(), dto.getEntryId().toString(), JSON.toJSONString(dto));
        }else {
// 取消收藏
            log.info("文章收藏，删除key: {}, {}, {}", dto.getEntryId().toString(), user.getId().toString(), JSON.toJSONString(dto));
            cacheService.hDelete(BehaviorConstants.COLLECTION_BEHAVIOR + user.getId(), dto.getEntryId().toString());
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
