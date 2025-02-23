package com.heima.article.service.impl;

import com.heima.article.ArticleApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ArticleApplication.class)
@RunWith(SpringRunner.class)
class HotArticleServiceImplTest {

    @Autowired
    HotArticleServiceImpl hotArticleService;
     /*
      * @Title: computeHotArticle
      * @Author: pyzxW
      * @Date: 2025-02-23 13:03:57
      * @Params:  
      * @Return: null
      * @Description: 测试类
      */
    @Test
    void computeHotArticle() {
        hotArticleService.computeHotArticle();
    }
}