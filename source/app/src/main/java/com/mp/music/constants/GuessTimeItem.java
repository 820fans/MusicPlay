package com.mp.music.constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by 张伟 on 2016-04-06.
 */
public class GuessTimeItem {

    private String time;

    public GuessTimeItem(String time){
        this.time=time;
    }


    public String getFormatTime(){

        try{
            String pat = "yyyy年MM月dd日 HH时mm分ss秒" ;
            SimpleDateFormat sdf1 = new SimpleDateFormat(pat) ; // 实例化模板对象
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
            return sdf1.format(sdf.parse(time));
        }catch (ParseException e){
            e.printStackTrace();
        }
        return "";
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
