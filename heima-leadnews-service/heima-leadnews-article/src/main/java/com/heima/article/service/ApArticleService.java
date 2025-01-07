package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;

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
}
