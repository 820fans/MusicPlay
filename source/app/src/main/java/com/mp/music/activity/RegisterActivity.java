package com.mp.music.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mp.music.R;
import com.mp.music.base.BaseActivity;
import com.mp.music.entity.Constants;
import com.mp.music.entity.NetResultData;
import com.mp.music.helper.NetHelper;
import com.mp.music.utils.ToastUtils;

/**
 * Created by 张伟 on 2016-04-15.
 */
public class RegisterActivity  extends BaseActivity {

    private EditText account;
    private EditText password;
    private EditText confirm_password;
    private Button submit;
    private Snackbar snackbar = null;
    private TextView jump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        account=(EditText)findViewById(R.id.account);
        password=(EditText)findViewById(R.id.password);
        confirm_password=(EditText)findViewById(R.id.cofirm_password);
        submit=(Button)findViewById(R.id.submit);
        jump=(TextView)findViewById(R.id.jump);

        //提交注册
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkInput()){

                    submit.setText("正在注册...");
                    submit.setEnabled(false);

                    NetHelper netHelper = new NetHelper(Constants.DO_REGISTER);
                    netHelper.setParams("account",account.getText().toString());
                    netHelper.setParams("password",password.getText().toString());
                    netHelper.setResultListener(new NetHelper.ResultListener() {

                        @Override
                        public void getResult(NetResultData result) {

                            if (result.getCode() == 1) {
                                Intent intent = new Intent();
                                intent.setClass(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                ToastUtils.showToast(RegisterActivity.this, "注册成功，请登录", Toast.LENGTH_SHORT);
                                RegisterActivity.this.finish();

                            } else if (result.getCode() == 2) {
                                submit.setText("注册");
                                submit.setEnabled(false);
                                ToastUtils.showToast(RegisterActivity.this, "该账号已经被注册", Toast.LENGTH_SHORT);
                            } else if (result.getCode() == 0) {
                                submit.setText("注册");
                                submit.setEnabled(true);
                                ToastUtils.showToast(RegisterActivity.this, "请求异常", Toast.LENGTH_SHORT);
                            }
                        }

                        @Override
                        public void getError() {
                            Toast.makeText(RegisterActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                        }
                    });

                    netHelper.doPost();
                }

            }
        });

        //立即登录
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent2Activity(LoginActivity.class);
                RegisterActivity.this.finish();
            }
        });
    }

    public boolean checkInput(){

        if(TextUtils.isEmpty(account.getText())){
            showSnack("请输入账户");
            return false;
        }
        else if(TextUtils.isEmpty(password.getText())){

            showSnack("请输入密码");
            return false;
        }
        else if(TextUtils.isEmpty(confirm_password.getText())){

            showSnack("请确认密码");
            return false;
        }
        else if(!confirm_password.getText().toString().equals(password.getText().toString())){

            showSnack("两次输入密码不一致");
            return false;
        }
        return true;
    }

    /**
     * 显示提示信息
     * @param msg
     */
    public void showSnack(String msg){

        snackbar= Snackbar.make(findViewById(R.id.register_wrapper),
                "测试弹出提示", Snackbar.LENGTH_SHORT);

        snackbar.setText(msg);

        ((TextView) snackbar.getView().findViewById(R.id.snackbar_text))
                .setTextColor(Color.parseColor("#FFFFFF"));

        if(!snackbar.isShown())
            snackbar.show();
    }
}