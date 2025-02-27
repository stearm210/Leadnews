package com.heima.model.article.dtos;

import com.heima.model.common.annotation.IdEncrypt;
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
    // 设备ID
    @IdEncrypt
    private Integer equipmentId;
    // 文章ID
    @IdEncrypt
    private Long articleId;
    // 作者ID
    @IdEncrypt
    private Integer authorId;
}
