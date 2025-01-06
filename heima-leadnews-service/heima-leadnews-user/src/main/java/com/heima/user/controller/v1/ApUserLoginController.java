package com.heima.user.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.LoginDto;
import com.heima.user.service.ApUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
//app端用户登录注解操作
//主要用于swagger操作，方便在网页上进行查看
@Api(value = "app端用户登录",tags = "app端用户登录")
public class ApUserLoginController {

    @Autowired
    private ApUserService apUserService;

    @PostMapping("/login_auth")
    @ApiOperation("用户登录")
    //需要使用@RequestBoby对传过来的json数据进行封装
    public ResponseResult login(@RequestBody LoginDto dto){
        return apUserService.login(dto);
    }
}
