package com.mp.music.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mp.music.R;
import com.mp.music.base.BaseActivity;
import com.mp.music.utils.TitleBuilder;

/**
 * Created by 张伟 on 2016-04-20.
 */
public class EditPasswordActivity extends BaseActivity {

    private EditText password;
    private EditText new_password;
    private EditText confirm_password;
    private Button submit;
    private Snackbar snackbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpass);

        new TitleBuilder(EditPasswordActivity.this)
                .setTitleText("修改密码")
                .setLeftImage(R.drawable.back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditPasswordActivity.this.finish();
                    }
                })
                .build();

        password=(EditText)findViewById(R.id.password);
        new_password=(EditText)findViewById(R.id.new_password);
        confirm_password=(EditText)findViewById(R.id.cofirm_password);
        submit=(Button)findViewById(R.id.submit);

        //提交修改
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkInput()){

                }
            }
        });
    }


    public boolean checkInput(){

        if(TextUtils.isEmpty(password.getText())){
            showSnack("请输入原始");
            return false;
        }
        else if(TextUtils.isEmpty(password.getText())){

            showSnack("请输入新密码");
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

        snackbar= Snackbar.make(findViewById(R.id.edit_wrapper),
                "测试弹出提示", Snackbar.LENGTH_SHORT);

        snackbar.setText(msg);

        ((TextView) snackbar.getView().findViewById(R.id.snackbar_text))
                .setTextColor(Color.parseColor("#FFFFFF"));

        if(!snackbar.isShown())
            snackbar.show();
    }
}
