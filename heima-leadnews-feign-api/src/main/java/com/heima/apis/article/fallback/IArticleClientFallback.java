package com.heima.apis.article.fallback;

import com.heima.apis.article.IArticleClient;
import com.heima.model.article.dtos.ArticleCommentDto;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.comment.dtos.CommentConfigDto;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.springframework.stereotype.Component;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.apis.article.fallback
 * @Author: yanhongwei
 * @CreateTime: 2025-01-18  16:01
 * @Description: TODO
 * @Version: 1.0
 */

@Component
public class IArticleClientFallback implements IArticleClient {
    /*
    * - 服务降级是服务自我保护的一种方式，或者保护下游服务的一种方式，用于确保服务不会受请求突增影响变得不可用，确保服务不会崩溃

- 服务降级虽然会导致请求失败，但是不会导致阻塞。
    * */
     /*
      * @Title: saveArticle
      * @Author: pyzxW
      * @Date: 2025-01-18 16:02:12
      * @Params:  
      * @Return: null
      * @Description: 在heima-leadnews-feign-api编写降级逻辑
      */
    @Override
    public ResponseResult saveArticle(ArticleDto dto)  {
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR,"获取数据失败");
    }

    @Override
    public ResponseResult findArticleConfigByArticleId(Long articleId) {
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR, "获取数据失败");
    }

    @Override
    public PageResponseResult findNewsComments(ArticleCommentDto dto) {
        PageResponseResult responseResult = new PageResponseResult(dto.getPage(),dto.getSize(),0);
        responseResult.setCode(501);
        responseResult.setErrorMessage("获取数据失败");
        return responseResult;
    }

    @Override
    public ResponseResult updateCommentStatus(CommentConfigDto dto) {
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR, "更新评论设置失败");
    }

}
