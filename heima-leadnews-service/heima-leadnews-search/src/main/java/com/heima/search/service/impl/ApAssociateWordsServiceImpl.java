package com.heima.search.service.impl;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.search.dtos.UserSearchDto;
import com.heima.search.pojos.ApAssociateWords;
import com.heima.search.service.ApAssociateWordsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.search.service.impl
 * @Author: yanhongwei
 * @CreateTime: 2025-02-17  15:07
 * @Description: TODO
 * @Version: 1.0
 */
@Slf4j
@Service
public class ApAssociateWordsServiceImpl implements ApAssociateWordsService {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 搜索联想词
     *
     * @param userSearchDto
     * @return
     */
    @Override
    public ResponseResult findAssociate(UserSearchDto userSearchDto) {
        //1.检查参数
        if(userSearchDto == null || StringUtils.isBlank(userSearchDto.getSearchWords())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //2.分页检查
        //超过20条，直接返回20条
        if (userSearchDto.getPageSize() > 20) {
            userSearchDto.setPageSize(20);
        }
        //3.执行查询 模糊查询
        //联想词之模糊查询
        Query query = Query.query(Criteria.where("associateWords").regex(".*?\\" + userSearchDto.getSearchWords() + ".*"));
        //设置限制条件
        query.limit(userSearchDto.getPageSize());
        //返回对应的结果
        List<ApAssociateWords> wordsList = mongoTemplate.find(query, ApAssociateWords.class);
        //返回正确的结果
        return ResponseResult.okResult(wordsList);
    }
}
