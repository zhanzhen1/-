package com.sanguo;

import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 敌人类 线程
 */
public class Enemy extends  Thread{
    //用于渲染敌人的图片对象
    BufferedImage img;
    //坐标
    int x,y;
    //宽高
    int w,h;
    //血量
    int hp = 100;
    //移动速度
    int speed = 10;
    //是否死亡
    boolean isDeath=false;

    // 声明游戏主面板
    GamePanel panel;
    //创建定时器
    Timer timer = new Timer();
    //创建敌人的定时
    private class MoveTaske extends TimerTask {
        int index;
        @Override
        public void run() {
            img = Imgutils.loadImg("enemy/"+(index++ %16 + 1) +".png");
        }
    }

    public Enemy(GamePanel panel){
        this.panel = panel;
        //敌人图片
        img  = Imgutils.loadImg("enemy/1.png");
        //获取图片大小  自身宽高
        w = img.getWidth();
        h = img.getHeight();
        //初始化
        x = Imgutils.WIDTH;
        int min = Imgutils.HEIGHT / 2;
        int max = Imgutils.HEIGHT -h*2;
        //随机一个敌人出现的y轴位置
        y = Imgutils.randomInt(min,max);
        MoveTaske task = new MoveTaske();
        timer.schedule(task,0,100);
    }
    // 方法重写
    @Override
    public void run() {
        //当前敌人未死亡前，一直移动
        while (!isDeath){
            //让敌人从屏幕右侧往左移动
            x -=speed;
            //判断敌人是否走到最左侧
            if (x<-w){
                //标记敌人死亡
                isDeath = true;
            }
            try {
                //每隔0.1s修改位置
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        timer.cancel();
        //当循环不在执行，敌人以死亡
        panel.list.remove(this);
    }
}