package com.mp.music.activity;

import android.os.Bundle;
import android.view.View;

import com.mp.music.R;
import com.mp.music.base.BaseActivity;
import com.mp.music.utils.TitleBuilder;

/**
 * Created by 张伟 on 2016-04-20.
 */
public class AboutActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        new TitleBuilder(AboutActivity.this)
                .setTitleText("关于")
                .setLeftImage(R.drawable.back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AboutActivity.this.finish();
                    }
                })
                .build();
    }
}
