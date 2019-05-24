package com.bjsxt.music.view;


import com.bjsxt.music.project.ServerPath;
import com.bjsxt.music.project.User;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class TestMusic {
    boolean isLogin = false;

    public static void main(String[] args) {
        TestMusic tm = new TestMusic();
        tm.enterMainMenu();
    }

    /**
     * 主菜单界面
     */
    public void enterMainMenu() {
        System.out.println("----------------欢迎来到音乐上传网站----------------");
        while (true) {
            System.out.println("-----------1、登录-----------");
            System.out.println("-----------2、上传音乐-----------");
            System.out.println("-----------3、退出-----------");
            System.out.println("请输入您的选择（1-3）：");
            char choose = MusicUtils.readMenuSelection();
            switch (choose) {
                case '1':
                    login();
                    break;
                case '2':
                    upload();
                    break;
                case '3':
                    System.out.println("您确认退出吗？（Y/N）");
                    char yn = MusicUtils.readConfirmSelection();
                    if (yn == 'Y') {
                        return;
                    }
                    break;
            }
        }
    }

    /**
     * 登录
     */
    private void login() {
        System.out.println("----------------登录-----------------");
        System.out.println("请输入您的用户名：");
        String username = MusicUtils.readString(6);
        System.out.println("请输入您的密码");
        String password = MusicUtils.readString(12);
        // 封装用户名和密码
        User use = new User(username, password);
        // 创建客户端
        try {
            Socket socket = new Socket("localhost", 7310);
            // 创建对象输出流发送到服务端
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            // 写出数据
            oos.writeObject(use);
            oos.flush();
            // 读取数据
            String str = dis.readUTF();
            // 读取服务端反馈的数据
            if ("登录成功".equals(str)) {
                System.out.println("登录成功！");
                isLogin = true;
            } else {
                System.out.println("登录失败！重新进入登录");
                isLogin = false;
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传
     */
    private void upload() {
        if (!isLogin) {
            System.out.println("请登录后再进行上传");
            return;
        }
        System.out.println("-----------上传音乐-----------");
        int index = 1;//用index定义歌曲的编号
        System.out.println("这是您的歌单:");
//        File file = new File("F:\\MyMusic");
        //返回某个目录下所有文件和目录的绝对路径,listFiles()返回一个File的数组
        File[] files = new File("F:\\MyMusic").listFiles();
        // 定义map集合
        Map<Integer, String> map = new HashMap<>();
        int key = 1;
        for (File f : files) {
            // 筛选数组中符合要求的音乐文件.mp3
            if (f.getName().endsWith(".mp3")) {
                System.out.println(index++ + "\t" + f.getName());
                //将文件放入map集合中
                map.put(key++, f.getName());
            }
        }
        // 上传音乐到服务器
        System.out.println("请输入您要上传的歌曲编号（歌单前面的数字）：");
        int id = MusicUtils.readInt();
        //通过ID获取音乐的编号
        String mcName = map.get(id);
        for (int i = 0; i < files.length; i++) {
            //判断是否为需要的上传音乐
            if (mcName.equals(files[i].getName())) {
                System.out.println("请输入您上传的地址（文件夹地址:如（C:/Windows））");
                String serverPath = MusicUtils.readString(16);
                //当指定路径获得上传的文件后，反馈上传成功则循环终止
                sendDataToSerever(files[i].getAbsolutePath(), serverPath + "\\" + mcName);
                break;
            }
        }
    }

    /**
     * 将数据发送到服务器端
     */
    private void sendDataToSerever(String absolutePath, String serverpath) {

        //创建客户端Socket
        ServerPath serverPath = new ServerPath(serverpath);
        Socket socket;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        DataInputStream dis = null;
        ObjectOutputStream oos = null;
        try {
            socket = new Socket("localhost", 9527);
            //absolutePath 歌曲本地地址
            bis = new BufferedInputStream(new FileInputStream(absolutePath));
            bos = new BufferedOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());

            //将客户端的地址传给服务器端
            oos = new ObjectOutputStream(socket.getOutputStream());
            //serverpath 歌曲指定地址
            oos.writeObject(serverPath);
            oos.flush();
            //读写数据音乐文件
            byte[] bys = new byte[1024];
            int len;
            while ((len = bis.read(bys)) != -1) {
                bos.write(bys, 0, len);
            }
            bos.flush();
            //保持Socket不关闭的前提下，关闭流
            socket.shutdownOutput();
            //接收服务器反馈客户端的信息
            String str = dis.readUTF();
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭流
            MusicUtils.closeTo(oos);
            MusicUtils.closeTo(bis);
            MusicUtils.closeTo(bos);
            MusicUtils.closeTo(dis);
        }
    }
}
