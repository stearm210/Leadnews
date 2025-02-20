package com.heima.behavior.service.Impl;

import com.alibaba.fastjson.JSON;
import com.heima.behavior.service.ApReadBehaviorService;
import com.heima.common.constants.BehaviorConstants;
import com.heima.common.constants.HotArticleConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.behavior.dtos.ReadBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.mess.UpdateArticleMess;
import com.heima.model.user.pojos.ApUser;
import com.heima.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.behavior.service.Impl
 * @Author: yanhongwei
 * @CreateTime: 2025-02-20  14:15
 * @Description: TODO
 * @Version: 1.0
 */
@Slf4j
@Service
@Transactional
public class ApReadBehaviorServiceImpl implements ApReadBehaviorService {
    @Autowired
    private CacheService cacheService;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
     /*
      * @Title: readBehavior
      * @Author: pyzxW
      * @Date: 2025-02-20 14:15:30
      * @Params:
      * @Return: null
      * @Description: 用户阅读操作
      */
    @Override
    public ResponseResult readBehavior(ReadBehaviorDto dto) {
        // 1. 检查参数
        if(dto == null || dto.getArticleId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        // 2. 是否登录
        ApUser user = AppThreadLocalUtil.getUser();
        if(user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        //3.更新阅读的频次
        //读取缓存中的数据
        //缓存中的数据格式为Object,需要将其转换为String获得.后续要将其解析为 JSON 格式。
        String readBehaviorJson = (String) cacheService.hGet(BehaviorConstants.READ_BEHAVIOR + dto.getArticleId().toString(), user.getId().toString());
        if(StringUtils.isNotBlank(readBehaviorJson)) {
            //JSON字符串对象转换为dto类型方便后面进行类型的查找与操作
            //解析过程中，JSON 库会根据 JSON 字段名和 ReadBehaviorDto 类的字段名进行匹配和赋值。
            ReadBehaviorDto readBehaviorDto = JSON.parseObject(readBehaviorJson, ReadBehaviorDto.class);
            //获取从缓存中读取的阅读行为对象的阅读次数 + 获取当前 DTO 中的阅读次数。
            dto.setCount((short) (readBehaviorDto.getCount() + dto.getCount()));
        }

        //保存当前的key
        log.info("保存当前key: {} {} {}", dto.getArticleId(), user.getId(), dto);
        //哈希表操作是一种通用的数据操作方式，适用于多种数据格式和场景，这里用于 JSON 格式。
        cacheService.hPut(BehaviorConstants.READ_BEHAVIOR + dto.getArticleId().toString(), user.getId().toString(), JSON.toJSONString(dto));

        // 5. 发送消息，数据聚合
        UpdateArticleMess mess = new UpdateArticleMess();
        mess.setArticleId(dto.getArticleId());
        mess.setType(UpdateArticleMess.UpdateArticleType.VIEWS);
        mess.setAdd(1);
        kafkaTemplate.send(HotArticleConstants.HOT_ARTICLE_SCORE_TOPIC, JSON.toJSONString(mess));

        // 6. 结果返回
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
