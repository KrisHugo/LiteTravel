package com.LiteTravel.controller;

import com.LiteTravel.file.FastDFSFile;
import com.LiteTravel.util.FastDFSUtil;
import entity.Result;
import entity.StatusCode;
import org.csource.common.MyException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/upload")
@CrossOrigin
public class fileController {
    /*
    * 文件上传请求
    * */
    @PostMapping
    public Result upload(@RequestParam(value = "file")MultipartFile file) throws Exception {
        // 封装文件
        FastDFSFile fastDFSFile = new FastDFSFile(
                file.getOriginalFilename(),
                file.getBytes(),
                StringUtils.getFilenameExtension(file.getOriginalFilename())
        );

        // 用Util将文件传输到FastDFS中, 并且获取存储位置信息, 可用于保存至数据库
        String[] result = FastDFSUtil.upload(fastDFSFile);
        // 拼接成请求url
        String storeUrl = "http://192.168.0.1:8080/" + result[0] + "/" + result[1];
        return new Result(true, StatusCode.OK, "上传成功!", storeUrl);
    }
}
