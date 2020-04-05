package com.LiteTravel.File.dao;
/*
* 封装文件上传信息
* 信息数据：
*   时间:
*   Author:
*   Type:
*   Size:
*   附加信息:
*   后缀:
*   文件内容->文件的字节数组
*  */

import lombok.Data;

import java.io.Serializable;

@Data
public class FastDFSFile implements Serializable {
    // 文件名
    private String name;
    // 文件内容
    private byte[] content;
    // 文件拓展名
    private String ext;
    // 文件MD5摘要值
    private String md5;
    // 文件作者
    private String author;

    public FastDFSFile(String name, byte[] content, String ext) {
        this.name = name;
        this.content = content;
        this.ext = ext;
    }
}

