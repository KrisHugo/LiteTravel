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
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

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
     * 代码复用, 获取TrackerServer
     * */
    private static TrackerServer getTrackServer() throws Exception{
        //创建一个Tracker访问的客户端对象TrackerClient
        TrackerClient trackerClient = new TrackerClient();
        //通过TrackerClient访问TrackerServer服务, 获取连接信息
        return trackerClient.getConnection();
    }
    /*
    * 代码复用, 获取StorageClient
    * */
    private static StorageClient getStorageClient(TrackerServer trackerServer) {
        //通过TrackerServer的链接信息可以获取Storage的链接信息, 创建StorageClient对象存储Storage的存储信息
        return new StorageClient(trackerServer, null);
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
        return getStorageClient(getTrackServer()).upload_file(fastDFSFile.getContent(), fastDFSFile.getExt(), meta_list);
    }

    /*
    * 获取文件信息
    * */
    public static FileInfo getFile(String groupName, String remoteFileName) throws Exception{
        return getStorageClient(getTrackServer()).get_file_info(groupName, remoteFileName);
    }

    /*
    * 下载文件
    * */
    public static InputStream downloadFile(String groupName, String remoteFileName) throws Exception{
        byte[] buffer = getStorageClient(getTrackServer()).download_file(groupName, remoteFileName);
        return new ByteArrayInputStream(buffer);
    }

    /*
    * 删除文件
    * */
    public static void deleteFile(String groupName, String remoteFileName) throws Exception{
        getStorageClient(getTrackServer()).delete_file(groupName, remoteFileName);
    }

    /*
    * 获取Tracker的信息 return http://ip:port
    * */
    public static String getTrackerInfo() throws Exception{
        //获取Tracker的Ip, Port
        String ip = getTrackServer().getInetSocketAddress().getHostName();
        int port = ClientGlobal.getG_tracker_http_port();
        return "http://" + ip + ":" + port;
    }

    /*
    * 获取Storage信息
    * 1. getStoragePathIndex() 存储服务器序列
    * 2. getInetSocketAddress.getHostString() 服务器ip
    * */
    public static StorageServer getStorage() throws Exception{
        //创建一个Tracker访问的客户端对象TrackerClient
        TrackerClient trackerClient = new TrackerClient();
        //通过TrackerClient访问TrackerServer服务, 获取连接信息
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerClient.getStoreStorage(trackerServer);
    }

    /*
    * 获取当前文件所属于的Storage的Ip和端口信息
    * */
    public static StorageServer getStorageInfo(String groupName, String remoteFileName) throws Exception {
        //创建一个Tracker访问的客户端对象TrackerClient
        TrackerClient trackerClient = new TrackerClient();
        //通过TrackerClient访问TrackerServer服务, 获取连接信息
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerClient.getFetchStorage(trackerServer, groupName, remoteFileName);
    }


}
