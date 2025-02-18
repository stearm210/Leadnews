package com.heima.user.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.UserRelationDto;

public interface ApUserRelationService {
     /*
      * @Title: follow
      * @Author: pyzxW
      * @Date: 2025-02-18 16:01:56
      * @Params:  
      * @Return: null
      * @Description: 关注与取消关注
      */
    public ResponseResult follow(UserRelationDto dto);
}
