package com.heima.comment.controller;
 
import com.heima.comment.service.CommentService;
import com.heima.model.comment.dtos.CommentDto;
import com.heima.model.comment.dtos.CommentLikeDto;
import com.heima.model.comment.dtos.CommentSaveDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {
    
    @Autowired
    private CommentService commentService;
 
    /**
     * 保存评论
     * @param dto
     * @return
     */
    @PostMapping("/save")
    public ResponseResult saveComment(@RequestBody CommentSaveDto dto) {
        return commentService.saveComment(dto);
    }
 
    /**
     * 点赞评论
     * @param dto
     * @return
     */
    @PostMapping("/like")
    public ResponseResult like(@RequestBody CommentLikeDto dto) {
        return commentService.like(dto);
    }
 
    /**
     * 加载评论
     * @param dto
     * @return
     */
    @PostMapping("/load")
    public ResponseResult findByArticleId(@RequestBody CommentDto dto) {
        return commentService.findByArticleId(dto);
    }
}