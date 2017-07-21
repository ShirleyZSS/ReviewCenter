package com.nowcoder.service;

import com.nowcoder.dao.NewsDAO;
import com.nowcoder.model.News;
import com.nowcoder.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Created by Shirley on 2017/7/13.
 */
@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;

    public List<News> getLatestNews(int userId,int offset,int limit){
        return newsDAO.selectByUserIdAndOffset(userId,offset,limit);
    }

    public int addNews(News news){
        newsDAO.addNews(news);
        return news.getId();
    }

    public News getById(int newsId){
        return newsDAO.getById(newsId);
    }

    public String saveImage(MultipartFile file)throws IOException{
        int dotPos=file.getOriginalFilename().lastIndexOf(".");
        if(dotPos<0){
            return null;
        }
        String fileExt=file.getOriginalFilename().substring(dotPos+1);
        if(!ToutiaoUtil.isFileAllowed(fileExt.toLowerCase())){
            return null;
        }
        //规范上传文件名
        String fileName= UUID.randomUUID().toString().replaceAll("-","")+"."+fileExt;

        //将二进制流文件copy下来 存储在目标路径下 如果已经存在则替换掉
        Files.copy(file.getInputStream(),new File(ToutiaoUtil.IMAGE_DIR+fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);

        return ToutiaoUtil.TOUTIAO_DOMAIN+"image?name="+fileName;




    }

    public int updateCommentCount(int id,int count){
        return newsDAO.updateCommentCount(id,count);
    }


}
