package com.heima.user.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.LoginDto;
import com.heima.user.service.ApUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.user.controller.v1
 * @Author: yanhongwei
 * @CreateTime: 2025-01-06  10:48
 * @Description: TODO
 * @Version: 1.0
 */

@RestController
@RequestMapping("/api/v1/login")
public class ApUserLoginController {

    @Autowired
    private ApUserService apUserService;

    @PostMapping("/login_auth")
    //需要使用@RequestBoby对传过来的json数据进行封装
    public ResponseResult login(@RequestBody LoginDto dto){
        return apUserService.login(dto);
    }
}
