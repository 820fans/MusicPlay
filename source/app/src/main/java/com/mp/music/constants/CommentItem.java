package com.mp.music.constants;

import com.mp.music.utils.TimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 张伟 on 2016-04-15.
 */
public class CommentItem  {
    private int id;
    private int talk_id;
    private int user_id;
    private String avatar;
    private String account;
    private String time;
    private String reply;//回复谁的 --- 这个人的account
    private String content;

    public CommentItem(int id,int talk_id,int user_id,String avatar,String account,
                       String time,String reply,String content){
        this.id=id;
        this.talk_id=talk_id;
        this.user_id=user_id;
        this.avatar=avatar;
        this.account=account;

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{

            this.time= TimeUtils.getDateDifftime(sdf.parse(time), new Date());

        }catch (ParseException e){
            e.printStackTrace();
        }

        if(reply=="" || reply==null ) {
            this.reply = "评论内容 : ";
        }
        else{
            this.reply = "回复 : "+reply;
        }
        this.content=content;
    }


    /**判断两元素相等，方法重写
     * 参考资料 http://blog.csdn.net/witsmakemen/article/details/7323604
     */
    public boolean equals(Object obj) {
        if (obj instanceof CommentItem) {
            CommentItem p = (CommentItem) obj;
            return this.id==p.id;
        }
        return super.equals(obj);
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public int getTalk_id() {
        return talk_id;
    }

    public void setTalk_id(int talk_id) {
        this.talk_id = talk_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
