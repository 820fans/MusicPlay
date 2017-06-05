package com.mp.music.activity;

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
import com.mp.music.db.DatabaseManager;
import com.mp.music.entity.Constants;
import com.mp.music.entity.NetResultData;
import com.mp.music.helper.NetHelper;
import com.mp.music.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 张伟 on 2016-04-15.
 */
public class LoginActivity extends BaseActivity {

    private EditText account;
    private EditText password;
    private Button submit;
    private Snackbar snackbar = null;
    private TextView jump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        account=(EditText)findViewById(R.id.account);
        password=(EditText)findViewById(R.id.password);
        submit=(Button)findViewById(R.id.submit);
        jump=(TextView)findViewById(R.id.jump);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkInput()){

                    submit.setText("正在登录...");
                    submit.setEnabled(false);

                    NetHelper netHelper = new NetHelper(Constants.DO_LOGIN);
                    netHelper.setParams("account",account.getText().toString());
                    netHelper.setParams("psw",password.getText().toString());
                    netHelper.setResultListener(new NetHelper.ResultListener() {

                        @Override
                        public void getResult(NetResultData result) {

                            //成功登陆
                            if (result.getCode() == 1) {
//                                intent2Activity(MainActivity.class);

                                try {

                                    JSONObject json = new JSONObject(result.getRaw());
                                    int user_id=json.optJSONObject("data").getInt("user_id");
//                                    ToastUtils.showToast(LoginActivity.this,
//                                            "用户" + user_id, Toast.LENGTH_SHORT);
                                    DatabaseManager.getInstance(getApplicationContext()).insertActiveUser(user_id);
                                    intent2Activity(MainActivity.class);
                                    LoginActivity.this.finish();
                                }
                                catch(JSONException e){
                                    e.printStackTrace();
                                }
                            } else if (result.getCode() == 2) {

                                submit.setText("登录");
                                submit.setEnabled(true);
                                ToastUtils.showToast(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT);
                            }
                        }

                        @Override
                        public void getError() {
                            Toast.makeText(LoginActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                    netHelper.doPost();
                }

            }
        });

        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                intent2Activity(RegisterActivity.class);
                LoginActivity.this.finish();

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
        return true;
    }

    /**
     * 显示提示信息
     * @param msg
     */
    public void showSnack(String msg){

        snackbar=Snackbar.make(findViewById(R.id.login_wrapper),
                "测试弹出提示", Snackbar.LENGTH_SHORT);

        snackbar.setText(msg);

        ((TextView) snackbar.getView().findViewById(R.id.snackbar_text))
                .setTextColor(Color.parseColor("#FFFFFF"));

        if(!snackbar.isShown())
        snackbar.show();
    }
}
