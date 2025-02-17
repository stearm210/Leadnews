package com.heima.search.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.search.service.ApUserSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.search.controller.v1
 * @Author: yanhongwei
 * @CreateTime: 2025-02-17  14:26
 * @Description: TODO
 * @Version: 1.0
 */

@RestController
@RequestMapping("/api/v1/history")
public class ApUserSearchController {
    @Autowired
    private ApUserSearchService apUserSearchService;
     /*
      * @Title: findUserSearch
      * @Author: pyzxW
      * @Date: 2025-02-17 14:29:05
      * @Params:  
      * @Return: null
      * @Description: APP用户搜索信息表 前端控制器
      */
    @PostMapping("/load")
    public ResponseResult findUserSearch() {
        return apUserSearchService.findUserSearch();
    }
}
