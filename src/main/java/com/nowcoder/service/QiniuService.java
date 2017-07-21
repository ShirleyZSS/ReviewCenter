package com.nowcoder.service;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.util.ToutiaoUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;


/**
 * Created by Shirley on 2017/7/17.
 */
@Service
public class QiniuService {
    private static final Logger logger=LoggerFactory.getLogger(QiniuService.class);
    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = "l47HbsqTeimL-FXB_TnzDQEJW8So74dh8lIAxJsB";
    String SECRET_KEY = "z0CWlRjIhIF5DIFgql0KDU2dhcn5nTTSvfALnN7p";
    //要上传的空间
    String bucketname = "nowcoder";
  //  String key = "file key";
    //密钥配置
    Auth auth=Auth.create(ACCESS_KEY,SECRET_KEY);
    //创建上传对象
    UploadManager uploadManager=new UploadManager();

    public String getUpToken(){
        return auth.uploadToken(bucketname);
    }


    public String saveImage (MultipartFile file)throws IOException{
        try {
            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if (dotPos < 0) {
                return null;
            }
            String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
            if (!ToutiaoUtil.isFileAllowed(fileExt)) {
                return null;
            }

            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
            //调用put方法上传
            Response res = uploadManager.put(file.getBytes(),fileName,getUpToken());//返回的是一个json串{“hash”:xxx,"key":xxxx}
            //打印返回的信息
            System.out.println(res.toString());
            if (res.isOK() && res.isJson()) {
                return ToutiaoUtil.QINIU_DOMAIN_PREFIX  + JSONObject.parseObject(res.bodyString()).get("key").toString();//将key解析出来
            } else {
                logger.error("七牛异常:" + res.bodyString());
                return null;
            }
        } catch (QiniuException e) {
            // 请求失败时打印的异常的信息
            e.printStackTrace();
            return null;
        }

    }






}
