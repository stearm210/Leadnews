package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

public interface WmMaterialService extends IService<WmMaterial> {
    public ResponseResult uploadPicture(MultipartFile multipartFile);

     /*
      * @Title: findList
      * @Author: pyzxW
      * @Date: 2025-01-12 15:14:56
      * @Params:
      * @Return: null
      * @Description: 对素材进行管理
      */
    public ResponseResult findList(WmMaterialDto dto);
}