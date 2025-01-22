package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.constants.WemediaConstants;
import com.heima.common.exception.CustomException;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmNewsMaterial;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.utils.thread.WmThreadLocalUtil;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.mapper.WmNewsMaterialMapper;
import com.heima.wemedia.service.WmNewsAutoScanService;
import com.heima.wemedia.service.WmNewsService;
import com.heima.wemedia.service.WmNewsTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class WmNewsServiceImpl  extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {

     /*
      * @Title: findAll
      * @Author: pyzxW
      * @Date: 2025-01-12 16:41:38
      * @Params:
      * @Return: null
      * @Description: 条件查询列表
      */
    @Override
    public ResponseResult findAll(WmNewsPageReqDto dto) {
        //1.检查参数
        if(dto == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //分页检查
        dto.checkParam();
        //获取当前登录人的信息
        WmUser user = WmThreadLocalUtil.getUser();
        if(user == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        //2.分页条件查询
        //创建一个页面的对象
        IPage page = new Page(dto.getPage(),dto.getSize());
        //Lambda查询包装器lambdaQueryWrapper，用于构建查询条件。WmNews是实体类，代表要查询的表对应的Java对象。
        LambdaQueryWrapper<WmNews> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //使用一个页面对象将查到的信息装起来
        page(page,lambdaQueryWrapper);
        //状态精确查询
        if (dto.getStatus() != null){
            lambdaQueryWrapper.eq(WmNews::getStatus,dto.getStatus());
        }
        //频道精确查询
        if (dto.getChannelId() != null){
            lambdaQueryWrapper.eq(WmNews::getChannelId,dto.getChannelId());
        }
        //时间范围查询
        if (dto.getBeginPubDate()!=null && dto.getEndPubDate()!=null){
            lambdaQueryWrapper.between(WmNews::getPublishTime,dto.getBeginPubDate(),dto.getEndPubDate());
        }

        //关键字模糊查询
        if (StringUtils.isNotBlank(dto.getKeyword())){
            lambdaQueryWrapper.like(WmNews::getTitle,dto.getKeyword());
        }

        //查询当前登录人的文章
        lambdaQueryWrapper.eq(WmNews::getUserId,user.getId());

        //按照发布时间倒序查询
        lambdaQueryWrapper.orderByDesc(WmNews::getCreatedTime);
        page = page(page,lambdaQueryWrapper);

        //3.结果返回
        //之后，再将上面查到的page对象封装到一个ResponseRsult对象中，统一进行返回
        ResponseResult responseResult = new PageResponseResult(dto.getPage(),dto.getSize(),(int)page.getTotal());
        //设置时间
        responseResult.setData(page.getRecords());
        return responseResult;
    }


    @Autowired
    private WmNewsAutoScanService wmNewsAutoScanService;
    @Autowired
    private WmNewsTaskService wmNewsTaskService;
     /*
      * @Title: submitNews
      * @Author: pyzxW
      * @Date: 2025-01-13 11:37:11
      * @Params:
      * @Return: null
      * @Description: 发布修改文章或保存为草稿
      */
    @Override
    public ResponseResult submitNews(WmNewsDto dto) {
        //0.条件判断
        if (dto == null || dto.getContent() == null){
            //返回参数失效
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //1.保存或者修改文章
        //定义对象
        WmNews wmNews = new WmNews();
        //属性拷贝
        BeanUtils.copyProperties(dto,wmNews);
        //封面图片 List--->string 处理
        if (dto.getImages() != null && dto.getImages().size() > 0){
            String imageStr = org.apache.commons.lang.StringUtils.join(dto.getImages(), ",");
            //在对应的数据库设置图片的名字属性
            wmNews.setImages(imageStr);
        }
        //如果当前封面类型为自动 -1
        //先设置为null过度
        if(dto.getType().equals(WemediaConstants.WM_NEWS_TYPE_AUTO)){
            wmNews.setType(null);
        }
        //调用保存或者修改文章函数
        saveOrUpdateWmNews(wmNews);

        //2.判断是否为草稿，如果是草稿则结束当前方法
        if (dto.getStatus().equals(WmNews.Status.NORMAL.getCode())){
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        //3.不是草稿，保存文章内容图片与素材的关系
        //获取到文章内容中的图片信息
        List<String> materials = ectractUrlInfo(dto.getContent());
        saveRelativeInfoForContent(materials,wmNews.getId());

        //4.不是草稿，保存文章封面图片与素材的关系，如果当前布局是自动，需要匹配封面图片
        //设置一个线程睡眠一秒
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        saveRelativeInfoForCover(dto,wmNews,materials);

        //在文章发布成功后调用审核的方法
        //审核文章
        //wmNewsAutoScanService.autoScanWmNews(wmNews.getId());
        wmNewsTaskService.addNewsToTask(wmNews.getId(),wmNews.getPublishTime());

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

     /*
      * @Title: saveRelativeInfoForCover
      * @Author: pyzxW
      * @Date: 2025-01-13 15:39:45
      * @Params:  
      * @Return: null
      * @Description:
      * 第一个功能：如果当前封面类型为自动，则设置封面类型的数据
      * 匹配规则：
      * 1，如果内容图片大于等于1，小于3  单图  type 1
      * 2，如果内容图片大于等于3  多图  type 3
      * 3，如果内容没有图片，无图  type 0
      *
      * 第二个功能：保存封面图片与素材的关系
      */
    private void saveRelativeInfoForCover(WmNewsDto dto, WmNews wmNews, List<String> materials){
        List<String> images = dto.getImages();
        //如果当前封面类型为自动，则设置封面类型的数据
        if (dto.getType().equals(WemediaConstants.WM_NEWS_TYPE_AUTO)) {
            //多图
            if (materials.size() >= 3) {
                wmNews.setType(WemediaConstants.WM_NEWS_MANY_IMAGE);
                //截取3张对应的图片
                images = materials.stream().limit(3).collect(Collectors.toList());
            } else if (materials.size() >= 1 && materials.size() < 3) {
                //单图
                wmNews.setType(WemediaConstants.WM_NEWS_SINGLE_IMAGE);
                //截取获得1张图片
                images = materials.stream().limit(1).collect(Collectors.toList());
            } else {
                //无图
                wmNews.setType(WemediaConstants.WM_NEWS_NONE_IMAGE);
            }
            //修改文章
            if (images != null && images.size() > 0) {
                wmNews.setImages(org.apache.commons.lang3.StringUtils.join(images, ","));
            }
            updateById(wmNews);
        }
        //2.保存封面图片与素材的关系
        if(images != null && images.size() > 0){
            saveRelativeInfo(images,wmNews.getId(),WemediaConstants.WM_COVER_REFERENCE);
        }
    }

     /*
      * @Title: saveRelativeInfoForContent
      * @Author: pyzxW
      * @Date: 2025-01-13 14:55:36
      * @Params:  
      * @Return: null
      * @Description: 处理文章内容与素材之间的关系
      */
    private void saveRelativeInfoForContent(List<String> materials, Integer id){
        saveRelativeInfo(materials,id,WemediaConstants.WM_CONTENT_REFERENCE);
    }

    @Autowired
    private WmMaterialMapper wmMaterialMapper;
     /*
      * @Title: saveRelativeInfo
      * @Author: pyzxW
      * @Date: 2025-01-13 15:01:02
      * @Params:
      * @Return: null
      * @Description: 保存文章图片与素材的关系到数据库中
      */
    private void saveRelativeInfo(List<String> materials, Integer newsId, Short type) {
        if(materials!=null && !materials.isEmpty()){
            //通过图片的url查询素材的id
            List<WmMaterial> dbMaterials = wmMaterialMapper.selectList(Wrappers.<WmMaterial>lambdaQuery().in(WmMaterial::getUrl, materials));

            //判断素材是否有效
            if (dbMaterials == null || dbMaterials.size() == 0){
                //手动抛出异常   第一个功能：能够提示调用者素材失效了，第二个功能，进行数据的回滚
                throw new CustomException(AppHttpCodeEnum.MATERIASL_REFERENCE_FAIL);
            }

            if(materials.size() != dbMaterials.size()){
                throw new CustomException(AppHttpCodeEnum.MATERIASL_REFERENCE_FAIL);
            }

            List<Integer> idList = dbMaterials.stream().map(WmMaterial::getId).collect(Collectors.toList());

            //批量保存
            wmNewsMaterialMapper.saveRelations(idList,newsId,type);
        }
    }
     /*
      * @Title: ectractUrlInfo
      * @Author: pyzxW
      * @Date: 2025-01-13 14:48:20
      * @Params:
      * @Return: null
      * @Description: 提取当前文章内容中的图片信息
      */
    private List<String> ectractUrlInfo(String content){
        //图片路径list集合
        List<String> materials = new ArrayList<>();
        //mao用于方便循环操作
        List<Map> maps = JSON.parseArray(content, Map.class);
        for (Map map : maps) {
            if(map.get("type").equals("image")){
                //图片路径获取
                String imgUrl = (String) map.get("value");
                materials.add(imgUrl);
            }
        }
        return materials;
    }

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

     /*
      * @Title: saveOrUpdateWmNews
      * @Author: pyzxW
      * @Date: 2025-01-13 14:28:50
      * @Params:
      * @Return: null
      * @Description: 保存或者修改文章
      */
    private void saveOrUpdateWmNews(WmNews wmNews){
       //补全属性
        wmNews.setUserId(WmThreadLocalUtil.getUser().getId());
        wmNews.setCreatedTime(new Date());
        wmNews.setSubmitedTime(new Date());
        wmNews.setEnable((short)1);//默认上架

        if (wmNews.getId() == null){
            //保存
            save(wmNews);
        }else {
            //修改
            //删除文章图片与素材的关联关系
            wmNewsMaterialMapper.delete(Wrappers.<WmNewsMaterial>lambdaQuery().eq(WmNewsMaterial::getNewsId,wmNews.getId()));
            updateById(wmNews);
        }
    }

}
