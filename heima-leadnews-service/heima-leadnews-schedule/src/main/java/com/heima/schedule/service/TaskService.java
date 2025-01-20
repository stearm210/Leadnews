package com.heima.schedule.service;

import com.heima.model.schedule.dtos.Task;

public interface TaskService {
     /*
      * @Title: addTask
      * @Author: pyzxW
      * @Date: 2025-01-20 15:38:16
      * @Params:  
      * @Return: null
      * @Description: 添加延迟任务
      */
    public long addTask(Task task);
}
