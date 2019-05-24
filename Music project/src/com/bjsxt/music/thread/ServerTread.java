package com.bjsxt.music.thread;

import com.bjsxt.music.project.ServerPath;
import com.bjsxt.music.view.MusicUtils;

import java.io.*;
import java.net.Socket;

public class ServerTread extends Thread {

    private Socket socket;

    public ServerTread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedInputStream bis = null;
        ObjectInputStream ois = null;
        DataOutputStream dos = null;
        BufferedOutputStream bos = null;
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            bis = new BufferedInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            //获取歌曲上传路径
            ServerPath serverpath = (ServerPath) ois.readObject();
            File file = new File(serverpath.getServerPath());
            //如果要上传到文件夹不存在，就创建新的文件夹
            if (!file.getParentFile().exists()) {
                System.out.println("该路径不存在，已经创建");
                file.getParentFile().mkdirs(); //创建文件夹
            }
            bos = new BufferedOutputStream(new FileOutputStream(file));
            //将音乐文件上传到指定的位置
            byte[] buf = new byte[1024];
            int len;
            while ((len = bis.read(buf)) != -1) {
                bos.write(buf, 0, len);
            }
            bos.flush();
            System.out.println("数据读写完成");
            //给用户反馈是否上传成功
            dos.writeUTF("上传成功！");
        } catch (IOException e) {
            try {
                dos.writeUTF("上传失败！");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            //关闭流
            MusicUtils.closeTo(bos);
            MusicUtils.closeTo(dos);
            MusicUtils.closeTo(ois);
            MusicUtils.closeTo(bis);
        }
    }
}
