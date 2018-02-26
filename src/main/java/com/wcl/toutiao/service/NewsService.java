package com.wcl.toutiao.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.wcl.toutiao.dao.NewsDao;
import com.wcl.toutiao.model.News;
import com.wcl.toutiao.util.ToutiaoUtil;

/**
 * @ClassName: NewsService 
 * @Description: TODO 
 * @author Lulu
 * @date 2017年11月20日 下午3:20:34 
 */
@Service
public class NewsService {
    @Autowired
    NewsDao newsDao;
    
    public List<News> getLastedNews(int userId, int offset, int limit) {
        return newsDao.selectByUserIdAndOffset(userId, offset, limit);
    }
    
    public News getById(int newsId) {
        return newsDao.selectById(newsId);
    }
    
    public void updateCommentCount(int commentCount, int newsId) {
        newsDao.updateCommentCount(commentCount, newsId);
    }
    
    public void updateLikeCount(int likeCount, int newsId) {
        newsDao.updateLikeCount(likeCount, newsId);
    }

    /**
     * @throws IOException    
     * @Title: 将用户上传的图片保存在本地目录   
     * @Description: TODO   
     * @param: @param file
     * @param: @return      
     * @return: String      
     * @throws   
     */  
    public String saveImage(MultipartFile file) throws IOException {
        // 因为是上传图片，先判断一下图片格式是否正确
        // 找到文件名中.的位置
        int dotPosition = file.getOriginalFilename().indexOf(".");
        if (dotPosition < 0) {
            // 没有.就返回null
            return null;
        }
        String fileExt = file.getOriginalFilename().substring(dotPosition + 1).toLowerCase();
        // 将判断图片后缀名是否符合要求的方法放在Toutiao.Util中，直接调用该工具类方法
        if (!ToutiaoUtil.isImageAllowed(fileExt)) {
            return null;
        }
        
        // 如果符合格式，将文件保存在本地目录
        
        // 用户上传的文件名可能很乱，所以用uuid重新定义图片名称
        String imageName = UUID.randomUUID().toString().replaceAll("-", "") +"." + fileExt;
        // 使用copy()方法进行上传
        Files.copy(file.getInputStream(),
                   new File(ToutiaoUtil.IMAGE_DIR + imageName).toPath(),
                   StandardCopyOption.REPLACE_EXISTING);
        // 返回前端要用的图片的访问地址
        return ToutiaoUtil.TOUTIAO_DOMAIN + "image?name=" + imageName;
    }
}
