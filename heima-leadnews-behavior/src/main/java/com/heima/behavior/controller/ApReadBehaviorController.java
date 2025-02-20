package com.heima.behavior.controller;

import com.heima.behavior.service.ApReadBehaviorService;
import com.heima.model.behavior.dtos.ReadBehaviorDto;
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
 * @CreateTime: 2025-02-20  14:08
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
@RequestMapping("/api/v1/read_behavior")
public class ApReadBehaviorController {
    @Autowired
    private ApReadBehaviorService apReadBehaviorService;
     /*
      * @Title: readBehavior
      * @Author: pyzxW
      * @Date: 2025-02-20 14:09:35
      * @Params:  
      * @Return: null
      * @Description: 记录文章查看次数、阅读文章等信息
      */
    @PostMapping
    public ResponseResult readBehavior(@RequestBody ReadBehaviorDto dto) {
        return apReadBehaviorService.readBehavior(dto);
    }
}
