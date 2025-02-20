package com.heima.behavior.service;

import com.heima.model.behavior.dtos.CollectionBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;

public interface ApCollectionService {
     /*
      * @Title: collect
      * @Author: pyzxW
      * @Date: 2025-02-20 15:32:46
      * @Params:
      * @Return: null
      * @Description: 藏对应的文章
      */
    public ResponseResult collect(CollectionBehaviorDto dto);
}
