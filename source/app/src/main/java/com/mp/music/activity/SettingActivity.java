package com.mp.music.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.mp.music.R;
import com.mp.music.base.BaseActivity;
import com.mp.music.db.DatabaseManager;
import com.mp.music.entity.Constants;
import com.mp.music.entity.NetResultData;
import com.mp.music.helper.NetHelper;
import com.mp.music.utils.DialogUtils;
import com.mp.music.utils.TitleBuilder;
import com.mp.music.utils.ToastUtils;

/**
 * Created by 张伟 on 2016-04-26.
 */
public class SettingActivity extends BaseActivity implements OnClickListener{

    private CardView clean;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        clean=(CardView)findViewById(R.id.clean);
        logout=(Button)findViewById(R.id.logout);

        new TitleBuilder(SettingActivity.this)
                .setTitleText("设置")
                .setLeftImage(R.drawable.back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SettingActivity.this.finish();
                    }
                })
                .build();

        clean.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    private void Logout(){

        DialogUtils.showConfirmDialog(SettingActivity.this, "登出", "确认退出登录？",
                "确认", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //获取用户信息
                        NetHelper netHelper = new NetHelper(Constants.DO_LOGOUT);
                        netHelper.setParams("user_id", DatabaseManager.
                                getInstance(SettingActivity.this).getActiveUser());
                        netHelper.setResultListener(new NetHelper.ResultListener() {

                            @Override
                            public void getResult(NetResultData result) {

                                if (result.getCode() == 1) {

                                    DatabaseManager.getInstance(SettingActivity.this).clearActiveUser();
                                    //退出登录成功，转到登录界面
                                    ToastUtils.showToast(SettingActivity.this, "登出成功", Toast.LENGTH_SHORT);


                                    Intent stopMusic=new Intent();
                                    stopMusic.setAction(Constants.MUSICSERVICE_ACTION);
                                    stopMusic.putExtra("control", Constants.STATE_STOP);
                                    sendBroadcast(stopMusic);

                                    Intent intent = new Intent();
                                    intent.setAction(Constants.EXIT_ACTION); // 说明动作
                                    sendBroadcast(intent);// 该函数用于发送广播

                                    intent2Activity(LoginActivity.class);
                                    SettingActivity.this.finish();
                                }
                            }

                            @Override
                            public void getError() {
                                ToastUtils.showToast(SettingActivity.this, "网络错误", Toast.LENGTH_SHORT);
                            }
                        });

                        netHelper.doPost();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clean:
                break;
            case R.id.logout:
                Logout();
                break;
            default:
                break;
        }
    }

}
