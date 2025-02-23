package com.heima.article.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleConfigMapper;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ApArticleService;
import com.heima.article.service.ArticleFreemarkerService;
import com.heima.common.constants.ArticleConstants;
import com.heima.common.constants.BehaviorConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.article.dtos.ArticleCommentDto;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.dtos.ArticleInfoDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleConfig;
import com.heima.model.article.pojos.ApArticleContent;
import com.heima.model.article.vos.ArticleCommnetVo;
import com.heima.model.article.vos.HotArticleVo;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.mess.ArticleVisitStreamMess;
import com.heima.model.user.pojos.ApUser;
import com.heima.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    private CacheService cacheService;

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

    /**
     * 加载文章列表
     *
     * @param dto
     * @param type      1 加载更多   2 加载最新
     * @param firstPage true  是首页  flase 非首页
     * @return
     */
    @Override
    public ResponseResult load2(ArticleHomeDto dto, Short type, boolean firstPage) {
        if (firstPage){
            //获取对应的热点数据
            String jsonStr = cacheService.get(ArticleConstants.HOT_ARTICLE_FIRST_PAGE + dto.getTag());
            //如果不为空
            if (StringUtils.isNotBlank(jsonStr)){
                List<HotArticleVo> hotArticleVoList = JSON.parseArray(jsonStr, HotArticleVo.class);
                return ResponseResult.okResult(hotArticleVoList);
            }
        }
        //非首页时
        return load(dto, type);
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

    @Autowired
    private ArticleFreemarkerService articleFreemarkerService;

    @Override
    public ResponseResult saveArticle(ArticleDto dto) {
        //用于测试操作
        //当线程睡眠为3秒时，则会触发对应的服务降级策略
//        try {
//            Thread.sleep(1000);
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
            // 修改文章内容
            ApArticleContent apArticleContent = apArticleContentMapper.selectOne(Wrappers.<ApArticleContent>lambdaQuery().eq(ApArticleContent::getArticleId, dto.getId()));
            apArticleContent.setContent(dto.getContent());
            apArticleContentMapper.updateById(apArticleContent);
        }

        //异步调用，生成静态文件上传到minio中
        articleFreemarkerService.buildArticleToMinIO(apArticle,dto.getContent());

        //3.结果返回  文章的id
        return ResponseResult.okResult(apArticle.getId());
    }

    /**
     * 更新文章的分值，同时更新缓存中的热点文章数据
     * @param mess
     */
    @Override
    public void updateScore(ArticleVisitStreamMess mess) {
        // 1. 更新文章的阅读、点赞、收藏、评论的数量
        ApArticle apArticle = updateArticle(mess);

        // 2. 计算文章的分值
        Integer score = computeScore(apArticle);
        score = score * 3;

        // 3. 替换当前文章对应的频道的热点数据
        replaceDataToRedis(apArticle, score, ArticleConstants.HOT_ARTICLE_FIRST_PAGE + apArticle.getChannelId());

        // 4. 替换推荐对应的热点数据
        replaceDataToRedis(apArticle, score, ArticleConstants.HOT_ARTICLE_FIRST_PAGE + ArticleConstants.DEFAULT_TAG);
    }

    /**
     * 替换数据并且存入到redis
     * @param apArticle
     * @param score
     * @param s
     */
    private void replaceDataToRedis(ApArticle apArticle, Integer score, String s) {
        String articleListStr = cacheService.get(s);
        if (StringUtils.isNotBlank(articleListStr)) {
            List<HotArticleVo> hotArticleVoList = JSON.parseArray(articleListStr, HotArticleVo.class);

            boolean flag = true;

            //如果缓存中存在该文章，只更新分值
            //需要经过循环比对是否存在对应的文章
            for (HotArticleVo hotArticleVo : hotArticleVoList) {
                if (hotArticleVo.getId().equals(apArticle.getId())) {
                    hotArticleVo.setScore(score);
                    flag = false;
                    break;
                }
            }

            //如果缓存中不存在，查询缓存中分值最小的一条数据，进行分值的比较，如果当前文章的分值大于缓存中的数据，就替换
            if (flag) {
                if (hotArticleVoList.size() >= 30) {
                    hotArticleVoList = hotArticleVoList.stream().sorted(Comparator.comparing(HotArticleVo::getScore).reversed()).collect(Collectors.toList());
                    HotArticleVo lastHot = hotArticleVoList.get(hotArticleVoList.size() - 1);
                    if (lastHot.getScore() < score) {
                        hotArticleVoList.remove(lastHot);
                        HotArticleVo hot = new HotArticleVo();
                        BeanUtils.copyProperties(apArticle, hot);
                        hot.setScore(score);
                        hotArticleVoList.add(hot);
                    }
                } else {
                    HotArticleVo hot = new HotArticleVo();
                    BeanUtils.copyProperties(apArticle, hot);
                    hot.setScore(score);
                    hotArticleVoList.add(hot);
                }
            }
            //缓存到redis
            hotArticleVoList = hotArticleVoList.stream().sorted(Comparator.comparing(HotArticleVo::getScore).reversed()).collect(Collectors.toList());
            cacheService.set(s, JSON.toJSONString(hotArticleVoList));
        }
    }

    /**
     * 计算文章的具体分值
     * @param apArticle
     * @return
     */
    private Integer computeScore(ApArticle apArticle) {
        Integer score = 0;
        if(apArticle.getLikes() != null) {
            score += apArticle.getLikes() * ArticleConstants.HOT_ARTICLE_LIKE_WEIGHT;
        }
        if(apArticle.getViews() != null){
            score += apArticle.getViews();
        }
        if(apArticle.getComment() != null){
            score += apArticle.getComment() * ArticleConstants.HOT_ARTICLE_COMMENT_WEIGHT;
        }
        if(apArticle.getCollection() != null){
            score += apArticle.getCollection() * ArticleConstants.HOT_ARTICLE_COLLECTION_WEIGHT;
        }

        return score;
    }

    /**
     * 更新文章行为数量
     * @param mess
     * @return
     */
    private ApArticle updateArticle(ArticleVisitStreamMess mess) {
        ApArticle apArticle = getById(mess.getArticleId());
        apArticle.setCollection(apArticle.getCollection()==null?0:apArticle.getCollection()+mess.getCollect());
        apArticle.setComment(apArticle.getComment()==null?0:apArticle.getComment()+mess.getComment());
        apArticle.setLikes(apArticle.getLikes()==null?0:apArticle.getLikes()+mess.getLike());
        apArticle.setViews(apArticle.getViews()==null?0:apArticle.getViews()+mess.getView());
        updateById(apArticle);

        return apArticle;
    }

     /*
      * @Title: loadArticleBehavior
      * @Author: pyzxW
      * @Date: 2025-02-20 16:04:31
      * @Params:
      * @Return: null
      * @Description: 数据进行回显
      */
    @Override
    public ResponseResult loadArticleBehavior(ArticleInfoDto dto) {
        // 1. 检查参数
        if(dto == null || dto.getArticleId() == 0 || dto.getAuthorId() == 0 ) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //{ "isfollow": true, "islike": true,"isunlike": false,"iscollection": true }
        boolean isFollow = false, isLike = false, isUnlike = false, isCollection = false;

        //2.获取用户信息
        ApUser user = AppThreadLocalUtil.getUser();

        //3.查询用户的行为
        if (user != null){
            //喜欢的行为
            String likeBehaviorJson = (String) cacheService.hGet(BehaviorConstants.LIKE_BEHAVIOR + dto.getArticleId(), user.getId().toString());
            if(StringUtils.isNotBlank(likeBehaviorJson)){
                //likeBehaviorJson 非空，则认为用户对这篇文章点过赞
                isLike = true;
            }

            //不喜欢的行为
            String unLikeBehaviorJson = (String) cacheService.hGet(BehaviorConstants.UN_LIKE_BEHAVIOR + dto.getArticleId(), user.getId().toString());
            if(StringUtils.isNotBlank(unLikeBehaviorJson)){
                isUnlike = true;
            }

            //是否收藏
            String collctionJson = (String) cacheService.hGet(BehaviorConstants.LIKE_BEHAVIOR + user.getId(), String.valueOf(dto.getArticleId()));
            if(StringUtils.isNotBlank(collctionJson)){
                isCollection = true;
            }

            // 3.4 是否关注
            Double score = cacheService.zScore(BehaviorConstants.APUSER_FOLLOW_RELATION + user.getId(), dto.getAuthorId());
            System.out.println(score);
            if(score != null){
                isFollow = true;
            }
        }

        //更新对应的信息
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("isfollow", isFollow);
        resultMap.put("islike", isLike);
        resultMap.put("isunlike", isUnlike);
        resultMap.put("iscollection", isCollection);
        //4.返回最终的信息
        //返回的JSON都是用HashMap封装的
        return ResponseResult.okResult(resultMap);
    }

    /**
     * 查询文章的评论统计
     * @param dto
     * @return
     */
    @Override
    public PageResponseResult findNewsComments(ArticleCommentDto dto) {
        // 1. 统计文章评论信息
        Integer currentPage = dto.getPage();
        dto.setPage((dto.getPage() - 1) * dto.getSize());
        List<ArticleCommnetVo> list = apArticleMapper.findNewsComments(dto);
        int count = apArticleMapper.findNewsCommentsCount(dto);

        // 2. 构造结果返回
        PageResponseResult responseResult = new PageResponseResult(currentPage, dto.getSize(), count);
        responseResult.setData(list);
        return responseResult;
    }
}
