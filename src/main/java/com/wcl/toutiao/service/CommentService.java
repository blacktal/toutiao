package com.wcl.toutiao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcl.toutiao.dao.CommentDao;
import com.wcl.toutiao.model.Comment;

/**
 * @ClassName: CommentService 
 * @Description: TODO 
 * @author Lulu
 * @date 2017年12月10日 上午11:13:40 
 */
@Service
public class CommentService {
    
    @Autowired
    CommentDao commentDao;
    
    public int addComment(Comment comment) {
        return commentDao.addComment(comment);
    }
    
    public List<Comment> selectCommentByEntityIndex(int entityId, int entityType) {
        return commentDao.selectByEntityIndex(entityId, entityType);
    }
    
    public int getCommentCount(int entityId, int entityType) {
        return commentDao.getCommentCount(entityId, entityType);
    }
}
