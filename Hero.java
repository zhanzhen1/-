package com.sanguo;

import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * 游戏的主jio玩家类
 *
 */
public class Hero {
    // 全局变量(成员变量)
    BufferedImage img ;//玩家图片
    int x;//坐标
    int y;
    //大小
    int w;
    int h;
    // 移动速度
    int speed = 10;
    //血量
    int hp = 100;

    //是否死亡
    Boolean isDeath;

    //移动方向
    String dir = "R";
    //创建一个定时器对象
    Timer timer = new Timer();
    //声明游戏主面板
    GamePanel panel;

    //初始玩家的基本消息
    public  Hero(GamePanel panel){
        this.panel = panel;
        //读取玩家图片
        img  = Imgutils.loadImg("h_R0.png");
        //获取图片大小
        w = img.getWidth();
        h = img.getHeight();
        //设置玩家的初始位置
        y=Imgutils.HEIGHT/2;
        //创建定时任务
        MoveTaske task = new MoveTaske();
        //启动定时任务(立即启动任务，每隔0.01s)
        timer.schedule(task,0,100);
    }
    // 全局内部类 定时改变玩家图片
    private  class  MoveTaske extends TimerTask{
        //图片索引
        int index;
        @Override
        public void run() {

            img  = Imgutils.loadImg("h_"+ dir +(index++ %8)+".png");

        }
    }

    /**
     * 控制玩家的行为方法：
     * 移动
     * 攻击
     * 技能释放
     * @param code
     */
    public void  control(int code){
//        上w 87 38  左a 65 37  下s 83 40  右d 68 39
        switch (code){
            case 87: case 38:  //w
                y-=speed;
                // 防止上移出界
                if (y<Imgutils.HEIGHT/2){
                    y=Imgutils.HEIGHT/2;
                }
                break;
            case 83: case 40: // s
                y+=speed;
                // 防止下移出界
                if (y>Imgutils.HEIGHT-h*2){
                    y = Imgutils.HEIGHT-h*2;
                }
                break;
            case 65:case 37:  //a
                dir = "L";
                x -=speed;
                //左边出界时
                if (x <-w){
                    x = Imgutils.WIDTH;
                    panel.prevMap();
                }
                break;
            case 68: case 39:  //d
                dir = "R";
                x+=speed;
                //左边出界时
                if (x >Imgutils.WIDTH){
                    x = -w;
                    panel.nextMap();
                }
                break;
            case 74:  //j
                //发起攻击
                attack();
                break;
            case 75: //k
                boomTask();
                break;
        }
    }
    //技能
    private void boomTask() {
        timer.cancel();
        Timer timer = new Timer();
        timer.schedule(new BoomTask(),0);

        //遍历所有敌人，是否与玩家碰撞
        panel.list.forEach(e->{
            //判断碰撞
            if (Imgutils.isHit(x,y,w,h,e.x,e.y,e.w,e.h)){
                //减少敌人血量
                e.hp-= Imgutils.randomInt(101,101);
            }
            //判断敌人是否死亡
            if (e.hp < 0){
                e.isDeath=true;
                Imgutils.playMusic("dea.mp3",false);
            }

        });
    }
    // 攻击实现
    private void attack() {
        // 停止移动动画
        timer.cancel();
        //启动攻击动画
        Timer timer = new Timer();
        timer.schedule(new AttackTask(),0);

        Imgutils.playMusic("attack.mp3",false);

        //遍历所有敌人，是否与玩家碰撞
        panel.list.forEach(e->{
            //判断碰撞
            if (Imgutils.isHit(x,y,w,h,e.x,e.y,e.w,e.h)){
                //减少敌人血量
                e.hp-= Imgutils.randomInt(10,30);
            }
            //判断敌人是否死亡
            if (e.hp < 0){
                e.isDeath=true;
                Imgutils.playMusic("dea.mp3",false);
            }

        });
    }

    // 实现攻击动画
    private  class  AttackTask extends TimerTask{
        int index;
        @Override
        public void run() {
            //循环切换攻击图片
            while (index<=5){
                //修改图片
                img = Imgutils.loadImg("attack/a1-"+dir+(index++)+".png");
                //休眠0.1s
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //停止攻击定时任务
            timer.cancel();
            //启动移动定时任务
            timer = new Timer();
            MoveTaske task = new MoveTaske();
            timer.schedule(task,0,100);
        }
    }
    private  class  BoomTask extends TimerTask{

        @Override
        public void run() {
            img = Imgutils.loadImg("boom.gif");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timer.cancel();
            timer = new Timer();
            MoveTaske taske = new MoveTaske();
            timer.schedule(taske,0,100);
        }
    }
}