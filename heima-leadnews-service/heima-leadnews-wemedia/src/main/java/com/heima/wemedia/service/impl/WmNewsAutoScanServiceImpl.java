package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.heima.common.aliyun.GreenTextScan;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.service.WmNewsAutoScanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

            //4.审核成功，保存app端的相关文章数据
        }
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
        try {
            Map map = greenTextScan.greeTextScan(content);
            if (map != null){
                //审核失败
                if (map.get("suggestion").equals("block")){
                    wmNews.setStatus((short)2);
                }
                //不确定信息 需要人工审核
                if (map.get("suggestion").equals("review")){
                    //人工审核信息
                    wmNews.setStatus((short)3);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


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