package com.heima.schedule.service.Impl;

import com.alibaba.fastjson.JSON;
import com.heima.common.constants.ScheduleConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.schedule.dtos.Task;
import com.heima.model.schedule.pojos.Taskinfo;
import com.heima.model.schedule.pojos.TaskinfoLogs;
import com.heima.schedule.mapper.TaskinfoLogsMapper;
import com.heima.schedule.mapper.TaskinfoMapper;
import com.heima.schedule.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

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
        //1.添加任务到数据库中,抽出一个方法函数
        boolean success = addTaskToDb(task);
        if(success){
            //2.添加任务到redis中
            addTaskToCache(task);

        }

        return 0;
    }

    @Autowired
    private CacheService cacheService;
     /*
      * @Title: addTaskToCache
      * @Author: pyzxW
      * @Date: 2025-01-21 14:15:58
      * @Params:
      * @Return: null
      * @Description: 添加任务至redis中
      */
    private void addTaskToCache(Task task) {
        String key = task.getTaskType() + "_" + task.getPriority();
        //获取5分钟之后的时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE,5);
        long nextSchedule = calendar.getTimeInMillis();

        //2.1 如果任务的执行时间小于等于当前时间，存入list
        //这表示立即执行此任务
        if (task.getExecuteTime() <= System.currentTimeMillis()){
            //task转换一下类型方便存入list集合中
            cacheService.lLeftPush(ScheduleConstants.TOPIC + key, JSON.toJSONString(task));
        }else if (task.getExecuteTime() <= nextSchedule){
            //2.2 如果任务的执行时间大于当前时间，同时小于预设时间(未来5分钟)，存入zset
            //这表示是延迟任务
            cacheService.zAdd(ScheduleConstants.FUTURE + key, JSON.toJSONString(task), task.getExecuteTime());
        }

    }

    @Autowired
    private TaskinfoMapper taskinfoMapper;

    @Autowired
    private TaskinfoLogsMapper taskinfoLogsMapper;
     /*
      * @Title: addTaskToDb
      * @Author: pyzxW
      * @Date: 2025-01-20 15:46:36
      * @Params:
      * @Return: null
      * @Description: 添加任务到数据库中,抽出一个方法函数
      */
    private boolean addTaskToDb(Task task){

        boolean flag = false;
        try{
            //保存任务表
            Taskinfo taskinfo = new Taskinfo();
            //数据库参数批量导入，从task表中导入到taskinfo表中
            BeanUtils.copyProperties(task,taskinfo);
            //更变执行时间类型，方便时间从task表中导入到taskinfo表中
            taskinfo.setExecuteTime(new Date(task.getExecuteTime()));
            taskinfoMapper.insert(taskinfo);

            //保存任务日志数据
            TaskinfoLogs taskinfoLogs = new TaskinfoLogs();
            //从taskinfo表中导入数据到taskinfoLogs表中
            BeanUtils.copyProperties(taskinfo,taskinfoLogs);
            //设置乐观锁的版本号
            taskinfoLogs.setVersion(1);
            //设置初始化状态
            taskinfoLogs.setStatus(ScheduleConstants.SCHEDULED);
            taskinfoLogsMapper.insert(taskinfoLogs);
            //执行成功之后再次设置为true
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
}
