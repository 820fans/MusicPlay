package com.mp.music.entity;

/**
 * Created by 张伟 on 2016-04-06.
 */

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;

import java.io.IOException;

public class Player implements OnBufferingUpdateListener, OnCompletionListener,
        OnPreparedListener {

    public MediaPlayer mediaPlayer ; // 媒体播放器

    public int bufferProcessPercent; //流媒体情况下，从网络加载的进度
    public boolean isPlaying=false;

//    private Timer mTimer = new Timer(); // 计时器

    // 初始化播放器
    public Player() {
        super();
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setScreenOnWhilePlaying(true); //设置手机屏幕常亮
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); // 设置媒体流类型
            mediaPlayer.setOnBufferingUpdateListener(this); //网络请求的情况下，从网络加载的进度
            mediaPlayer.setOnPreparedListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 计时器
//    TimerTask timerTask = new TimerTask() {
//
//        @Override
//        public void run() {
//            if (mediaPlayer == null)
//                return;
//            if (mediaPlayer.isPlaying() && !OnLinePlayService.isChanging) {
//                handler.sendEmptyMessage(0); // 发送消息
//            }
//        }
//    };

//    Handler handler = new Handler() {
//
//        public void handleMessage(android.os.Section msg) {
//            int position = mediaPlayer.getCurrentPosition();
//            int duration = mediaPlayer.getDuration();
//            if (duration > 0) {
//                // 计算进度（获取进度条最大刻度*当前音乐播放位置 / 当前音乐时长）
////                long pos = seekBar.getMax() * position / duration;
////                OnLinePlayService.sendProcesstoActivity(position,duration);
//            }
//        };
//    };

    public void play() {
        mediaPlayer.start();
    }

    /**
     *
     * @param url
     *            url地址
     */
    public void playUrl(String url) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url); // 设置数据源
//            mediaPlayer.prepare(); // prepare自动播放
            mediaPlayer.prepareAsync(); //异步播放
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 暂停
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlaying = false;
        }
    }

    // 停止
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
        Log.e("mediaPlayer", "onPrepared");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.e("mediaPlayer", "onCompletion");
    }

    /**
     * 缓冲更新
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        bufferProcessPercent=percent;
//        int currentProgress = seekBar.getMax()
//                * mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
//        Log.e(currentProgress + "% play", percent + " buffer");
    }

}
