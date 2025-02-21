package com.heima.apis.user;

import com.heima.model.user.pojos.ApUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("leadnews-user")
public interface IUserClient {
    @GetMapping("/api/v1/user/findUserById/{userId}")
    public ApUser findUserById(@PathVariable("userId") int userId);
}
