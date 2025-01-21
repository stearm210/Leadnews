package com.heima.schedule.test.service.impl;

import com.heima.model.schedule.dtos.Task;
import com.heima.schedule.ScheduleApplication;
import com.heima.schedule.service.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.schedule.test.service.impl
 * @Author: yanhongwei
 * @CreateTime: 2025-01-21  14:36
 * @Description: TODO
 * @Version: 1.0
 */

@SpringBootTest(classes = ScheduleApplication.class)
@RunWith(SpringRunner.class)
public class TaskServiceImplTest {
    @Autowired
    private TaskService taskService;

    @Test
    public void addTask(){
        Task task = new Task();
        task.setTaskType(100);
        task.setPriority(50);
        task.setParameters("task test".getBytes());
        task.setExecuteTime(new Date().getTime()+50000);

        long taskId = taskService.addTask(task);
        System.out.println(taskId);
    }

     /*
      * @Title: cancelTask
      * @Author: pyzxW
      * @Date: 2025-01-21 15:07:31
      * @Params:
      * @Return: null
      * @Description: 删除操作
      */
//    @Test
//    public void cancelTask(){
//        taskService.cancelTask(1881594079263883266L);
//    }

}
