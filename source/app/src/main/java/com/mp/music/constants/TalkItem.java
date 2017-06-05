package com.mp.music.constants;

import com.mp.music.utils.TimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 张伟 on 2016-04-12.
 */
public class TalkItem {
    private int talk_id;  //帖子的id
    private int section_id; //版块的id
    private int user_id;//发帖人的id
    private int comment_num;//某个帖子评论的数量
    private String avatar;//发帖人头像
    private String account; //发帖人名称
    private String time; //发帖时间
    private String title; //标题
    private String content;//帖子内容


    public TalkItem(int talk_id,int section_id,int user_id,int comment_num,String avatar,String account,
                    String time,String title,String content){
        this.talk_id=talk_id;
        this.section_id=section_id;
        this.user_id=user_id;
        this.comment_num=comment_num;
        this.avatar=avatar;
        this.account=account;

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{

            this.time= TimeUtils.getDateDifftime(sdf.parse(time),new Date());

        }catch (ParseException e){
            e.printStackTrace();
        }

        this.title=title;
        this.content=content;
    }

    /**判断两元素相等，方法重写
     * 参考资料 http://blog.csdn.net/witsmakemen/article/details/7323604
     */
    public boolean equals(Object obj) {
        if (obj instanceof TalkItem) {
            TalkItem p = (TalkItem) obj;
            return this.talk_id==p.talk_id;
        }
        return super.equals(obj);
    }

    public int getSection_id() {
        return section_id;
    }

    public void setSection_id(int section_id) {
        this.section_id = section_id;
    }

    public int getTalk_id() {
        return talk_id;
    }

    public void setTalk_id(int talk_id) {
        this.talk_id = talk_id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
