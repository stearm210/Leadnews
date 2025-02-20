package com.heima.behavior.service;

import com.heima.model.behavior.dtos.ReadBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;

public interface ApReadBehaviorService {
     /*
      * @Title: readBehavior
      * @Author: pyzxW
      * @Date: 2025-02-20 14:14:27
      * @Params:  
      * @Return: null
      * @Description: 用户阅读操作
      */
    public ResponseResult readBehavior(ReadBehaviorDto dto);
}
