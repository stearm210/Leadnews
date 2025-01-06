package com.heima.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.LoginDto;
import com.heima.model.user.pojos.ApUser;

 /*
  * @Title:
  * @Author: pyzxW
  * @Date: 2025-01-06 11:14:04
  * @Params:
  * @Return: null
  * @Description: app端登录功能
  */
public interface ApUserService extends IService<ApUser> {
     public ResponseResult login(LoginDto dto);
}
