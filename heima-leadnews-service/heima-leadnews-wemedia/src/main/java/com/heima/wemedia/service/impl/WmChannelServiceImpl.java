package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.wemedia.mapper.WmChannelMapper;
import com.heima.wemedia.service.WmChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class WmChannelServiceImpl extends ServiceImpl<WmChannelMapper, WmChannel> implements WmChannelService {
    
     /*
      * @Title: findAll
      * @Author: pyzxW
      * @Date: 2025-01-12 16:12:02
      * @Params:  
      * @Return: null
      * @Description: 查询所有频道
      */
    @Override
    public ResponseResult findAll() {
        // 查询所有频道，数据库查询
        return ResponseResult.okResult(list());
    }
}