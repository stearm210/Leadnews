package com.heima.article.feign;

import com.heima.apis.article.IArticleClient;
import com.heima.article.service.ApArticleService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.article.feign
 * @Author: yanhongwei
 * @CreateTime: 2025-01-15  15:25
 * @Description: TODO
 * @Version: 1.0
 */


@RestController
public class ArticleClient implements IArticleClient {

    @Autowired
    private ApArticleService apArticleService;
     /*
      * @Title: saveArticle
      * @Author: pyzxW
      * @Date: 2025-01-15 15:26:49
      * @Params:
      * @Return: null
      * @Description: 实现feign接口,远程调用？
      */
    @PostMapping("api/v1/article/save")
    @Override
    public ResponseResult saveArticle(@RequestBody ArticleDto dto) {
        return apArticleService.saveArticle(dto);
    }
}
