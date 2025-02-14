package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.article.pojos.ApArticleConfig;

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
}
