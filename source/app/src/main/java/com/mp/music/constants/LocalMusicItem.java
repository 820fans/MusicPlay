package com.mp.music.constants;

/**
 * Created by 张伟 on 2016-04-22.
 */
public class LocalMusicItem {

    private int id;
    private String path;
    private String singer="未知歌手";
    private String name;
    private String album;
    private String tag;

    public LocalMusicItem(String path,String name){
        this.path=path;
        this.name=name;
    }

    /**判断两元素相等，方法重写
     * 参考资料 http://blog.csdn.net/witsmakemen/article/details/7323604
     */
    public boolean equals(Object obj) {
        if (obj instanceof LocalMusicItem) {
            LocalMusicItem p = (LocalMusicItem) obj;
            return this.name.equals(p.name);
        }
        return super.equals(obj);
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
