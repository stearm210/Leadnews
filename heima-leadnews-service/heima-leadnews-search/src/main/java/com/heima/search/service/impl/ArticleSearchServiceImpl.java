package com.heima.search.service.impl;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.search.dtos.UserSearchDto;
import com.heima.search.service.ArticleSearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.search.service.impl
 * @Author: yanhongwei
 * @CreateTime: 2025-02-15  21:09
 * @Description: TODO
 * @Version: 1.0
 */

@Slf4j
@Service
public class ArticleSearchServiceImpl implements ArticleSearchService {
    //查询条件？高亮操作？
    @Autowired
    private RestHighLevelClient restHighLevelClient;
     /*
      * @Title: search
      * @Author: pyzxW
      * @Date: 2025-02-15 21:09:44
      * @Params:
      * @Return: null
      * @Description: 搜索接口定义
      */
    @Override
    public ResponseResult search(UserSearchDto dto) {
        //1.检查参数
        if (dto == null || StringUtils.isBlank(dto.getSearchWords())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.设置查询条件
        //关键字的分词之后进行查询
        SearchRequest searchRequest = new SearchRequest("app_info_article");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //查询小于mindate的数据

        //分页查询

        //按照发布时间倒序查询

        //设置高亮 title

        //3.结果封装返回
        return null;
    }
}
