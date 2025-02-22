package com.heima.article.service.impl;

import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.HotArticleService;
import com.heima.common.constants.ArticleConstants;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.vos.HotArticleVo;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


    }

     /*
      * @Title: computeHotArticle
      * @Author: pyzxW
      * @Date: 2025-02-22 20:14:25
      * @Params:  
      * @Return: null
      * @Description: 计算文章的分值操作
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
