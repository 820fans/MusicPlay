package com.mp.music.constants;

/**
 * Created by 张伟 on 2016-04-06.
 */
public class ChallengeItem {

    private int id;

    private String avatar;

    private String challenge_name;

    private String upper_name;

    private String upper_time;

    public ChallengeItem(int id,String avatar,String challenge_name,String upper_name,String upper_time){
        this.id=id;
        this.avatar=avatar;
        this.challenge_name=challenge_name;
        this.upper_name=upper_name;
        this.upper_time=upper_time;
    }

    /**判断两元素相等，方法重写
     * 参考资料 http://blog.csdn.net/witsmakemen/article/details/7323604
     */
    public boolean equals(Object obj) {
        if (obj instanceof ChallengeItem) {
            ChallengeItem p = (ChallengeItem) obj;
            return this.id==p.id;
        }
        return super.equals(obj);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChallenge_name() {
        return challenge_name;
    }

    public void setChallenge_name(String challenge_name) {
        this.challenge_name = challenge_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUpper_name() {
        return upper_name;
    }

    public void setUpper_name(String upper_name) {
        this.upper_name = upper_name;
    }

    public String getUpper_time() {
        return upper_time;
    }

    public void setUpper_time(String upper_time) {
        this.upper_time = upper_time;
    }
}
