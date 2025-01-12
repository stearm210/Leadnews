package com.heima.wemedia.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.service.WmNewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class WmNewsServiceImpl  extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {

     /*
      * @Title: findAll
      * @Author: pyzxW
      * @Date: 2025-01-12 16:41:38
      * @Params:
      * @Return: null
      * @Description: 条件查询列表
      */
    @Override
    public ResponseResult findAll(WmNewsPageReqDto dto) {
        return null;
    }
}
