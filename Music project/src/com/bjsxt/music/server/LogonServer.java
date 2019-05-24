package com.bjsxt.music.server;

import com.bjsxt.music.project.User;
import com.bjsxt.music.thread.ServerTread;
import com.bjsxt.music.view.MusicUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 登录到服务端
 */
public class LogonServer {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("*************服务器端*************");
        //new一个服务端对象
        ServerSocket ss = new ServerSocket(7310);

        while (true) {
            Socket socket = ss.accept();
            //ObjectInputStream对象流用于拿到客户端传递的对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //DataOutputStream获取输出流用于反馈用户登录是否成功
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            Object object = ois.readObject();
            User u = (User) object;
            String str = null;//定义变量接收登录结果
            System.out.println(u.getUsername().equals("wbq") && u.getPassword().equals("123123")? (str = "登录成功"): (str = "登录失败"));
            //反馈数据到客户端
            dos.writeUTF(str);
            dos.flush();

            //关闭流
            MusicUtils.closeTo(ois);
            MusicUtils.closeTo(dos);
        }
    }
}

class ServerUpLoad {
    public static void main(String[] args) throws Exception {
        System.out.println("*************上传文件至服务器*************");
        ServerSocket ss = new ServerSocket(9527);
        while (true) {
            try {
                Socket socket = ss.accept();
                new ServerTread(socket).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
