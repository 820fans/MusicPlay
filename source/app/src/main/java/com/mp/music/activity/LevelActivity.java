package com.mp.music.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mp.music.R;
import com.mp.music.base.BaseActivity;
import com.mp.music.entity.Constants;
import com.mp.music.utils.TitleBuilder;

/**
 * Created by 张伟 on 2016-04-21.
 */
public class LevelActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        new TitleBuilder(LevelActivity.this)
                .setTitleText("等级信息")
                .setLeftImage(R.drawable.back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LevelActivity.this.finish();
                    }
                })
                .build();

        imageLoader.displayImage(Constants.SERVER_URL + getIntent().getStringExtra("avatar"),
                ((ImageView) findViewById(R.id.avatar)));

        ((TextView)findViewById(R.id.level)).setText("LV " + getIntent().getIntExtra("level", -1));
        ((TextView)findViewById(R.id.brand)).setText(getIntent().getStringExtra("brand"));
        ((TextView)findViewById(R.id.currentexp)).setText(getIntent().getIntExtra("exp", -1)+"");
        ((TextView)findViewById(R.id.needexp)).setText(getIntent().getIntExtra("needexp", -1)+"");
    }
}
