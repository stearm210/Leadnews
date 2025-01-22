package com.heima.schedule.feign;

import com.heima.apis.schedule.IScheduleClient;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.schedule.dtos.Task;
import com.heima.schedule.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.schedule.feign
 * @Author: yanhongwei
 * @CreateTime: 2025-01-22  15:18
 * @Description: TODO
 * @Version: 1.0
 */

@RestController
public class ScheduleClient implements IScheduleClient {
    @Autowired
    private TaskService taskService;

     /*
      * @Title: addTask
      * @Author: pyzxW
      * @Date: 2025-01-22 15:20:01
      * @Params:
      * @Return: null
      * @Description: 添加任务
      */
    @PostMapping("/api/v1/task/add")
    @Override
    public ResponseResult addTask(@RequestBody Task task) {
        return ResponseResult.okResult(taskService.addTask(task));
    }

     /*
      * @Title: cancelTask
      * @Author: pyzxW
      * @Date: 2025-01-22 15:20:35
      * @Params:
      * @Return: null
      * @Description: 取消任务
      */
    @GetMapping("/api/v1/task/cancel/{taskId}")
    @Override
    public ResponseResult cancelTask(@PathVariable("taskId") long taskId) {
        return ResponseResult.okResult(taskService.cancelTask(taskId));
    }


    /*
      * @Title: pull
      * @Author: pyzxW
      * @Date: 2025-01-22 15:20:55
      * @Params:
      * @Return: null
      * @Description: 按照类型和优先级拉取任务
      */
    //到底是使用poll还是pull争议还是挺大的，不知道前端是怎么配置的
    @GetMapping("/api/v1/task/pull/{type}/{priority}")
    @Override
    public ResponseResult pull(@PathVariable("type") int type, @PathVariable("priority") int priority) {
        return ResponseResult.okResult(taskService.pull(type,priority));
    }
}
