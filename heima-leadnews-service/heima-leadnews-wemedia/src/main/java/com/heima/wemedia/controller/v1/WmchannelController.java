package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.wemedia.service.WmChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.wemedia.controller.v1
 * @Author: yanhongwei
 * @CreateTime: 2025-01-12  16:06
 * @Description: TODO
 * @Version: 1.0
 */

@RestController
@RequestMapping("/api/v1/channel")
public class WmchannelController {
     /*
      * @Title: findAll
      * @Author: pyzxW
      * @Date: 2025-01-12 16:07:42
      * @Params:
      * @Return: null
      * @Description: 对频道列表进行查询
      */

    @Autowired
    private WmChannelService wmChannelService;

    @GetMapping("/channels")
    public ResponseResult findAll(){
        return wmChannelService.findAll();
    }

}
