package com.heima.search.service.impl;

import com.heima.search.pojos.ApUserSearch;
import com.heima.search.service.ApUserSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.search.service.impl
 * @Author: yanhongwei
 * @CreateTime: 2025-02-16  19:59
 * @Description: TODO
 * @Version: 1.0
 */
@Service
@Slf4j
public class ApUserSearchServiceImpl implements ApUserSearchService {
    @Autowired
    private MongoTemplate mongoTemplate;

     /*
      * @Title: insert
      * @Author: pyzxW
      * @Date: 2025-02-16 19:59:30
      * @Params:
      * @Return: null
      * @Description: Convert to Basic Latin
      */
    @Override
    @Async
    public void insert(String keyword, Integer userId) {
        //1.查询当前用户的搜索关键词
        //根据userid以及keyword进行查询
        Query query = Query.query(Criteria.where("userId").is(userId).and("keyword").is(keyword));
        ApUserSearch apUserSearch = mongoTemplate.findOne(query, ApUserSearch.class);
        //2.存在关键字(之前查找过这个关键字) 更新创建时间
        if(apUserSearch != null){
            //更新创建时间
            apUserSearch.setCreatedTime(new Date());
            mongoTemplate.save(apUserSearch);
            return;
        }

        //3.不存在，判断当前历史记录总数量是否超过10
        apUserSearch = new ApUserSearch();
        apUserSearch.setUserId(userId);
        apUserSearch.setKeyword(keyword);
        apUserSearch.setCreatedTime(new Date());
        //根据userid进行查询
        Query query1 = Query.query(Criteria.where("userId").is(userId));
        //根据创建时间进行倒序排序
        query1.with(Sort.by(Sort.Direction.DESC,"createdTime"));
        List<ApUserSearch> apUserSearchList = mongoTemplate.find(query1, ApUserSearch.class);

        if(apUserSearchList == null || apUserSearchList.size() < 10){
            //小于10的情况下
            mongoTemplate.save(apUserSearch);
        }else {
            ApUserSearch lastUserSearch = apUserSearchList.get(apUserSearchList.size() - 1);
            //替换操作，将会替换时间最久的那个
            mongoTemplate.findAndReplace(Query.query(Criteria.where("id").is(lastUserSearch.getId())),apUserSearch);
        }
    }
}
