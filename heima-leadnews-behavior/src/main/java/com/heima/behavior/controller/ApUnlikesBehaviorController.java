package com.heima.behavior.controller;

import com.heima.behavior.service.ApUnlikesBehaviorService;
import com.heima.model.behavior.dtos.UnLikesBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.behavior.controller
 * @Author: yanhongwei
 * @CreateTime: 2025-02-20  15:08
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
@RequestMapping("/api/v1/un_likes_behavior")
public class ApUnlikesBehaviorController {
    @Autowired
    private ApUnlikesBehaviorService apUnlikesBehaviorService;
     /*
      * @Title: unLikesBehavior
      * @Author: pyzxW
      * @Date: 2025-02-20 15:09:44
      * @Params:  
      * @Return: null
      * @Description: 用户行为之不喜欢操作
      */
    @PostMapping
    public ResponseResult unLikesBehavior(@RequestBody UnLikesBehaviorDto dto){
        return apUnlikesBehaviorService.unlike(dto);
    }
}
