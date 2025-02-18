package com.heima.user.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.UserRelationDto;
import com.heima.user.service.ApUserRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.user.controller.v1
 * @Author: yanhongwei
 * @CreateTime: 2025-02-18  15:57
 * @Description: TODO
 * @Version: 1.0
 */

@RestController
@RequestMapping("/api/v1/user")
public class ApUserRelationController {
    @Autowired
    private ApUserRelationService apUserRelationService;

     /*
      * @Title: follow
      * @Author: pyzxW
      * @Date: 2025-02-18 16:02:11
      * @Params:
      * @Return: null
      * @Description: 关注与取消关注
      */
    @PostMapping("/user_follow")
    public ResponseResult follow(@RequestBody UserRelationDto dto){
        return apUserRelationService.follow(dto);
    }
}
