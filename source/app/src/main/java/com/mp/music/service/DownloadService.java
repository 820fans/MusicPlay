package com.mp.music.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.IBinder;

import com.mp.music.entity.Constants;
import com.mp.music.file.DownloadProgressListener;
import com.mp.music.file.FileDownloader;
import com.mp.music.fragment.HomeFragment;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by 张伟 on 2016-04-29.
 */
public class DownloadService extends Service {

    private DownloadTask task;
    File savMusicDir ;

    //广播接收器
    public DownloadSercieReceiver receiver=new DownloadSercieReceiver();
    private ArrayList<String> downloadPaths=new ArrayList<>();

    //当前播放状态
    public static int state=Constants.STATE_NON;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //注册广播接收
        IntentFilter filter=new IntentFilter();
        filter.addAction(Constants.DOWNSERVICE_ACTION);
        registerReceiver(receiver, filter);
        System.out.println("下载服务启动");

        savMusicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);

    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void exit() {
        if (task != null)
            task.exit();
    }

    private void DownloadMusic(String path, File savDir) {
        task = new DownloadTask(path, savDir);
        new Thread(task).start();
    }

    private final class DownloadTask implements Runnable {
        private String path;
        private File saveDir;
        private FileDownloader loader;

        public DownloadTask(String path, File saveDir) {
            this.path = path;
            this.saveDir = saveDir;
        }

        public void exit() {
            if (loader != null)
                loader.exit();
        }

        DownloadProgressListener downloadProgressListener = new DownloadProgressListener() {
            @Override
            public void onDownloadSize(int size) {

                //发送广播通知前台Activity更新界面
                Intent intent=new Intent();
                intent.putExtra("action", Constants.DOWN_PROCESS);
                intent.putExtra("size",size);

                int position=-1;
                for(int i=0;i<HomeFragment.onlinelist.size();i++){
                    if(path.equals(Constants.SERVER_URL+HomeFragment.onlinelist.get(i).getPath())){
                        position=i;
                        break;
                    }
                }

                intent.putExtra("position",position);
                intent.setAction(Constants.DOWNBOX_ACTION);
                sendBroadcast(intent);
                state=Constants.DOWN_PROCESS;
            }
        };

        public void run() {
            try {

                int position=-1;
                for(int i=0;i<HomeFragment.onlinelist.size();i++){
                    if(path.equals(Constants.SERVER_URL+HomeFragment.onlinelist.get(i).getPath())){
                        position=i;
                        break;
                    }
                }

                loader = new FileDownloader(DownloadService.this, path,
                        saveDir, 3 ,HomeFragment.onlinelist.get(position).getName()+".mp3");

                //发送广播通知前台Activity更新界面
                Intent intent=new Intent();
                intent.setAction(Constants.DOWNBOX_ACTION);
                intent.putExtra("action", Constants.DOWN_SIZE);
                intent.putExtra("size",loader.getFileSize());

                intent.putExtra("position", position);
                sendBroadcast(intent);
                state=Constants.DOWN_PROCESS;

                loader.download(downloadProgressListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //创建广播接收器用于接收前台Activity发来的广播
    class DownloadSercieReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            int action=intent.getIntExtra("action", -1);
            switch (action){
                case Constants.DOWN_START:
                    int position=intent.getIntExtra("position", -1);
                    String url= Constants.SERVER_URL+HomeFragment.onlinelist.get(position).getPath();
                    System.out.println("收到请求"+url);
                    DownloadMusic(url, savMusicDir);
                    break;
            }

        }
    }
}
