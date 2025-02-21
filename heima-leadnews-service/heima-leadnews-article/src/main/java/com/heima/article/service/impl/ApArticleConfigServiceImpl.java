package com.heima.article.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleConfigMapper;
import com.heima.article.service.ApArticleConfigService;
import com.heima.model.article.pojos.ApArticleConfig;
import com.heima.model.comment.dtos.CommentConfigDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.article.service.impl
 * @Author: yanhongwei
 * @CreateTime: 2025-02-14  19:30
 * @Description: TODO
 * @Version: 1.0
 */
@Service
@Slf4j
@Transactional
public class ApArticleConfigServiceImpl extends ServiceImpl<ApArticleConfigMapper, ApArticleConfig> implements ApArticleConfigService {

     /*
      * @Title: updateByMap
      * @Author: pyzxW
      * @Date: 2025-02-14 19:33:23
      * @Params:
      * @Return: null
      * @Description: 修改文章配置
      */
    @Override
    public void updateByMap(Map map) {
        //0 下架 1 上架
        Object enable = map.get("enable");
        boolean isDown = true;
        //等于1为上架
        if(enable.equals(1)){
            isDown = false;
        }
        //修改数据库中对应的数据
        update(Wrappers.<ApArticleConfig>lambdaUpdate().eq(ApArticleConfig::getArticleId,map.get("articleId")).
                set(ApArticleConfig::getIsDown,isDown));
    }

    /**
     * 更新文章的评论设置->打开或关闭评论
     * @param dto
     * @return
     */
    @Override
    public ResponseResult updateCommentStatus(CommentConfigDto dto) {
        update(Wrappers.<ApArticleConfig>lambdaUpdate()
                .eq(ApArticleConfig::getArticleId, dto.getArticleId())
                .set(ApArticleConfig::getIsComment, dto.getOperation()));

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
