package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.article.pojos.ApArticleConfig;
import com.heima.model.comment.dtos.CommentConfigDto;
import com.heima.model.common.dtos.ResponseResult;

import java.util.Map;

public interface ApArticleConfigService extends IService<ApArticleConfig> {

     /*
      * @Title: updateByMap
      * @Author: pyzxW
      * @Date: 2025-02-14 19:33:09
      * @Params:  
      * @Return: null
      * @Description: 修改文章配置
      */
    void updateByMap(Map map);

    /**
     * 更新文章的评论设置->打开或关闭评论
     * @param dto
     * @return
     */
    ResponseResult updateCommentStatus(CommentConfigDto dto);
}
