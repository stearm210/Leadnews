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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

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
      * @Title: refresh
      * @Author: pyzxW
      * @Date: 2025-01-21 15:52:43
      * @Params:
      * @Return: null
      * @Description: redis中的未来数据定时进行刷新
      */
    //每分钟执行一次
    @Scheduled(cron = "0 */1 * * * ?")
    public void refresh(){
        //实现加锁的操作
        String token = cacheService.tryLock("FUTRUE_TASK_SYNC", 1000 * 30);
        //如果成功的加锁
        if (StringUtils.isNoneBlank(token)){
            //打印执行指定任务的操作
            System.out.println(System.currentTimeMillis() / 1000 + "执行了定时任务");
            //1.获取所有未来数据集合的key值
            Set<String> futureKeys = cacheService.scan(ScheduleConstants.FUTURE + "*");
            for (String futureKey : futureKeys){
                //得到对应的topicKey,也就是名字。这里进行了名字的分割，方便操作
                String topicKey = ScheduleConstants.TOPIC + futureKey.split(ScheduleConstants.FUTURE)[1];
                //2.按照key和分值查询符合条件的数据
                //获取该组key下当前需要消费的任务数据
                Set<String> tasks = cacheService.zRangeByScore(futureKey, 0, System.currentTimeMillis());
                //如果你添加到了当前的list中，则需要在未来的zset中删除对应的数据
                if (!tasks.isEmpty()) {
                    //将这些任务数据添加到消费者队列中，并且在zset中删除对应的数据
                    //refreshWithPipeline是对此封装的一个函数
                    cacheService.refreshWithPipeline(futureKey, topicKey, tasks);
                    System.out.println("成功的将" + futureKey + "下的当前需要执行的任务数据刷新到" + topicKey + "下");
                }
            }
        }
    }

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
        return task.getTaskId();
    }

     /*
      * @Title: cancelTask
      * @Author: pyzxW
      * @Date: 2025-01-21 14:50:03
      * @Params:
      * @Return: null
      * @Description: 取消任务之操作
      */
    @Override
    public boolean cancelTask(long taskId) {
        boolean flag = false;
        //1.删除任务，更新任务日志
        Task task = updateDb(taskId,ScheduleConstants.CANCELLED);
        //2.删除redis中的数据
        if (task != null){
            //删除任务抽出一个方法
            removeTaskFromCache(task);
            flag = true;
        }
        return flag;
    }

     /*
      * @Title: pull
      * @Author: pyzxW
      * @Date: 2025-01-21 15:26:10
      * @Params:
      * @Return: null
      * @Description: 按照类型和优先级拉取任务
      */
    @Override
    public Task pull(int type, int priority) {
        Task task = null;
        try {
            String key = type+"_"+priority;
            //拉取任务
            String task_json = cacheService.lRightPop(ScheduleConstants.TOPIC + key);
            if(StringUtils.isNotBlank(task_json)){
                task = JSON.parseObject(task_json, Task.class);
                //更新数据库信息
                updateDb(task.getTaskId(),ScheduleConstants.EXECUTED);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("poll task exception");
        }
        return task;
    }

    /*
      * @Title: removeTaskFromCache
      * @Author: pyzxW
      * @Date: 2025-01-21 15:01:21
      * @Params:
      * @Return: null
      * @Description: 删除redis中的数据
      */
    private void removeTaskFromCache(Task task) {
        //key是一个标签，需要一个参数属性
        String key = task.getTaskType() + "_" + task.getPriority();
        if(task.getExecuteTime() <= System.currentTimeMillis()){
            //删除当前的数据
            cacheService.lRemove(ScheduleConstants.TOPIC+key,0,JSON.toJSONString(task));
        }else {
            //删除未来的数据
            cacheService.zRemove(ScheduleConstants.FUTURE+key, JSON.toJSONString(task));
        }
    }

    /*
      * @Title: updateDb
      * @Author: pyzxW
      * @Date: 2025-01-21 14:52:55
      * @Params:
      * @Return: null
      * @Description: 删除任务，更新任务日志
      */
    private Task updateDb(long taskId, int status) {
        Task task = null;
        try {
            //删除任务
            taskinfoMapper.deleteById(taskId);
            //更新任务日志，获取对应的任务日志信息
            TaskinfoLogs taskinfoLogs = taskinfoLogsMapper.selectById(taskId);
            taskinfoLogs.setStatus(status);
            //更新数据库
            taskinfoLogsMapper.updateById(taskinfoLogs);

            task = new Task();
            //将对应的信息传递到task中
            BeanUtils.copyProperties(taskinfoLogs,task);
            task.setExecuteTime(taskinfoLogs.getExecuteTime().getTime());
        }catch (Exception e){
            log.error("task cancel exception taskid={}",taskId);
        }
        return task;
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
        //key是一个标签
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

            //设置任务id
            task.setTaskId(taskinfo.getTaskId());

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
