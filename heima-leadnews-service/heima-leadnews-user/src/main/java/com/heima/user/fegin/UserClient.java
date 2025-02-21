package com.heima.user.fegin;

import com.heima.apis.user.IUserClient;
import com.heima.model.user.pojos.ApUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.user.fegin
 * @Author: yanhongwei
 * @CreateTime: 2025-02-21  17:05
 * @Description: TODO
 * @Version: 1.0
 */
public class UserClient implements IUserClient {
    @Override
    @GetMapping("/api/v1/user/findUserById/{userId}")
    public ApUser findUserById(@PathVariable("userId") int userId) {
        return null;
    }
}
