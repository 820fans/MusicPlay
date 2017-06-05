package com.mp.music.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mp.music.R;
import com.mp.music.adapter.TalkListAdapter;
import com.mp.music.base.BaseActivity;
import com.mp.music.constants.TalkItem;
import com.mp.music.entity.Constants;
import com.mp.music.entity.NetResultData;
import com.mp.music.helper.NetHelper;
import com.mp.music.utils.TitleBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 张伟 on 2016-04-12.
 */
public class TalkListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private TalkListAdapter mAdapter;
    private ArrayList<TalkItem> mDatas=new ArrayList<TalkItem>();
    int section_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

        section_id= getIntent().getIntExtra("section_id",-1);
        int res=getIntent().getIntExtra("res",-1);
        String title=getIntent().getStringExtra("section");

        ((ImageView)findViewById(R.id.section_img)).setImageResource(res);
        ((TextView)findViewById(R.id.section_title)).setText(title);

        new TitleBuilder(this)
            .setLeftImage(R.drawable.back)
            .setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TalkListActivity.this.finish();
                }
            })
            .setTitleText(title)
            .setRightText("发起聊天")
            .setRightOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(TalkListActivity.this, PostChallenge.class);
                    intent.putExtra("type","talk");
                    intent.putExtra("id", section_id);
                    startActivity(intent);
                }
            });

        //得到控件
        mRecyclerView = (RecyclerView)this.findViewById(R.id.recycle_talklist);
        mRecyclerView.setHasFixedSize(true);

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //设置适配器
        mAdapter = new TalkListAdapter(this, mDatas);

        mRecyclerView.setAdapter(mAdapter);

        //监听按键
        mAdapter.setOnItemClickLitener(new TalkListAdapter.OnItemClickLitener() {

            @Override
            public void onItemClick(View view, int position) {

                Intent intent=new Intent();
                intent.putExtra("talk_id",mDatas.get(position).getTalk_id());
                intent.putExtra("avatar",mDatas.get(position).getAvatar());
                intent.putExtra("account",mDatas.get(position).getAccount());
                intent.putExtra("time",((TextView)view.findViewById(R.id.time)).getText());
                intent.putExtra("title",mDatas.get(position).getTitle());
                intent.putExtra("content",mDatas.get(position).getContent());
                intent.setClass(TalkListActivity.this, TalkDetailActivity.class);
                startActivity(intent);
            }
        });

        GetTalkData();
    }


    @Override
    protected void onResume() {
        super.onResume();

        GetTalkData();
    }

    public void GetTalkData(){
        //获取音乐标签
        NetHelper netHelper = new NetHelper(Constants.GET_TALKS);
        netHelper.setParams("section_id",section_id+"");
        netHelper.setResultListener(new NetHelper.ResultListener() {

            @Override
            public void getResult(NetResultData result) {

                JSONArray musicList = result.getData();

                try {

                    //帖子数量
                    ((TextView)findViewById(R.id.section_talk_num)).setText(musicList.length()+"");

                    //处理JSON数组数据
                    for (int i = 0; i < musicList.length(); i++) {
                        JSONObject item = (JSONObject) musicList.opt(i);

                        TalkItem talkItem = new TalkItem(
                                item.getInt("talk_id"),
                                item.getInt("section_id"),
                                item.getInt("user_id"),
                                item.getInt("comment_num"),
                                item.getString("avatar"),
                                item.getString("account"),
                                item.getString("time"),
                                item.getString("title"),
                                item.getString("content"));

                        if(!mDatas.contains(talkItem))
                            mDatas.add(talkItem);

                    }

                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getError() {
                Toast.makeText(getApplicationContext(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        });

        netHelper.doPost();
    }
}
