package com.heima.behavior.controller;

import com.heima.model.behavior.dtos.CollectionBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.behavior.controller
 * @Author: yanhongwei
 * @CreateTime: 2025-02-20  15:30
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
@RequestMapping("/api/v1/collection_behavior")
public class ApCollectionController {

     /*
      * @Title: collection
      * @Author: pyzxW
      * @Date: 2025-02-20 15:32:52
      * @Params:
      * @Return: null
      * @Description: 收藏对应的文章
      */
    @PostMapping
    public ResponseResult collection(@RequestBody CollectionBehaviorDto dto){
        return null;
    }
}
