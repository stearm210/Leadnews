package com.heima.apis.wemedia;
 
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
 
@FeignClient("leadnews-wemedia")
public interface IWemediaClient {
 
 
    @GetMapping("/api/v1/user/findByName/{name}")
    WmUser findWmUserByName(@PathVariable("name") String name);
 
    @PostMapping("/api/v1/wm_user/save")
    ResponseResult saveWmUser(@RequestBody WmUser wmUser);
 
     /*
      * @Title: getChannels
      * @Author: pyzxW
      * @Date: 2025-02-22 20:39:20
      * @Params:  
      * @Return: null
      * @Description: 查询文章的所有频道
      */
    @GetMapping("/api/v1/channel/list")
    public ResponseResult getChannels();
 
    @PostMapping("/api/v1/wm_sensitive/check")
    ResponseResult checkSensitive(@RequestBody String content);
}