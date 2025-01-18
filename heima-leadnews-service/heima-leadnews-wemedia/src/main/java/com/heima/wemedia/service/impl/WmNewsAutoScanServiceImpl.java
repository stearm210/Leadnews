package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.heima.apis.article.IArticleClient;
import com.heima.common.aliyun.GreenImageScan;
import com.heima.common.aliyun.GreenTextScan;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.wemedia.mapper.WmChannelMapper;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.mapper.WmUserMapper;
import com.heima.wemedia.service.WmNewsAutoScanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.wemedia.service.impl
 * @Author: yanhongwei
 * @CreateTime: 2025-01-16  15:53
 * @Description: TODO
 * @Version: 1.0
 */

@Service
@Slf4j
@Transactional
public class WmNewsAutoScanServiceImpl implements WmNewsAutoScanService {
    @Autowired
    private WmNewsMapper wmNewsMapper;
     /*
      * @Title: autoScanWmNews
      * @Author: pyzxW
      * @Date: 2025-01-16 15:54:22
      * @Params:
      * @Return: null
      * @Description: 自媒体文章审核
      */
    @Override
    @Async //表明当前方法是一个异步的调用
    public void autoScanWmNews(Integer id) {
        //1.查询自媒体文章
        WmNews wmNews = wmNewsMapper.selectById(id);
        if (wmNews == null){
            throw new RuntimeException("WmNewsAutoScanServiceImpl-文章不存在");
        }
        //需要注意，这里必须为文章待审核的状态
        if (wmNews.getStatus().equals(WmNews.Status.SUBMIT.getCode())){
            //从内容中提取纯文本内容和图片
            Map<String,Object> textAndImages = handleTextAndImages(wmNews);
            //2.审核文本内容
            //返回一个布尔值来判定是否审核成功或者失败
            boolean isTextScan = handleTextScan((String) textAndImages.get("content"), wmNews);
            if (!isTextScan){
                return;//失败时的状态
            }
            //3.审核图片内容
            boolean isImageScan = handleImagesScan((List<String>) textAndImages.get("images"), wmNews);
            if (!isImageScan){
                return;//失败时的状态
            }
            //4.审核成功，保存app端的相关文章数据
            //调用保存方法
            ResponseResult responseResult = saveAppArticle(wmNews);
            if (!responseResult.getCode().equals(200)){
                throw new RuntimeException("WmNewsAutoScanServiceImpl-文章审核，保存app端相关文章数据失败");
            }
            //回填atricle_id
            //审核成功
            wmNews.setArticleId((Long) responseResult.getData());
            updateWmNews(wmNews,(short) 9,"审核成功");
        }
    }


    //调用远程接口apis
    @Autowired
    private IArticleClient articleClient;
    @Autowired
    private WmChannelMapper wmChannelMapper;
    @Autowired
    private WmUserMapper wmUserMapper;
     /*
      * @Title: saveAppArticle
      * @Author: pyzxW
      * @Date: 2025-01-17 17:28:47
      * @Params:
      * @Return: null
      * @Description: 保存文章
      */
    private ResponseResult saveAppArticle(WmNews wmNews) {
        ArticleDto dto = new ArticleDto();
        //属性的拷贝
        BeanUtils.copyProperties(wmNews,dto);
        //文章的布局
        dto.setLayout(wmNews.getType());
        //频道
        //通过id查询name
        WmChannel wmChannel = wmChannelMapper.selectById(wmNews.getChannelId());
        if(wmChannel != null){
            dto.setChannelName(wmChannel.getName());
        }

        //作者
        //通过id查询name
        dto.setAuthorId(wmNews.getUserId().longValue());
        WmUser wmUser = wmUserMapper.selectById(wmNews.getUserId());
        if(wmUser != null){
            dto.setAuthorName(wmUser.getName());
        }

        //设置文章id
        if(wmNews.getArticleId() != null){
            dto.setId(wmNews.getArticleId());
        }
        //创建时间
        dto.setCreatedTime(new Date());

        ResponseResult responseResult = articleClient.saveArticle(dto);
        return responseResult;
    }

    @Autowired
    private FileStorageService fileStorageService;

    //用于审核图片
    @Autowired
    private GreenImageScan greenImageScan;

     /*
      * @Title: handleImagesScan
      * @Author: pyzxW
      * @Date: 2025-01-17 17:16:53
      * @Params:
      * @Return: null
      * @Description: 审核图片内容
      */
    private boolean handleImagesScan(List<String> images, WmNews wmNews) {
        boolean flag = true;

        if(images == null || images.size() == 0){
            return flag;
        }
        //下载图片 minIO
        //图片去重
        images = images.stream().distinct().collect(Collectors.toList());

        //定义数组进行存储
        List<byte[]> imageList = new ArrayList<>();

        //循环下载对应的额图片
        for (String image : images) {
            byte[] bytes = fileStorageService.downLoadFile(image);
            imageList.add(bytes);
        }

        //批量审核图片
        try {
            Map map = greenImageScan.imageScan(imageList);
            if(map != null){
                //审核失败
                if(map.get("suggestion").equals("block")){
                    flag = false;
                    updateWmNews(wmNews, (short) 2, "当前文章中存在违规内容");
                }

                //不确定信息  需要人工审核
                if(map.get("suggestion").equals("review")){
                    flag = false;
                    updateWmNews(wmNews, (short) 3, "当前文章中存在不确定内容");
                }
            }
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    @Autowired
    private GreenTextScan greenTextScan;
     /*
      * @Title: handleTextScan
      * @Author: pyzxW
      * @Date: 2025-01-17 16:36:13
      * @Params:  
      * @Return: null
      * @Description: 审核纯文本内容
      */
    private boolean handleTextScan(String content, WmNews wmNews) {
        //这里是默认审核通过的
        //到时可以在方法中进行修改
        boolean flag = true;

        //当前文章为空时
        if((wmNews.getTitle()+"-"+content).length() == 0){
            return flag;
        }
        try {
            //标题和文本同时进行审核
            Map map = greenTextScan.greeTextScan((wmNews.getTitle()+"-"+content));
            if (map != null){
                //审核失败
                if (map.get("suggestion").equals("block")){
                    flag = false;
                    updateWmNews(wmNews, (short) 2, "当前文章中存在违规内容");
                }
                //不确定信息 需要人工审核
                if (map.get("suggestion").equals("review")){
                    flag = false;
                    updateWmNews(wmNews, (short) 3, "当前文章中存在不确定内容");
                }
            }
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

     /*
      * @Title: updateWmNews
      * @Author: pyzxW
      * @Date: 2025-01-17 16:51:28
      * @Params:status
      * @Return: null
      * @Description: 保存数据库方法
      */
    private void updateWmNews(WmNews wmNews, short status ,String reason){
        wmNews.setStatus(status);
        //拒绝的原因
        wmNews.setReason(reason);
        //保存到数据库
        wmNewsMapper.updateById(wmNews);
    }


    /*
      * @Title: handleTextAndImages
      * @Author: pyzxW
      * @Date: 2025-01-16 16:01:48
      * @Params:
      * @Return: null
      * @Description:
      * 1.从自媒体文章的内容中提取文本和图片
      * 2.提取文章的封面图片
      */
    private Map<String, Object> handleTextAndImages(WmNews wmNews){
        //存储纯文本内容
        StringBuilder stringBuilder = new StringBuilder();
        //存储图片内容
        List<String> images = new ArrayList<>();
        //1.从自媒体文章的内容中提取文本和图片
        if(StringUtils.isNotBlank(wmNews.getContent())){
            List<Map> maps = JSONArray.parseArray(wmNews.getContent(), Map.class);
            for (Map map : maps) {
                if (map.get("type").equals("text")){
                    stringBuilder.append(map.get("value"));
                }
                if (map.get("type").equals("image")){
                    images.add((String) map.get("value"));
                }
            }
        }
        //2.提取文章的封面图片
        if(StringUtils.isNotBlank(wmNews.getImages())){
            String[] split = wmNews.getImages().split(",");
            images.addAll(Arrays.asList(split));
        }

        //构建返回参数
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("content",stringBuilder.toString());
        resultMap.put("images",images);
        return resultMap;
    }
}
