package com.heima.model.article.dtos;
 
import com.heima.model.common.dtos.PageRequestDto;
import lombok.Data;

 /*
  * @Title:
  * @Author: pyzxW
  * @Date: 2025-02-21 16:03:09
  * @Params:
  * @Return: null
  * @Description: 放置用户评论的参数，包括日期用户id等
  */
@Data
public class ArticleCommentDto extends PageRequestDto {
    private String beginDate;
    private String endDate;
    private Integer wmUserId;
}