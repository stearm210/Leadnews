package com.heima.model.article.dtos;

import com.heima.model.article.pojos.ApArticle;
import lombok.Data;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.model.article.dtos
 * @Author: yanhongwei
 * @CreateTime: 2025-01-14  15:51
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class ArticleDto extends ApArticle {
    //文章内容需要被传输
    private String content;
}
