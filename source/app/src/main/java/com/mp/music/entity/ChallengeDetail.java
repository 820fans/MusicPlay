package com.mp.music.entity;

/**
 * Created by 张伟 on 2016-04-09.
 */
public class ChallengeDetail {

    private int id;

    private String path_fog;//雾霾图

    private String path;//图像路径

    private String account;//上传用户

    private String avatar;//上传挑战的人的头像路径

    private String title;//挑战的详情

    private String introduce;

    private String tag;

    private String music;

    public ChallengeDetail(int id,String path_fog,String path,String account,String avatar,String title,
                           String introduce,String tag,String music){
        this.id=id;
        this.path_fog=path_fog;
        this.path=path;
        this.account=account;
        this.avatar=avatar;
        this.title=title;
        this.introduce=introduce;
        this.tag=tag;
        this.music=music;
    }

    public String getPath_fog() {
        return path_fog;
    }

    public void setPath_fog(String path_fog) {
        this.path_fog = path_fog;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }
}
