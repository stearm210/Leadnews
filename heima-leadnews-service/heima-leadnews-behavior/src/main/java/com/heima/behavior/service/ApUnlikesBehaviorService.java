package com.heima.behavior.service;

import com.heima.model.behavior.dtos.UnLikesBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;

public interface ApUnlikesBehaviorService {
     /*
      * @Title: unlike
      * @Author: pyzxW
      * @Date: 2025-02-20 15:12:03
      * @Params:
      * @Return: null
      * @Description: 用户行为之不喜欢操作
      */
    public ResponseResult unlike(UnLikesBehaviorDto dto);
}
