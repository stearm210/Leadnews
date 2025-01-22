package com.heima.apis.schedule;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.schedule.dtos.Task;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("leadnews-schedule")
public interface IScheduleClient {
    /*
     * @Title: addTask
     * @Author: pyzxW
     * @Date: 2025-01-20 15:38:16
     * @Params:
     * @Return: null
     * @Description: 添加延迟任务
     */
    @PostMapping("/api/v1/task/add")
    public ResponseResult addTask(@RequestBody Task task);

    /*
     * @Title: cancelTask
     * @Author: pyzxW
     * @Date: 2025-01-21 14:49:46
     * @Params:
     * @Return: null
     * @Description: 取消任务
     */
    @GetMapping("/api/v1/task/cancel/{taskId}")
    public ResponseResult cancelTask(@PathVariable("taskId") long taskId);

    /*
     * @Title: pull
     * @Author: pyzxW
     * @Date: 2025-01-21 15:09:45
     * @Params:
     * @Return: null
     * @Description: 按照类型和优先级拉取任务
     */
    @GetMapping("/api/v1/task/poll/{type}/{priority}")
    public ResponseResult pull(@PathVariable("type") int type,@PathVariable("priority")  int priority);
}
