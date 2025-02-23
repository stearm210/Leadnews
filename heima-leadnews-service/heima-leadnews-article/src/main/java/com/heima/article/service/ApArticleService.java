package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.article.dtos.ArticleCommentDto;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.dtos.ArticleInfoDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.mess.ArticleVisitStreamMess;
import org.springframework.web.bind.annotation.RequestBody;

public interface ApArticleService extends IService<ApArticle> {
     /*
      * @Title: load
      * @Author: pyzxW
      * @Date: 2025-01-07 15:34:30
      * @Params: type 1 加载更多 2 加载最新
      * @Return: null
      * @Description: 加载文章列表
      */
//    业务层用于接收持久层传回来的参数(文章的list)，并且转换为controller需要进行返回的参数，也就是ResponseResult
    public ResponseResult load(ArticleHomeDto dto, Short type);

    /**
     * 加载文章列表
     * @param dto
     * @param type  1 加载更多   2 加载最新
     * @param firstPage  true  是首页  flase 非首页
     * @return
     */
    public ResponseResult load2(ArticleHomeDto dto,Short type,boolean firstPage);

     /*
      * @Title: saveArticle
      * @Author: pyzxW
      * @Date: 2025-01-15 15:28:24
      * @Params:
      * @Return: null
      * @Description:保存app端相关文章
      */
    public ResponseResult saveArticle( ArticleDto dto);

    /**
     * 更新文章的分值，同时更新缓存中的热点文章数据
     * @param mess
     *
     */
    public void updateScore(ArticleVisitStreamMess mess);

     /*
      * @Title: loadArticleBehavior
      * @Author: pyzxW
      * @Date: 2025-02-20 16:03:35
      * @Params:
      * @Return: null
      * @Description: 加载文章详情，进行数据的回显
      */
    public ResponseResult loadArticleBehavior(ArticleInfoDto dto);

    /**
     * 查询文章的评论列表
     * @param dto
     * @return
     */
    public PageResponseResult findNewsComments(ArticleCommentDto dto);
}
