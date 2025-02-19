package com.heima.behavior.service.Impl;

import com.heima.behavior.service.ApLikesBehaviorService;
import com.heima.model.behavior.dtos.LikesBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;
import lombok.extern.slf4j.Slf4j;
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
    /**
     * 点赞
     *
     * @param dto 0:点赞 1:取消点赞
     * @return
     */
    @Override
    public ResponseResult like(LikesBehaviorDto dto) {
        return null;
    }
}
