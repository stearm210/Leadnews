package com.heima.article.test;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.article.ArticleApplication;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleContent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.article.test
 * @Author: yanhongwei
 * @CreateTime: 2025-01-10  16:05
 * @Description: TODO
 * @Version: 1.0
 */

@SpringBootTest(classes = ArticleApplication.class)
@RunWith(SpringRunner.class)
public class ArticleFreemarkerTest {
    @Autowired
    private Configuration configuration;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private ApArticleContentMapper apArticleContentMapper ;
    @Test
    public void createStaticUrlTest() throws Exception {
        //1.获取文章内容
        //通过apArticleContentMapper从数据库中查询ApArticleContent表中articleId等于1390536764510310401的那一条记录，并将查询结果封装到一个ApArticleContent对象中，赋值给变量apArticleContent
        ApArticleContent apArticleContent = apArticleContentMapper.selectOne(Wrappers.<ApArticleContent>lambdaQuery().eq(ApArticleContent::getArticleId, 1390536764510310401L));
        //不存在文章时跳过生成html文件，或者没有内容时也将跳过生成
        if(apArticleContent != null && StringUtils.isNotBlank(apArticleContent.getContent())){
            //2.文章内容通过freemarker生成html文件
            StringWriter out = new StringWriter();
            //找到模版
            Template template = configuration.getTemplate("article.ftl");

            //信息传入map中
            Map<String, Object> params = new HashMap<>();
            //传入内容，并且转换为字符串
            params.put("content", JSONArray.parseArray(apArticleContent.getContent()));

            //对应模版信息传入
            //合成信息
            template.process(params, out);
            //上传操作
            InputStream is = new ByteArrayInputStream(out.toString().getBytes());

            //3.把html文件上传到minio中
            //返回文件上传完成之后的路径
            String path = fileStorageService.uploadHtmlFile("", apArticleContent.getArticleId() + ".html", is);

            //4.修改ap_article表，保存static_url字段
            ApArticle article = new ApArticle();
            //对数据库表中的文章id和对应的文章地址进行保存
            article.setId(apArticleContent.getArticleId());
            article.setStaticUrl(path);
            //保存对应的id
            apArticleMapper.updateById(article);
        }
    }
}

