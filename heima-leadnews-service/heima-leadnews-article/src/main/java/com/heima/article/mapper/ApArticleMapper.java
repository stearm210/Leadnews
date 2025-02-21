package com.heima.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.article.dtos.ArticleCommentDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.vos.ArticleCommnetVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApArticleMapper extends BaseMapper<ApArticle> {
     /*
      * @Title: loadArticleList
      * @Author: pyzxW
      * @Date: 2025-01-07 15:21:47
      * @Params:  type 1 加载更多 2 加载最新
      * @Return: null
      * @Description: 加载文章列表
      */
    public List<ApArticle> loadArticleList(ArticleHomeDto dto,Short type);

    List<ArticleCommnetVo> findNewsComments(@Param("dto") ArticleCommentDto dto);

    public int findNewsCommentsCount(@Param("dto")ArticleCommentDto dto);
}
