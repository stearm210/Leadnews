package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.wemedia.service.WmMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.wemedia.controller.v1
 * @Author: yanhongwei
 * @CreateTime: 2025-01-11  19:59
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
@RequestMapping("/api/v1/material")
public class WmMaterialController {
    @Autowired
    private WmMaterialService wmMaterialService;

    //上传图片至服务器
    @PostMapping("/upload_picture")
    public ResponseResult uploadPicture(MultipartFile multipartFile){
        return wmMaterialService.uploadPicture(multipartFile);
    }

     /*
      * @Title: findList
      * @Author: pyzxW
      * @Date: 2025-01-12 15:09:32
      * @Params:
      * @Return: null
      * @Description: 素材管理，接口定义.素材列表查询
      */
    public ResponseResult findList(){
        return null;
    }
}
