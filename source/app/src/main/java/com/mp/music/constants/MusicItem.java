package com.mp.music.constants;

/**
 * Created by 张伟 on 2016-04-04.
 */
public class MusicItem {

    private int id;

    private String name;

    private String cover;

    private String path;

    private String singer;

    private String type;

    public MusicItem(int id,String name,String cover,String path,String singer,String type){
        this.id=id;
        this.name=name;
        this.cover=cover;
        this.path= path;
        this.singer=singer;
        this.type=type;
    }


    /**判断两元素相等，方法重写
     * 参考资料 http://blog.csdn.net/witsmakemen/article/details/7323604
     */
    public boolean equals(Object obj) {
        if (obj instanceof MusicItem) {
            MusicItem p = (MusicItem) obj;
            return this.id==p.id;
        }
        return super.equals(obj);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCover() {
        return cover;
    }

    public String getPath() {
        return path;
    }

    public String getSinger() {
        return singer;
    }

    public String getType() {
        return type;
    }
}
