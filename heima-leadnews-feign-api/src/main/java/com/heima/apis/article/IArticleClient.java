package com.heima.apis.article;

import com.heima.apis.article.fallback.IArticleClientFallback;
import com.heima.model.article.dtos.ArticleCommentDto;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.comment.dtos.CommentConfigDto;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "leadnews-article", fallback = IArticleClientFallback.class)
public interface IArticleClient {

    /**
     * 保存文章
     * @param dto
     * @return
     */
    @PostMapping("/api/v1/article/save")
    public ResponseResult saveArticle(@RequestBody ArticleDto dto);

    /**
     * 查询文章配置信息
     * @param articleId
     * @return
     */
    @GetMapping("/api/v1/article/findArticleConfigByArticleId/{articleId}")
    ResponseResult findArticleConfigByArticleId(@PathVariable("articleId") Long articleId);

    @PostMapping("/api/v1/article/findNewsComments")
    PageResponseResult findNewsComments(@RequestBody ArticleCommentDto dto);

    @PostMapping("/api/v1/article/updateCommentStatus")
    ResponseResult updateCommentStatus(@RequestBody CommentConfigDto dto);
}