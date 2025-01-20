package com.heima.schedule.service.Impl;

import com.heima.model.schedule.dtos.Task;
import com.heima.schedule.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.schedule.service.Impl
 * @Author: yanhongwei
 * @CreateTime: 2025-01-20  15:38
 * @Description: TODO
 * @Version: 1.0
 */
@Service
@Transactional
@Slf4j
public class TaskServiceImpl implements TaskService {
     /*
      * @Title: addTask
      * @Author: pyzxW
      * @Date: 2025-01-20 15:39:20
      * @Params:
      * @Return: null
      * @Description: 添加延迟任务
      */
    @Override
    public long addTask(Task task) {

        return 0;
    }
}
