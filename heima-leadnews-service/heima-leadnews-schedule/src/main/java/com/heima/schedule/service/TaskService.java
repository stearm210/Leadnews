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

     /*
      * @Title: cancelTask
      * @Author: pyzxW
      * @Date: 2025-01-21 14:49:46
      * @Params:
      * @Return: null
      * @Description: 取消任务
      */
    public boolean cancelTask(long taskId);
}
