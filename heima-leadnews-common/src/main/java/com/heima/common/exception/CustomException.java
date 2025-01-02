package com.heima.common.exception;

import com.heima.model.common.enums.AppHttpCodeEnum;

 /*
  * @Title: CustomException
  * @Author: pyzxW
  * @Date: 2025-01-02 10:52:31
  * @Params:
  * @Return: null
  * @Description: 自定义异常类
  */
public class CustomException extends RuntimeException {

    private AppHttpCodeEnum appHttpCodeEnum;

    public CustomException(AppHttpCodeEnum appHttpCodeEnum){
        this.appHttpCodeEnum = appHttpCodeEnum;
    }

    public AppHttpCodeEnum getAppHttpCodeEnum() {
        return appHttpCodeEnum;
    }
}
