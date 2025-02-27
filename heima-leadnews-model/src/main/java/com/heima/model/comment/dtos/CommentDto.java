package com.heima.model.comment.dtos;
 
import lombok.Data;
 
import java.util.Date;

 /*
  * @Title:
  * @Author: pyzxW
  * @Date: 2025-02-21 16:22:32
  * @Params:
  * @Return: null
  * @Description: 评论参数
  */
@Data
public class CommentDto {
 
    /**
     * 文章id
     */
    private Long articleId;
 
    // 最小时间
    private Date minDate;
 
    //是否是首页
    private Short index;
 
}