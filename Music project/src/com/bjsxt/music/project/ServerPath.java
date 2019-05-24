package com.bjsxt.music.project;

import java.io.Serializable;

/**
 * 存储指定服务器的路径
 */
public class ServerPath implements Serializable {//实现可序列化接口
    //歌曲的路径
    private String serverPath;

    //无参构造器
    public ServerPath() {

    }

    public ServerPath(String serverPath) {
        super();
        this.serverPath = serverPath;
    }

    public String getServerPath() {
        return serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }
}
