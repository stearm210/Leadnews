package com.heima.search.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.UserSearchDto;
import com.heima.search.service.ArticleSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.search.controller.v1
 * @Author: yanhongwei
 * @CreateTime: 2025-02-15  21:02
 * @Description: TODO
 * @Version: 1.0
 */
 /*
  * @Title:
  * @Author: pyzxW
  * @Date: 2025-02-15 21:03:52
  * @Params:
  * @Return: null
  * @Description: 搜索接口定义
  */
@RestController
@RequestMapping("/api/v1/article/search")
public class ArticleSearchController {
    @Autowired
    private ArticleSearchService articleSearchService;

    @PostMapping("/search")
    public ResponseResult search(@RequestBody UserSearchDto dto) throws IOException {
        return articleSearchService.search(dto);
    }
}
