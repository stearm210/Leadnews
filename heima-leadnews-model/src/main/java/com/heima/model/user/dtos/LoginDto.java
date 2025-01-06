package com.heima.model.user.dtos;

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
    private String phone;
    private String password;
}
