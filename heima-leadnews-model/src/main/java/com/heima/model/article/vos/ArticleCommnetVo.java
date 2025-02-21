package com.heima.model.article.vos;
 
import lombok.Data;
 
import java.util.Date;

 /*
  * @Title:
  * @Author: pyzxW
  * @Date: 2025-02-21 16:04:33
  * @Params:
  * @Return: null
  * @Description: 这个主要是文章评论的回显参数
  */
@Data
public class ArticleCommnetVo {
 
    private Long id;
 
    private String title;
 
    private Integer comments;

    //是否评论
    private Boolean isComment;
 
    private Date publishTime;
}