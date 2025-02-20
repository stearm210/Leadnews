package com.heima.model.article.dtos;

import lombok.Data;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.model.article.dtos
 * @Author: yanhongwei
 * @CreateTime: 2025-02-20  16:00
 * @Description: TODO
 * @Version: 1.0
 */
 /*
  * @Title:
  * @Author: pyzxW
  * @Date: 2025-02-20 16:01:23
  * @Params:
  * @Return: null
  * @Description: 文章之数据回显操作
  */
@Data
public class ArticleInfoDto {
    //文章id
    private long articleId;
    //作者id
    private int authorId;
}
