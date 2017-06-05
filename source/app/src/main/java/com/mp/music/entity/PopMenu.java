package com.mp.music.entity;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mp.music.R;
import com.mp.music.base.BaseApplication;

public class PopMenu extends PopupWindow {

    private TextView music_name,music_singer;
    private FloatingActionButton act_challenge;
    private ImageView btnPre,btnNext;
    private ImageView music_cover;
    private CheckBox btnPlayPause;
    private FloatingActionButton pop_back;
    private SeekBar seekBar;
    private View mMenuView;

  
    @SuppressWarnings("deprecation")
	public PopMenu(Activity context,OnClickListener itemsOnClick,
                   SeekBar.OnSeekBarChangeListener seekBarChangeListener) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        mMenuView = inflater.inflate(R.layout.music_detail_dialog, null);

        //获取文字
        music_name=(TextView)mMenuView.findViewById(R.id.music_name);
        music_singer=(TextView)mMenuView.findViewById(R.id.music_singer);
        act_challenge=(FloatingActionButton)mMenuView.findViewById(R.id.act_challenge);

        //获取按钮
        btnNext=(ImageView)mMenuView.findViewById(R.id.ic_next);
        btnPlayPause=(CheckBox)mMenuView.findViewById(R.id.cb_musicplay);
        btnPre=(ImageView)mMenuView.findViewById(R.id.ic_previous);
        pop_back=(FloatingActionButton)mMenuView.findViewById(R.id.pop_back);
        seekBar=(SeekBar)mMenuView.findViewById(R.id.seekBar);
        music_cover=(ImageView)mMenuView.findViewById(R.id.music_cover);
        music_cover.setImageResource(R.drawable.album);

        //设置按钮监听
        btnNext.setOnClickListener(itemsOnClick);
        btnPlayPause.setOnClickListener(itemsOnClick);
        btnPre.setOnClickListener(itemsOnClick);
        pop_back.setOnClickListener(itemsOnClick);
        act_challenge.setOnClickListener(itemsOnClick);
        //设置进度条滚动监听
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        //设置SelectPicPopupWindow的View  
        this.setContentView(mMenuView);  
        //设置SelectPicPopupWindow弹出窗体的宽  
        this.setWidth(LayoutParams.FILL_PARENT);  
        //设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(LayoutParams.WRAP_CONTENT);  
        //设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);  
        //设置SelectPicPopupWindow弹出窗体动画效果  
        this.setAnimationStyle(R.style.AnimBottom);  
        //实例化一个ColorDrawable颜色为半透明  
        ColorDrawable dw = new ColorDrawable(0xb0000000);  
        //设置SelectPicPopupWindow弹出窗体的背景  
        this.setBackgroundDrawable(dw);  
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框  
//        mMenuView.setOnTouchListener(new OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//
//                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
//                int y=(int) event.getY();
//                if(event.getAction()==MotionEvent.ACTION_UP){
//                    if(y<height){
//                        dismiss();
//                    }
//                }
//                return true;
//            }
//        });
    }

    public void setMusic_cover(String cover){

        if(cover!="" && cover!=null){
            BaseApplication.imageLoader.displayImage(cover, music_cover);
        }
        else{
            music_cover.setImageResource(R.drawable.album);
        }
    }

    public boolean getBtnPlayPause(){
        return btnPlayPause.isChecked();
    }
    public void setBtnPlayPause(boolean state){
        btnPlayPause.setChecked(state);
    }

    public void setMusicNameandSinger(String name,String singer){
        this.music_name.setText(name);
        this.music_singer.setText(singer);
    }

    public void setSeekBarProgress(float percent){
        this.seekBar.setProgress((int) (this.seekBar.getMax()*percent));
    }

    public void setSeekBarSecondProgress(int percent){
        this.seekBar.setSecondaryProgress(percent);
    }
} 