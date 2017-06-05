package com.mp.music.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.mp.music.R;
import com.mp.music.adapter.GuessTimeDetailAdapter;
import com.mp.music.base.BaseActivity;
import com.mp.music.constants.GuessDetailItem;
import com.mp.music.db.DatabaseManager;
import com.mp.music.entity.Constants;
import com.mp.music.entity.NetResultData;
import com.mp.music.helper.NetHelper;
import com.mp.music.utils.TitleBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 张伟 on 2016-04-28.
 */
public class GuessActivity extends BaseActivity {


    private RecyclerView mRecyclerView;
    private GuessTimeDetailAdapter mAdapter;
    private ArrayList<GuessDetailItem> mDatas=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);

        new TitleBuilder(GuessActivity.this)
                .setTitleText("猜测详情")
                .setLeftImage(R.drawable.back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GuessActivity.this.finish();
                    }
                })
                .build();


        //得到控件
        mRecyclerView = (RecyclerView)this.findViewById(R.id.recycle_guesses);
        mRecyclerView.setHasFixedSize(true);
        //设置布局管理器
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        mAdapter = new GuessTimeDetailAdapter(this, mDatas);

        mRecyclerView.setAdapter(mAdapter);

        //获取猜测详情
        NetHelper netHelper = new NetHelper(Constants.GET_GUESS_DETAIL);
        netHelper.setParams("challenge_id",getIntent().getIntExtra("id", -1)+"");
        netHelper.setParams("time",getIntent().getStringExtra("time"));
        netHelper.setParams("user_id", DatabaseManager.getInstance(
                GuessActivity.this).getActiveUser()+"");
        System.out.println(getIntent().getIntExtra("id", -1)+getIntent().getStringExtra("time")+
                DatabaseManager.getInstance(
                        GuessActivity.this).getActiveUser()+"");
        netHelper.setResultListener(new NetHelper.ResultListener() {

            @Override
            public void getResult(NetResultData result) {

                JSONArray detail = result.getData();

                try {

                    for(int i=0;i<detail.length();i++){
                        JSONObject item1 = (JSONObject) detail.opt(i);
                        GuessDetailItem guessDetailItem=new GuessDetailItem(
                                item1.getInt("id"),
                                item1.getString("answer"),
                                item1.getInt("time"),
                                item1.getString("done"));
                        mDatas.add(guessDetailItem);
                    }

                    mAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getError() {
                Toast.makeText(GuessActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });

        netHelper.doPost();
    }
}
