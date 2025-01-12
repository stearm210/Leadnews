package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;

public interface WmChannelService extends IService<WmChannel> {
     /*
      * @Title: findAll
      * @Author: pyzxW
      * @Date: 2025-01-12 16:11:32
      * @Params:
      * @Return: null
      * @Description: 查询所有频道
      */
    public ResponseResult findAll();
}