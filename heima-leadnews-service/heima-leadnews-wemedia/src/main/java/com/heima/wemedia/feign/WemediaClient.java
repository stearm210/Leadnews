package com.heima.wemedia.feign;
 
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.apis.wemedia.IWemediaClient;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.wemedia.service.WmChannelService;
import com.heima.wemedia.service.WmSensitiveService;
import com.heima.wemedia.service.WmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
 
@RestController
public class WemediaClient implements IWemediaClient {
 
    @Autowired
    private WmUserService wmUserService;
    @Autowired
    private WmSensitiveService wmSensitiveService;
 
    @Override
    @GetMapping("/api/v1/user/findByName/{name}")
    public WmUser findWmUserByName(@PathVariable("name") String name) {
        return wmUserService.getOne(Wrappers.<WmUser>lambdaQuery().eq(WmUser::getName, name));
    }
 
    @Override
    @PostMapping("/api/v1/wm_user/save")
    public ResponseResult saveWmUser(@RequestBody WmUser wmUser) {
        wmUserService.save(wmUser);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
 
    @Autowired
    private WmChannelService wmChannelService;
 
     /*
      * @Title: getChannels
      * @Author: pyzxW
      * @Date: 2025-02-22 20:40:19
      * @Params:  
      * @Return: null
      * @Description: 查找文章对应的频道
      */
    @GetMapping("/api/v1/channel/list")
    @Override
    public ResponseResult getChannels() {
        return wmChannelService.findAll();
    }
 
 
    @Override
    @PostMapping("/api/v1/wm_sensitive/check")
    public ResponseResult checkSensitive(String content) {
        return wmSensitiveService.checkSensitive(content);
    }
}