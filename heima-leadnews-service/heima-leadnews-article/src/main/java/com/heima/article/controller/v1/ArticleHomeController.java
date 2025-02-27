package com.heima.article.controller.v1;

import com.heima.model.article.dtos.ArticleHomeDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleHomeController {
    /**
     * 加载首页
     * @param dto
     * @return
     */
    @PostMapping("/load")
    public String load(@RequestBody(required = false) ArticleHomeDto dto) {
        return "success";
    }

    /**
     * 加载更多
     * @param dto
     * @return
     */
    @PostMapping("/loadmore")
    public String loadmore(@RequestBody(required = false)ArticleHomeDto dto) {
        return "success";
    }

    /**
     * 加载最新
     * @param dto
     * @return
     */
    @PostMapping("/loadnew")
    public String loadnew(@RequestBody(required = false) ArticleHomeDto dto) {
        return "success";
    }
}
