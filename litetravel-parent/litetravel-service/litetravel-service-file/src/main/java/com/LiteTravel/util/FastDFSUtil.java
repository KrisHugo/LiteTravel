package com.LiteTravel.util;

/*
* 实现FastDFS的文件管理
*   文件上传
*   文件下载
*   文件信息获取
*   文件删除
*   Storage信息获取
*   Tracker信息获取
* */

import com.LiteTravel.file.FastDFSFile;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;

public class FastDFSUtil {

    /*
    * 加载Tracker连接信息
    * */
    static{
        String path = new ClassPathResource("fdfs_client.conf").getPath();
        try {
            ClientGlobal.init(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
    * 文件上传
    * */
    public static String[] upload(FastDFSFile fastDFSFile) throws Exception {
        /*
        * 写入附加信息
        * */
        NameValuePair[] meta_list = new NameValuePair[1];
        meta_list[0] = new NameValuePair("author", fastDFSFile.getAuthor());

        //创建一个Tracker访问的客户端对象TrackerClient
        TrackerClient trackerClient = new TrackerClient();
        //通过TrackerClient访问TrackerServer服务, 获取连接信息
        TrackerServer trackerServer = trackerClient.getConnection();
        //通过TrackerServer的链接信息可以获取Storage的链接信息, 创建StorageClient对象存储Storage的存储信息
        StorageClient storageClient = new StorageClient(trackerServer, null);
        /*
        * 通过StorageClient访问Storage, 实现文件上传, 并且获取文件上传后的存储信息
        * answer 参数解释
        * 1. 上传文件的字节数组
        * 2. 文件的拓展名
        * 3. 附加参数
        *
        * answer 返回值
        * uploads[0] 文件上传所储存的Storage的组的名字 -> group1
        * uploads[1] 文件储存到Storage上的文件名字 -> M00/02/44/test.jpg
        * */
        String[] uploads = storageClient.upload_file(fastDFSFile.getContent(), fastDFSFile.getExt(), meta_list);
        return uploads;
    }

}
