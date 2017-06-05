package com.mp.music.constants;

/**
 * Created by 张伟 on 2016-04-22.
 */
public class PlayItem {

    private int id;
    private String cover="";
    private String name;
    private String singer;
    private String url;
    private boolean isLocal=false;

    public PlayItem(){

    }
    public PlayItem (int id,String name,String singer){
        this.id=id;
        this.name=name;
        this.singer=singer;
    }

    /**判断两元素相等，方法重写
     * 参考资料 http://blog.csdn.net/witsmakemen/article/details/7323604
     */
    public boolean equals(Object obj) {
        if (obj instanceof PlayItem) {
            PlayItem p = (PlayItem) obj;
            return this.url.equals(p.url);
        }
        return super.equals(obj);
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setIsLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        if(url.contains("http")){
            this.isLocal=false;
        }
        else{
            this.isLocal=true;
        }
    }
}
