package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmNews;
import org.springframework.web.bind.annotation.RequestBody;

public interface WmNewsService extends IService<WmNews> {
     /*
      * @Title: findAll
      * @Author: pyzxW
      * @Date: 2025-01-12 16:41:13
      * @Params:
      * @Return: null
      * @Description: 条件查询文章列表
      */
    public ResponseResult findAll(WmNewsPageReqDto dto);
}