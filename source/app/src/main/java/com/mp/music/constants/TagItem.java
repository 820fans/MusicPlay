package com.mp.music.constants;

/**
 * Created by 张伟 on 2016-04-07.
 */
public class TagItem {

    private int id;

    private String music;

    private String tag;

    public TagItem(int id,String music,String tag){
        this.id=id;
        this.music=music;
        this.tag=tag;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
