package com.sanguo;

/*
工具类
图片读取
随机数生成
*/

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

public class Imgutils {
    //获取屏幕宽度
    public static int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    //获取屏幕高度
    public static int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    /**
     * @param name
     * @return 读取指定名称的图片为一个BufferedImage对象
     */
    public static BufferedImage loadImg(String name) {

        try {
            URL url = Imgutils.class.getResource("/img/" + name);
            //将url读取为一个图片对象
            return ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param min
     * @param max
     * @return 返回【min，max】之间的随机整数
     */
    public  static  int  randomInt(int min , int max){
        Random r = new Random();
        //
        return r.nextInt(max - min + 1) + min;
    }

    /**
     * 播放音效
     * @param name 音效名
     * @param isLoop 是否循环播放
     */
    public  static  void playMusic(String name,boolean isLoop){
        //启动线程播放音效
        new Thread(){
            @Override
            public void run() {
                do {
                    try {
                        //加载指定的资源为一个输入流（IO）
                        InputStream is =  Imgutils.class.getResourceAsStream("/music/"+name);
                        //基于流创建播放器
                        Player p = new Player(is);
                        p.play();
                    } catch (JavaLayerException e) {
                        e.printStackTrace();
                    }
                }while (isLoop);
            }
        }.start();
    }

    /**
     * 碰撞检测方法，根据提供的构造矩形的参数，判断两个矩形是否碰撞
     * @param x1
     * @param y1
     * @param w1
     * @param h1
     * @param x2
     * @param y2
     * @param w2
     * @param h2
     * @return 如果碰撞返沪true ，否则false
     */
    public  static  boolean isHit(int x1,int y1,int w1,int h1, int x2, int y2,int w2,int h2){
        //根据提供的参数构造两个矩形对象
        Rectangle r1 = new Rectangle(x1,y1,w1,h1);
        Rectangle r2 = new Rectangle(x2,y2,w2,h2);
        return r1.intersects(r2);
    }
}
