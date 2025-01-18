package com.heima.article.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleConfigMapper;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ApArticleService;
import com.heima.common.constants.ArticleConstants;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleConfig;
import com.heima.model.article.pojos.ApArticleContent;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
//    传入的short type为对应的时间，注意
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
        if(StringUtils.isEmpty(dto.getTag())){
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }
        //时间校验
        //最大时间
        if(dto.getMaxBehotTime() == null){
            dto.setMaxBehotTime(new Date());
        }
        //最小时间
        if(dto.getMinBehotTime() == null) dto.setMinBehotTime(new Date());

        //2.查询数据
        List<ApArticle> articleList = apArticleMapper.loadArticleList(dto, type);

        //3.结果返回
        //返回最终的结果，封装于ResponseResult
        return ResponseResult.okResult(articleList);
    }

     /*
      * @Title: saveArticle
      * @Author: pyzxW
      * @Date: 2025-01-15 15:29:25
      * @Params:
      * @Return: null
      * @Description: 保存app端相关文章
      */

    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;

    @Autowired
    private ApArticleContentMapper apArticleContentMapper;

    @Override
    public ResponseResult saveArticle(ArticleDto dto) {
        //用于测试操作
        //当线程睡眠为3秒时，则会触发对应的服务降级策略
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //1.检查参数
        //当不存在文章时
        if(dto == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApArticle apArticle = new ApArticle();
        //拷贝对应的属性
        BeanUtils.copyProperties(dto,apArticle);

        //2.判断是否存在id
        if (dto.getId() == null){
            //2.1 不存在id  保存文章  保存文章配置  保存文章内容

            //保存文章
            save(apArticle);
            //保存配置
            ApArticleConfig apArticleConfig = new ApArticleConfig(apArticle.getId());
            apArticleConfigMapper.insert(apArticleConfig);
            //保存文章内容
            ApArticleContent apArticleContent = new ApArticleContent();
            apArticleContent.setArticleId(apArticle.getId());
            apArticleContent.setContent(dto.getContent());
            apArticleContentMapper.insert(apArticleContent);

        }else {
            //2.2 存在id   修改文章  修改文章内容
            //修改  文章
            updateById(apArticle);

            //修改文章内容
            //获取文章内容
            ApArticleContent apArticleContent = apArticleContentMapper.selectOne(Wrappers.<ApArticleContent>lambdaQuery().eq(ApArticleContent::getArticleId, dto.getId()));
            //进行修改
            apArticleContent.setContent(dto.getContent());
            //更新
            apArticleContentMapper.updateById(apArticleContent);
        }

        //3.结果返回  文章的id
        return ResponseResult.okResult(apArticle.getId());
    }
}
