package com.heima.article.controller.v1;

import com.heima.article.service.ApArticleService;
import com.heima.model.article.dtos.ArticleInfoDto;
import com.heima.model.common.dtos.ResponseResult;
import org.apache.commons.net.nntp.ArticleInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.article.controller.v1
 * @Author: yanhongwei
 * @CreateTime: 2025-02-20  15:56
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
@RequestMapping("/api/v1article/load_article_behavior")
public class ArticleInfoController {
    @Autowired
    private ApArticleService apArticleService;

     /*
      * @Title: loadArticleBehavior
      * @Author: pyzxW
      * @Date: 2025-02-20 16:04:48
      * @Params:
      * @Return: null
      * @Description:
      */
    @PostMapping
    public ResponseResult loadArticleBehavior(@RequestBody ArticleInfoDto dto) {
        return apArticleService.loadArticleBehavior(dto);
    }
}
