package com.heima.search.service;


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
}
