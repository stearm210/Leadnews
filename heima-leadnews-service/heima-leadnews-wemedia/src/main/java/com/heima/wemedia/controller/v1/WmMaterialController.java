package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
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

    //上传图片至服务器
    @PostMapping("/upload_picture")
    public ResponseResult uploadPicture(MultipartFile multipartFile){
        return null;
    }
}
