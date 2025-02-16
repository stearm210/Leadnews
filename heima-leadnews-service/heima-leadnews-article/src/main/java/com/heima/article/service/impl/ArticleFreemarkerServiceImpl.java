package com.heima.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.service.ApArticleService;
import com.heima.article.service.ArticleFreemarkerService;
import com.heima.common.constants.ArticleConstants;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.search.vos.SearchArticleVo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.article.service.impl
 * @Author: yanhongwei
 * @CreateTime: 2025-01-18  21:08
 * @Description: TODO
 * @Version: 1.0
 */

@Slf4j
@Service
@Transactional
public class ArticleFreemarkerServiceImpl implements ArticleFreemarkerService {
    @Autowired
    private ApArticleContentMapper apArticleContentMapper;
    @Autowired
    private Configuration configuration;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ApArticleService apArticleService;

    /**
     * 生成静态文件上传到minIO中
     *
     * @param apArticle
     * @param content
     */
    @Override
    @Async
    public void buildArticleToMinIO(ApArticle apArticle, String content) {
        //已知文章的id
        //4.1 获取文章内容
        if(StringUtils.isNotBlank(content)){
            //4.2 文章内容通过freemarker生成html文件
            Template template = null;
            StringWriter out = new StringWriter();
            try {
                template = configuration.getTemplate("article.ftl");
                //数据模型
                Map<String,Object> contentDataModel = new HashMap<>();
                contentDataModel.put("content", JSONArray.parseArray(content));
                //合成
                template.process(contentDataModel,out);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //4.3 把html文件上传到minio中
            InputStream in = new ByteArrayInputStream(out.toString().getBytes());
            String path = fileStorageService.uploadHtmlFile("", apArticle.getId() + ".html", in);
            //4.4 修改ap_article表，保存static_url字段
            apArticleService.update(Wrappers.<ApArticle>lambdaUpdate().eq(ApArticle::getId,apArticle.getId())
                    .set(ApArticle::getStaticUrl,path));

            //发送消息，创建索引
            createArticleESIndex(apArticle,content,path);
        }
    }

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
     /*
      * @Title: createArticleESIndex
      * @Author: pyzxW
      * @Date: 2025-02-16 12:19:57
      * @Params:
      * @Return: null
      * @Description: 发送消息，创建索引
      */
    private void createArticleESIndex(ApArticle apArticle, String content, String path) {
        //消息类型类，包含多种属性
        SearchArticleVo vo = new SearchArticleVo();
        BeanUtils.copyProperties(apArticle,vo);
        //拷贝对应的内容
        vo.setContent(content);
        //拷贝对应的静态地址
        vo.setStaticUrl(path);
        //传递对应的消息至kafka
        kafkaTemplate.send(ArticleConstants.ARTICLE_ES_SYNC_TOPIC, JSON.toJSONString(vo));
    }
}
