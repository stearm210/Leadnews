package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.pojos.WmSensitive;
import com.heima.utils.common.SensitiveWordUtil;
import com.heima.wemedia.mapper.WmSensitiveMapper;
import com.heima.wemedia.service.WmSensitiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.wemedia.service.impl
 * @Author: yanhongwei
 * @CreateTime: 2025-02-21  16:47
 * @Description: TODO
 * @Version: 1.0
 */
@Slf4j
@Service
@Transactional
public class WmSensitiveServiceImpl implements WmSensitiveService {
    @Autowired
    private WmSensitiveMapper wmSensitiveMapper;

    @Override
    public ResponseResult checkSensitive(String content) {
        // 获取所有的敏感词
        List<WmSensitive> wmSensitives = wmSensitiveMapper.selectList(Wrappers.<WmSensitive>lambdaQuery().select(WmSensitive::getSensitives));
        List<String> sensitiveList = wmSensitives.stream().map(WmSensitive::getSensitives).collect(Collectors.toList());

        // 初始化敏感词库
        SensitiveWordUtil.initMap(sensitiveList);

        // 查看评论中是否包含敏感词
        Map<String, Integer> map = SensitiveWordUtil.matchWords(content);
        if(map.size() > 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "当前评论中包含敏感词: " + map);
        }

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
