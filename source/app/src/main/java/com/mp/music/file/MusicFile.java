package com.mp.music.file;

import com.mp.music.adapter.LocalMusicListAdapter;
import com.mp.music.constants.LocalMusicItem;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by 张伟 on 2016-04-22.
 */
public class MusicFile {

    // 搜索音乐文件
    public static void search(ArrayList<LocalMusicItem> list,File file, String[] ext,
                              LocalMusicListAdapter adapter) {
        if (file != null) {
            if (file.isDirectory()) {

                //深度优先搜索
                File[] listFile = file.listFiles();
                if (listFile != null) {
                    for (int i = 0; i < listFile.length; i++) {
                        search(list,listFile[i], ext,adapter);
                    }
                }
            } else {
                String filepath = file.getAbsolutePath();
                String filename=filepath.substring(filepath.lastIndexOf('/')+1);

                //匹配扩展名
                for (int i = 0; i < ext.length; i++) {
                    if (filepath.endsWith(ext[i])) {
                        LocalMusicItem localMusicItem=new LocalMusicItem(filepath,filename);
                        if(!list.contains(localMusicItem))
                        list.add(localMusicItem);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        }
    }

}
