package com.heima.model.mess;
 
import lombok.Data;

 /*
  * @Title:
  * @Author: pyzxW
  * @Date: 2025-02-25 16:05:28
  * @Params:
  * @Return: null
  * @Description: 主用于kafka文章流式计算操作
  */
@Data
public class UpdateArticleMess {
 
    /**
     * 修改文章的字段类型
      */
    private UpdateArticleType type;
    /**
     * 文章ID
     */
    private Long articleId;
    /**
     * 修改数据的增量，可为正负
     */
    private Integer add;

    //枚举类
    public enum UpdateArticleType{
        COLLECTION,COMMENT,LIKES,VIEWS;
    }
}