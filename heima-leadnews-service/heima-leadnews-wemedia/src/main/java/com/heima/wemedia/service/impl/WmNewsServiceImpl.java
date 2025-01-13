package com.heima.wemedia.service.impl;


import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.utils.thread.WmThreadLocalUtil;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.service.WmNewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if(dto.getType().equals(-1)){
            wmNews.setType(null);
        }

        //2.判断是否为草稿，如果是草稿则结束当前方法

        //3.不是草稿，保存文章内容图片与素材的关系

        //4.不是草稿，保存文章封面图片与素材之间的关系

        return null;
    }
}
