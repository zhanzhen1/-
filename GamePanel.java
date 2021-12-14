package com.sanguo;

/*
 * 游戏的主面板
 * 游戏的各种元素都在这个主面板中显示;
 * 地图、玩家、敌人、道具
 * */
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.TimerTask;

public class GamePanel extends JPanel {
    /**
     * 创建玩家对象
     */
    Hero h = new  Hero(this);
    // 标记是否开始游戏
    boolean starting = true;
    // 声明一个地图索引
    int mapIndex;
    //声明一个集合，存储所有产生的敌人
    ArrayList<Enemy> list = new ArrayList<>();

    //定时器 定时去生成敌人
    java.util.Timer timer = new java.util.Timer();
    private  class  EnemyTask extends TimerTask{

        @Override
        public void run() {
            while (starting){
                //创建敌人
                Enemy e = new Enemy(GamePanel.this);
                //启动敌人的运行内存
                e.start();
                //将产生敌人放入集合
                list.add(e);
                try {
                    //随机1s到3s之内制造敌人
                    Thread.sleep(Imgutils.randomInt(1000,3000));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public GamePanel(){
        //启动并启动刷新界面的线程
        new Thread(){
            @Override
            public void run() {
                //当游戏正常运行时，不断刷新
                while (starting){
                    //界面刷新
                    repaint();
                    try {
                        //每隔0.01s刷新一次界面
                        sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        // 设置当前的面板，获取焦点
        setFocusable(true);
        //为面板绑定事件
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                h.control(code);
            }
        });
        // 启动定时任务 5s之后制造敌人
        timer.schedule(new EnemyTask(),5000);

        //播放背景音乐
        Imgutils.playMusic("稻香.mp3",starting);
    }
    //切换上一张地图
    public  void prevMap(){
        clearEnemy();
        mapIndex--;
        if (mapIndex < 0){
            mapIndex = 8;
        }
    }
    //切换下一张地图
    public  void  nextMap(){
        clearEnemy();
        mapIndex++;
        if (mapIndex > 8){
            mapIndex = 0;
        }
    }
    //清理敌人 标记所有敌人isdath=true
    private  void clearEnemy(){
        list.forEach(e->e.isDeath=true);
    }

    /**
     * 用于绘制各种组件的方法
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //根据地图的索引，更改后缀
        String suffix = mapIndex < 4 ? ".jpg":".png";
        // 获取背景图片
        BufferedImage img = Imgutils.loadImg("bg"+mapIndex + suffix);
        // 绘制地图到面板中
        g.drawImage(img,0,0,Imgutils.WIDTH,Imgutils.HEIGHT,null);
        //绘制玩家
        g.drawImage(h.img,h.x,h.y,h.w,h.h,null);
        g.drawRect(h.x,h.y-10,h.w,5);
        g.fillRect(h.x,h.y-10,(int) (h.hp/100.0*h.w),5);
        //绘制敌人 java8的新特
        list.forEach(e->{
            //绘制一个矩形 血槽
            g.drawRect(e.x,e.y-10,e.w,5);
            //实际血量
            g.fillRect(e.x,e.y-10,(int) (e.hp/100.0*e.w),5);
            g.drawImage(e.img,e.x,e.y,e.w,e.h,null);
        });
    }
}
