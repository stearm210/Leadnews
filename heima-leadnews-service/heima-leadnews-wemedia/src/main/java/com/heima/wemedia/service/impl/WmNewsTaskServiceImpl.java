package com.heima.wemedia.service.impl;

import com.heima.apis.schedule.IScheduleClient;
import com.heima.model.common.enums.TaskTypeEnum;
import com.heima.model.schedule.dtos.Task;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.utils.common.ProtostuffUtil;
import com.heima.wemedia.service.WmNewsTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.wemedia.service.impl
 * @Author: yanhongwei
 * @CreateTime: 2025-01-22  15:33
 * @Description: TODO
 * @Version: 1.0
 */

@Service
@Slf4j
public class WmNewsTaskServiceImpl implements WmNewsTaskService {
    @Autowired
    private IScheduleClient scheduleClient;
     /*
      * @Title: addNewsToTask
      * @Author: pyzxW
      * @Date: 2025-01-22 15:34:03
      * @Params:
      * @Return: null
      * @Description: 添加任务到延迟队列中
      */
    @Override
    public void addNewsToTask(Integer id, Date publishTime) {
        log.info("添加任务到延迟服务中----begin");
        Task task = new Task();
        //任务的执行时间
        task.setExecuteTime(publishTime.getTime());
        //任务类型以及优先级添加
        task.setTaskType(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType());
        task.setPriority(TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
        //传递对应的参数
        WmNews wmNews = new WmNews();
        wmNews.setId(id);
        //执行对象序列化
        task.setParameters(ProtostuffUtil.serialize(wmNews));
        scheduleClient.addTask(task);
        log.info("添加任务到延迟服务中----end");
    }
}
