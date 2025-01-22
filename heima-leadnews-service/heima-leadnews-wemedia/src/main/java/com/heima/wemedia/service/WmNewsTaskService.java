package com.heima.wemedia.service;

import java.util.Date;

public interface WmNewsTaskService {
    /*
     * @Title: addNewsToTask
     * @Author: pyzxW
     * @Date: 2025-01-22 15:32:37
     * @Params:
     * @Return: null
     * @Description: 添加任务到延迟队列中
     */
    public void addNewsToTask(Integer id, Date publishTime);
}
