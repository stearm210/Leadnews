package com.heima.article.controller.v1;

import com.heima.article.service.ApArticleService;
import com.heima.common.constants.ArticleConstants;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.article.controller.v1
 * @Author: yanhongwei
 * @CreateTime: 2025-01-07  15:10
 * @Description: TODO
 * @Version: 1.0
 */

@RestController
@RequestMapping("/api/v1/article")
public class ArticleHomeController {

    @Autowired
    private ApArticleService apArticleService;

     /*
      * @Title: load
      * @Author: pyzxW
      * @Date: 2025-01-07 15:16:12
      * @Params:
      * @Return: null
      * @Description: 加载首页
      */
    @PostMapping("/load")
    public ResponseResult load(@RequestBody ArticleHomeDto dto){
        return apArticleService.load2(dto, ArticleConstants.LOADTYPE_LOAD_MORE, true);
    }

      /*
       * @Title: loadmore
       * @Author: pyzxW
       * @Date: 2025-01-07 15:17:57
       * @Params:
       * @Return: null
       * @Description: 加载更多
       */
    @PostMapping("/loadmore")
    public ResponseResult loadmore(@RequestBody ArticleHomeDto dto){
        return apArticleService.load(dto, ArticleConstants.LOADTYPE_LOAD_MORE);
    }

      /*
       * @Title: loadnew
       * @Author: pyzxW
       * @Date: 2025-01-07 15:18:07
       * @Params:
       * @Return: null
       * @Description: 加载最新
       */
    @PostMapping("/loadnew")
    public ResponseResult loadnew(@RequestBody ArticleHomeDto dto){
        return apArticleService.load(dto, ArticleConstants.LOADTYPE_LOAD_NEW);
    }

}
