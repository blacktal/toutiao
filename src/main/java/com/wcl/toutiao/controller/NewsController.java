package com.wcl.toutiao.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wcl.toutiao.model.Comment;
import com.wcl.toutiao.model.HostHolder;
import com.wcl.toutiao.model.News;
import com.wcl.toutiao.model.User;
import com.wcl.toutiao.model.ViewObject;
import com.wcl.toutiao.service.CommentService;
import com.wcl.toutiao.service.NewsService;
import com.wcl.toutiao.service.UserService;
import com.wcl.toutiao.util.EntityType;
import com.wcl.toutiao.util.ToutiaoUtil;

/**
 * @ClassName: NewsController 
 * @Description: TODO 
 * @author Lulu
 * @date 2017年12月1日 上午10:39:28 
 */
@Controller
public class NewsController {
    
    @Autowired
    NewsService newsService;
    
    @Autowired
    HostHolder hostHolder;
    
    @Autowired
    UserService userService;
    
    @Autowired
    CommentService commentService;
    
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    
    /**   
     * @Title: uploadImage   
     * @Description: 将图片上传到本地目录 C盘：图片
     * @param: @param file
     * @param: @return      
     * @return: String      
     * @throws   
     */  
    @RequestMapping(path = {"/uploadImage/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // 要上传的文件就在multipartFile中，要对其进行一些解析处理
            String fileUrl = newsService.saveImage(file);
            if (fileUrl == null) {
                return ToutiaoUtil.getJSONString(1, "上传图片失败");
            }
            return ToutiaoUtil.getJSONString(0, fileUrl);
        } catch (Exception e) {
            logger.error("上传图片失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "上传图片失败");
        }
    }
    
    // TODO：多个图片上传
    
    
    /**   
     * @Title: getImage   
     * @Description: 访问上传的图片 
     * @param: @param name      
     * @return: void      
     * @throws   
     */  
    @RequestMapping(path = {"/image"}, method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String name,
                         HttpServletResponse response) {
        try {
            // 仿照其他图片请求的response格式，在response中设置一些值
            response.setContentType("image/jpeg");
            // 通过二进制流直接copy到response中，可以用Files或StreamUitls等任何工具类中的copy()方法
            
//            Files.copy(new File(ToutiaoUtil.IMAGE_DIR + name).toPath(), response.getOutputStream());
              
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_DIR + name)), response.getOutputStream());
              
        } catch (IOException e) {
            logger.error("读取图片出错");
        }
    }
    
    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId,
            @RequestParam("content") String content) {
        try {
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setEntityType(EntityType.ENTITY_NEWS.ordinal());
            comment.setCreatedDate(new Date());
            comment.setEntityId(newsId);
            comment.setStatus(0);
            comment.setUserId(hostHolder.getUser().getId());
            commentService.addComment(comment);
            
            // 更新news的comment数
            // 已经添加了新comment，然后统计该类comment的count
            int commentCount = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            newsService.updateCommentCount(commentCount, comment.getEntityId());
            
            // TODO:更行评论数异步化
            
        } catch (Exception e) {
            logger.error("提交评论错误" + e.getMessage());
        }
        return "redirect:/news/" + String.valueOf(newsId);
    }
    
    @RequestMapping(path = {"/news/{newsId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String showNewsDetails(Model model, @PathVariable("newsId") int newsId) {
        
        try {
            News news = newsService.getById(newsId);
            if (news != null) {
                List<Comment> commentList = commentService.selectCommentByEntityIndex(newsId, EntityType.ENTITY_NEWS.ordinal());
                // 在每个comment的viewObject中放入对应的commment-user属性
                List<ViewObject> commentVos = new ArrayList<>();
                for (Comment comment : commentList) {
                    ViewObject vo = new ViewObject();
                    vo.set("comment", comment);
                    vo.set("user", userService.getUser(comment.getUserId()));
                    commentVos.add(vo);
                }
                model.addAttribute("comments", commentVos);
            }
            model.addAttribute("news", news);
            model.addAttribute("owner", userService.getUser(news.getUserId()));
        } catch (Exception e) {
            logger.error("获取资讯详细信息错误" + e.getMessage());
        }
        return "detail";
    }
    
    
}
