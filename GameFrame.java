package com.sanguo;

// 窗体 显示游戏面板

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameFrame extends JFrame {

    public  GameFrame(){
        this.setTitle("三国战纪");
        //获取全屏
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        //禁止改变窗口大小
        setResizable(false);
        //窗口关闭时，（不做任何操作）
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        //窗口关闭事件(监听器)
        addWindowListener(new WindowAdapter() {
            //监听窗口关闭时

            @Override
            public void windowClosing(WindowEvent e) {
                // 显示一个对话框
                int  i = JOptionPane.showConfirmDialog(GameFrame.this,"确认关闭？",
                        "关闭提醒",JOptionPane.YES_NO_OPTION);
                System.out.println(i);
                if (i == 0 ) {
                    //关闭游戏
                    System.exit(0);
                }
            }
        });
        add(new GamePanel());
    }
}
