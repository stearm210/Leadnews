package com.heima.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.apis.wemedia.IWemediaClient;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.HotArticleService;
import com.heima.common.constants.ArticleConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.vos.HotArticleVo;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.article.service.impl
 * @Author: yanhongwei
 * @CreateTime: 2025-02-22  20:02
 * @Description: TODO
 * @Version: 1.0
 */
@Service
@Slf4j
@Transactional
public class HotArticleServiceImpl implements HotArticleService {
    @Autowired
    private ApArticleMapper apArticleMapper;

    /**
     * 计算热点文章
     */
    @Override
    public void computeHotArticle() {
        //1.查询前5天的文章数据
        Date dateParam = DateTime.now().minusDays(5).toDate();
        List<ApArticle> apArticleList = apArticleMapper.findArticleListByLast5days(dateParam);

        //2.计算文章的分值
        List<HotArticleVo> hotArticleVoList = computeHotArticle(apArticleList);

        //3.为每个频道缓存30条分值较高的文章
        cacheTagToRedis(hotArticleVoList);
    }


    @Autowired
    private IWemediaClient wemediaClient;

    @Autowired
    private CacheService cacheService;
     /*
      * @Title: cacheTagToRedis
      * @Author: pyzxW
      * @Date: 2025-02-22 20:37:15
      * @Params:
      * @Return: null
      * @Description: 为每个频道缓存30条分值较高的文章
      */
    private void cacheTagToRedis(List<HotArticleVo> hotArticleVoList) {
        //为每个频道缓存30条分值较高的文章
        ResponseResult responseResult = wemediaClient.getChannels();
        //获得频道的操作是成功的//这是由于不同微服务之间进行调用，需要判断是否正常相应
        if (responseResult.getCode().equals(200)){
            //先转为JSON格式
            String channelJson = JSON.toJSONString(responseResult.getData());
            //再从JSON格式转换为实体类方便进行操作
            List<WmChannel> wmChannels = JSON.parseArray(channelJson, WmChannel.class);
            //检索出每个频道的文章
            if(wmChannels != null && wmChannels.size() > 0){
                //对某一个频道进行循环
                for (WmChannel wmChannel : wmChannels) {
                    //list中装着某一个频道中的所有文章数据
                    List<HotArticleVo> hotArticleVos = hotArticleVoList.stream().filter(x -> x.getChannelId().equals(wmChannel.getId())).collect(Collectors.toList());
                    //给文章进行排序，取30条分值较高的文章存入redis  key：频道id   value：30条分值较高的文章
                    sortAndCache(hotArticleVos, ArticleConstants.HOT_ARTICLE_FIRST_PAGE + wmChannel.getId());
                }
            }
        }
        //设置推荐的数据
        //给文章进行排序，取30条分值较高的文章存入redis  key：频道id   value：30条分值较高的文章
        sortAndCache(hotArticleVoList, ArticleConstants.HOT_ARTICLE_FIRST_PAGE+ArticleConstants.DEFAULT_TAG);
    }

     /*
      * @Title: sortAndCache
      * @Author: pyzxW
      * @Date: 2025-02-22 20:51:20
      * @Params:
      * @Return: null
      * @Description: 给文章进行排序,取30条分值较高的文章存入redis
      */
     private void sortAndCache(List<HotArticleVo> hotArticleVos, String key) {
         //查到文章进行排序
         hotArticleVos = hotArticleVos.stream().sorted(Comparator.comparing(HotArticleVo::getScore).reversed()).collect(Collectors.toList());
         //如果大于30条，则只取30条
         if (hotArticleVos.size() > 30) {
             hotArticleVos = hotArticleVos.subList(0, 30);
         }
         //缓存到redis中
         //key为频道id
         cacheService.set(key, JSON.toJSONString(hotArticleVos));
     }

    /*
      * @Title: computeHotArticle
      * @Author: pyzxW
      * @Date: 2025-02-22 20:14:25
      * @Params:  
      * @Return: null
      * @Description: 文章的分值操作
      */
    private List<HotArticleVo> computeHotArticle(List<ApArticle> apArticleList) {
        List<HotArticleVo> hotArticleVoList = new ArrayList<>();
        //循环文章列表，方便进行计算操作
        if(apArticleList != null && apArticleList.size() > 0){
            for (ApArticle apArticle : apArticleList) {
                HotArticleVo hot = new HotArticleVo();
                //先进行参数的拷贝
                BeanUtils.copyProperties(apArticle,hot);
                //计算分数
                Integer score = computeScore(apArticle);
                //设置分值
                hot.setScore(score);
                hotArticleVoList.add(hot);
            }
        }
        return hotArticleVoList;
    }

     /*
      * @Title: computeScore
      * @Author: pyzxW
      * @Date: 2025-02-22 20:22:38
      * @Params:
      * @Return: null
      * @Description: 计算分值的操作
      */
     private Integer computeScore(ApArticle apArticle) {
         Integer scere = 0;
         //点赞操作
         if(apArticle.getLikes() != null){
             scere += apArticle.getLikes() * ArticleConstants.HOT_ARTICLE_LIKE_WEIGHT;
         }
         //文章阅读对应分数
         if(apArticle.getViews() != null){
             scere += apArticle.getViews();
         }
         //文章评论对应分数
         if(apArticle.getComment() != null){
             scere += apArticle.getComment() * ArticleConstants.HOT_ARTICLE_COMMENT_WEIGHT;
         }
         //文章收藏对应分数
         if(apArticle.getCollection() != null){
             scere += apArticle.getCollection() * ArticleConstants.HOT_ARTICLE_COLLECTION_WEIGHT;
         }
         return scere;
     }
}
