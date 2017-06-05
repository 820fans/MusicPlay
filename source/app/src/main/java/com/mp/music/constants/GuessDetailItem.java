package com.mp.music.constants;

/**
 * Created by 张伟 on 2016-04-28.
 */
public class GuessDetailItem {

    private int id;
    private String answer;
    private int time;
    private String done;

    public GuessDetailItem(int id,String answer,int time,String done){
        this.id=id;
        this.answer=answer;
        this.time=time;
        this.done=done;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDone() {
        return done;
    }

    public void setDone(String done) {
        this.done = done;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time+"s";
    }

    public void setTime(int time) {
        this.time = time;
    }
}
