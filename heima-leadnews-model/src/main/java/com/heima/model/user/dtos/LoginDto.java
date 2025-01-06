package com.heima.model.user.dtos;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.model.user.dtos
 * @Author: yanhongwei
 * @CreateTime: 2025-01-06  10:55
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class LoginDto {
//    添加手机号以及密码
    @ApiModelProperty(value = "手机号",required = true)
    private String phone;
    @ApiModelProperty(value = "密码",required = true)
    private String password;
}
