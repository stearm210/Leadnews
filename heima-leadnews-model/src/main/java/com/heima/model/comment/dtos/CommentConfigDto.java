package com.heima.model.comment.dtos;
 
import lombok.Data;

 /*
  * @Title:
  * @Author: pyzxW
  * @Date: 2025-02-21 16:22:10
  * @Params:
  * @Return: null
  * @Description: 评论配置参数
  */
@Data
public class CommentConfigDto {
 
    /**
     * 文章id
     */
    private Long articleId;
    /**
     * 操作
     * 0  关闭评论
     * 1  开启评论
     */
    private Short operation;
}