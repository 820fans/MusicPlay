package com.mp.music.service;

/**
 * Created by 张伟 on 2016-04-06.
 */

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.mp.music.entity.Constants;
import com.mp.music.entity.Player;
import com.mp.music.fragment.HomeFragment;

import java.util.Timer;
import java.util.TimerTask;
/**
 * Describe:<br>
 * <br>负责音乐播放的后台Service
 * <br>1.通过接收来自前台的控制播放信息的广播来播放指定音乐
 * <br>2.加载网络或者文件目录下的音乐
 * <br>3.为mediaPlayer的完成事件创建监听器当当前音乐播放
 *
 * <br>完成后自动播放下一首音乐 ==>> 网络音乐只提供试听
 *
 * <br>4.为每一首正在播放的音乐创建一个定时器，用于检测播放进度
 * <br>并更新进度条
 * <br>@author yida
 * <br>Date：2016.04
 * */
public class OnLinePlayService extends Service {

    //定时器
    Timer mTimer;
    TimerTask mTimerTask;
    //记录Timer运行状态
    boolean isTimerRunning=false;
    public static boolean isChanging=false;//互斥变量，防止定时器与SeekBar拖动时进度冲突

    //播放器
    public static Player player = new Player();

    //广播接收器
    public MusicSercieReceiver receiver=new MusicSercieReceiver();

    //当前的播放的音乐
    public static int current=0;
    //当前播放状态
    public static int state=Constants.STATE_NON;
    //当前播放的模式
    public static int mode=Constants.LOCAL_MODE;


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        System.out.println("音乐服务启动");
        //注册广播接收
        IntentFilter filter=new IntentFilter();
        filter.addAction(Constants.MUSICSERVICE_ACTION);
        registerReceiver(receiver, filter);

        //为mediaPlayer的完成事件创建监听器
        player.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {

                if(getPlayerCurrentPosition()>=200){
                    mTimer.cancel();//取消定时器
                    current++;
                    prepareAndPlay(current);
                }
            }
        });
    }

    public static int getPlayerDuration(){
        return player.mediaPlayer.getDuration();
    }

    public static int getPlayerCurrentPosition(){
        return player.mediaPlayer.getCurrentPosition();
    }

    /**
     * 装载和播放音乐
     * @param index int index 播放第几首音乐的索引
     * */
    protected void prepareAndPlay(int index) {
        // TODO Auto-generated method stub
        if (isTimerRunning) {//如果Timer正在运行
            mTimer.cancel();//取消定时器
            isTimerRunning=false;
        }
        if (index>= HomeFragment.playlist.size()) {
            current=index=0;
        }
        if (index<0) {
            current=index=HomeFragment.playlist.size()-1;
        }

        if(current>=HomeFragment.playlist.size()){

            //发送广播通知前台Activity更新界面
            Intent intent=new Intent();
            intent.putExtra("current", -4);
            intent.setAction(Constants.MUSICBOX_ACTION);
            sendBroadcast(intent);
            state=Constants.STATE_STOP;
            return;
        }
        else{
            //发送广播通知前台Activity更新界面
            Intent intent=new Intent();
            intent.putExtra("current", index);
            intent.setAction(Constants.MUSICBOX_ACTION);
            sendBroadcast(intent);
        }

        //播放某一个音频文件
        System.out.println(HomeFragment.playlist.get(current).getUrl());
        player.playUrl(HomeFragment.playlist.get(current).getUrl());

        startTimer();
    }

    public void stopTimer(){
        isTimerRunning=false;
        mTimer.cancel();
        mTimerTask.cancel();
    }

    public void startTimer(){

        //----------定时器记录播放进度---------//
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                isTimerRunning=true;
                if(isChanging)//当用户正在拖动进度进度条时不处理进度条的的进度
                    return;

                if(state==Constants.STATE_PLAY) {
                    int position = getPlayerCurrentPosition();
                    int duration = getPlayerDuration();
                    if (duration > 0) {

                        //发送广播通知前台Activity更新进度条
                        Intent intent = new Intent();
                        intent.putExtra("current", -3);

                        //  当前音乐播放位置 / 当前音乐时长
                        intent.putExtra("position",current);
                        intent.putExtra("bufferPercent",player.bufferProcessPercent);
                        intent.putExtra("percent", (float) (position / (duration*1.0)));
                        intent.setAction(Constants.MUSICBOX_ACTION);
                        sendBroadcast(intent);
                    }
                }
                else if(state==Constants.STATE_PAUSE){

                    //发送广播通知前台Activity更新界面到暂停
                    Intent s = new Intent();
                    s.putExtra("current", -2);
                    s.setAction(Constants.MUSICBOX_ACTION);
                    sendBroadcast(s);
                }

            }
        };

        //每隔10毫秒检测一下播放进度
        mTimer.schedule(mTimerTask, 0, 10);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    //创建广播接收器用于接收前台Activity发来的广播
    class MusicSercieReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            int control=intent.getIntExtra("control", -1);
            switch (control) {
                case Constants.STATE_PLAY://播放音乐

                    if (state==Constants.STATE_PAUSE) {//如果原来状态是暂停
                        player.play();
                        startTimer();
                    }else if (state!=Constants.STATE_PLAY) {
                        prepareAndPlay(current);
                    }
                    state=Constants.STATE_PLAY;
                    break;
                case Constants.STATE_PAUSE://暂停播放
                    if (state==Constants.STATE_PLAY) {

                        stopTimer();
                        player.mediaPlayer.pause();
                        state=Constants.STATE_PAUSE;

                        //发送5个广播通知前台Activity更新界面到暂停
                        for(int i=0;i<5;i++) {
                            Intent s = new Intent();
                            s.putExtra("current", -2);
                            s.setAction(Constants.MUSICBOX_ACTION);
                            sendBroadcast(s);
                        }
                    }
                    break;
                case Constants.STATE_STOP://停止播放
                    if (state==Constants.STATE_PLAY||state==Constants.STATE_PAUSE) {
                        player.mediaPlayer.stop();
                        state=Constants.STATE_STOP;
                        mTimer.cancel();
                        mTimerTask.cancel();

                        //发送广播通知前台Activity更新界面
                        Intent s=new Intent();
                        intent.putExtra("current", -5);
                        intent.setAction(Constants.MUSICBOX_ACTION);
                        sendBroadcast(s);

                    }
                    break;
                case Constants.STATE_PREVIOUS://上一首
                    prepareAndPlay(--current);
                    state=Constants.STATE_PLAY;
                    break;
                case Constants.STATE_NEXT://下一首
                    prepareAndPlay(++current);
                    state=Constants.STATE_PLAY;
                    break;
                case Constants.PLAY_PROCESS://播放进度
                    int process=intent.getIntExtra("process", 0);
                    player.mediaPlayer.seekTo(process);
                    isChanging=false;
                    break;
                case Constants.STATE_CHECK://检查是否播放

                    //正在播放音乐
                    if(OnLinePlayService.state == Constants.STATE_PLAY ||
                            OnLinePlayService.state == Constants.STATE_PAUSE){

                        int position = getPlayerCurrentPosition();
                        int duration = getPlayerDuration();

                        System.out.println("成功检查"+current);

                        //发送广播通知前台Activity更新进度条
                        Intent u = new Intent();
                        intent.putExtra("current", current);
                        intent.putExtra("position", position);
                        intent.putExtra("duration", duration);
                        intent.setAction(Constants.MUSICBOX_ACTION);
                        sendBroadcast(u);

                    }

                default:
                    break;
            }
        }
    }
}