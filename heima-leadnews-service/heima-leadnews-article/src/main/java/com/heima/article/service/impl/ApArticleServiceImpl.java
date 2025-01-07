package com.heima.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ApArticleService;
import com.heima.common.constants.ArticleConstants;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.article.service.impl
 * @Author: yanhongwei
 * @CreateTime: 2025-01-07  15:32
 * @Description: TODO
 * @Version: 1.0
 */

@Service
@Transactional
@Slf4j
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements ApArticleService {

    @Autowired
    private ApArticleMapper apArticleMapper;

    // 单页最大加载的数字
    private final static short MAX_PAGE_SIZE = 50;
     /*
      * @Title: load
      * @Author: pyzxW
      * @Date: 2025-01-07 15:43:05
      * @Params:  type 1 为加载更多  2 为加载最新
      * @Return: null
      * @Description:
      */

    /*
    * 函数接收的参数是持久层传回的list集合，需要进行转化，变为controller需要传回的ResponseResult
    * */
    @Override
    public ResponseResult load(ArticleHomeDto dto, Short type) {
        //1.校验参数
        //分页条数的校验
        Integer size = dto.getSize();
        if(size == null || size == 0){
            size = 10;
        }
        //分页的值不超过50
        size = Math.min(size, MAX_PAGE_SIZE);
        //校验参数 -->type
        if (!type.equals(ArticleConstants.LOADTYPE_LOAD_MORE) && !type.equals(ArticleConstants.LOADTYPE_LOAD_NEW)){
            type = ArticleConstants.LOADTYPE_LOAD_MORE;
        }

        //频道参数校验

        //时间校验

        List<ApArticle> articleList = apArticleMapper.loadArticleList(dto, type);

        //返回最终的结果，封装于ResponseResult
        return ResponseResult.okResult(articleList);
    }
}
