package com.heima.search.service;


import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.HistorySearchDto;

public interface ApUserSearchService {
     /*
      * @Title: insert
      * @Author: pyzxW
      * @Date: 2025-02-16 19:57:48
      * @Params:
      * @Return: null
      * @Description: 保存用户搜索的历史记录
      */
    public void insert(String keyword, Integer userId);

    /**
     查询搜索历史
     @return
     */
    public ResponseResult findUserSearch();

    /**
     删除搜索历史
     @param historySearchDto
     @return
     */
    public ResponseResult delUserSearch(HistorySearchDto historySearchDto);
}
