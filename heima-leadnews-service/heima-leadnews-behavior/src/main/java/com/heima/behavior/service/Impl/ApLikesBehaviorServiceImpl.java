package com.heima.behavior.service.Impl;

import com.alibaba.fastjson.JSON;
import com.heima.behavior.service.ApLikesBehaviorService;
import com.heima.common.constants.BehaviorConstants;
import com.heima.common.constants.HotArticleConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.behavior.dtos.LikesBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.mess.UpdateArticleMess;
import com.heima.model.user.pojos.ApUser;
import com.heima.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.behavior.service.Impl
 * @Author: yanhongwei
 * @CreateTime: 2025-02-19  16:10
 * @Description: TODO
 * @Version: 1.0
 */
@Service
@Transactional
@Slf4j
public class ApLikesBehaviorServiceImpl implements ApLikesBehaviorService {
    @Autowired
    private CacheService cacheService;

    //kafka操作，用于方便发送kafka消息
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    /**
     * 点赞
     *
     * @param dto 0:点赞 1:取消点赞
     * @return
     */
    @Override
    public ResponseResult like(LikesBehaviorDto dto) {
        // 1. 检查参数
        if(dto == null || dto.getArticleId() == null || checkParam(dto)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        // 2. 是否登录
        ApUser apUser = AppThreadLocalUtil.getUser();
        if(apUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        //修改文章的信息对象
        UpdateArticleMess mess = new UpdateArticleMess();
        //设置文章id
        mess.setArticleId(dto.getArticleId());
        //设置文章的类型
        mess.setType(UpdateArticleMess.UpdateArticleType.LIKES);

        //3.点赞，保存对应数据
        if (dto.getOperation() == 0){
            //hGet是用于获取存储在哈希表中指定字段的值
            //使用Object类型是因为它在Java中是所有类的超类，可以容纳任何类型的值。
            Object obj = cacheService.hGet(BehaviorConstants.LIKE_BEHAVIOR + dto.getArticleId().toString(), apUser.getId().toString());
            //操作不为空时，返回已点赞提示
            if(obj != null) {
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "已点赞");
            }
            // 保存当前key
            log.info("保存当前key：{}, {}, {}", dto.getArticleId(), apUser.getId(), dto);
            //保存操作
            cacheService.hPut(BehaviorConstants.LIKE_BEHAVIOR + dto.getArticleId().toString(), apUser.getId().toString(), JSON.toJSONString(dto));
            //点赞操作，加上一个正一
            mess.setAdd(1);
        }else {
            //取消点赞，删除当前的key
            log.info("删除当前key:{}, {}", dto.getArticleId(), apUser.getId());
            //删除当前的key
            cacheService.hDelete(BehaviorConstants.LIKE_BEHAVIOR + dto.getArticleId().toString(), apUser.getId().toString());
            mess.setAdd(-1);
        }

        //4.对kafka发送对应的消息
        //常量类消息为定位的路径
        kafkaTemplate.send(HotArticleConstants.HOT_ARTICLE_SCORE_TOPIC,JSON.toJSONString(mess));

        //5.结果返回
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

     /*
      * @Title: checkParam
      * @Author: pyzxW
      * @Date: 2025-02-19 16:12
      * :59
      * @Params:
      * @Return: null
      * @Description: 检查参数操作
      */
    private boolean checkParam(LikesBehaviorDto dto) {
        //参数有误
        if(dto.getType() > 2 || dto.getType() < 0 || dto.getOperation() > 1 || dto.getOperation() < 0) {
            return true;
        }
        return false;
    }
}
