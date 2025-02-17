package com.heima.search.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.UserSearchDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.search.controller.v1
 * @Author: yanhongwei
 * @CreateTime: 2025-02-17  15:03
 * @Description: TODO
 * @Version: 1.0
 */

@RestController
@RequestMapping("/api/v1/associate")
public class ApAssociateWordsController {
     /*
      * @Title: search
      * @Author: pyzxW
      * @Date: 2025-02-17 15:06:14
      * @Params:  
      * @Return: null
      * @Description: 
      */
    @PostMapping("/search")
    public ResponseResult search(@RequestBody UserSearchDto userSearchDto) {
        return null;
    }
}
