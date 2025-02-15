package com.heima.search.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.UserSearchDto;
import org.springframework.web.bind.annotation.RequestBody;

public interface ArticleSearchService {
     /*
      * @Title: search
      * @Author: pyzxW
      * @Date: 2025-02-15 21:07:50
      * @Params:
      * @Return: null
      * @Description: es文章分页检索
      */
    public ResponseResult search(UserSearchDto dto);
}
