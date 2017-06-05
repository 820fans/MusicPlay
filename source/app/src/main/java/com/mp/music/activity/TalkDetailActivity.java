package com.mp.music.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.mp.music.R;
import com.mp.music.adapter.TalkDetailAdapter;
import com.mp.music.base.BaseActivity;
import com.mp.music.constants.CommentItem;
import com.mp.music.db.DatabaseManager;
import com.mp.music.entity.Constants;
import com.mp.music.entity.NetResultData;
import com.mp.music.helper.ImageHelper;
import com.mp.music.helper.NetHelper;
import com.mp.music.utils.StringUtils;
import com.mp.music.utils.TitleBuilder;
import com.mp.music.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 张伟 on 2016-04-13.
 */
public class TalkDetailActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private TalkDetailAdapter mAdapter;
    private ArrayList<CommentItem> mDatas = new ArrayList<CommentItem>();

    private LinearLayout input_bar;
    private EditText answer;
    private Button submit;

    private int talk_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_detail);

        initView();

        //得到控件
        mRecyclerView = (RecyclerView) this.findViewById(R.id.recycle_talkcomment);

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //设置适配器
        mAdapter = new TalkDetailAdapter(this, mDatas);

        mRecyclerView.setAdapter(mAdapter);

        //监听按键
        mAdapter.setOnItemClickLitener(new TalkDetailAdapter.OnItemClickLitener() {

            @Override
            public void onItemClick(View view, int position) {

            }
        });

        GetCommentData();
    }

    public void GetCommentData(){

        //获取音乐标签
        NetHelper netHelper = new NetHelper(Constants.GET_COMMENTS);
        netHelper.setParams("talk_id", talk_id + "");
        netHelper.setResultListener(new NetHelper.ResultListener() {

            @Override
            public void getResult(NetResultData result) {

                JSONArray musicList = result.getData();

                try {

                    //处理JSON数组数据
                    for (int i = 0; i < musicList.length(); i++) {
                        JSONObject item = (JSONObject) musicList.opt(i);

                        CommentItem commentItem = new CommentItem(
                                item.getInt("id"),
                                item.getInt("talk_id"),
                                item.getInt("user_id"),
                                item.getString("avatar"),
                                item.getString("account"),
                                item.getString("time"),
                                item.getString("reply") + "",
                                item.getString("content"));

                        if(!mDatas.contains(commentItem))
                        mDatas.add(commentItem);

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

    public void initView() {

        talk_id = getIntent().getIntExtra("talk_id", -1);
        String avatar = getIntent().getStringExtra("avatar");
        String account = getIntent().getStringExtra("account");
        String time = getIntent().getStringExtra("time");
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");

        ((NetworkImageView) findViewById(R.id.talk_avatar)).setImageUrl(Constants.SERVER_URL + avatar,
                ImageHelper.getLoader());
        ((TextView) findViewById(R.id.talk_account)).setText(account);
        ((TextView) findViewById(R.id.talk_time)).setText(time);
        ((TextView) findViewById(R.id.talk_title)).setText(title);
        //特殊文字处理
        ((TextView) findViewById(R.id.talk_content)).setText(StringUtils.getWeiboContent(
                getApplicationContext(), (TextView) findViewById(R.id.talk_content), content));


        new TitleBuilder(this)
                .setLeftImage(R.drawable.back)
                .setTitleText("帖子详情")
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TalkDetailActivity.this.finish();
                    }
                });


        input_bar = (LinearLayout) findViewById(R.id.input_controlbar);
        answer = (EditText) input_bar.findViewById(R.id.answer);
        answer.setHint("填写你的评论");
        submit = (Button) input_bar.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String content = answer.getText().toString();

                if(content.equals("")){
                    ToastUtils.showToast(TalkDetailActivity.this,"请输入评论内容",Toast.LENGTH_SHORT);
                    return;
                }

                NetHelper netHelper = new NetHelper(Constants.UP_COMMENT);
                netHelper.setParams("user_id", DatabaseManager.getInstance(
                        TalkDetailActivity.this).getActiveUser()+"");
                netHelper.setParams("talk_id", talk_id + "");
                netHelper.setParams("content", content);
                netHelper.setResultListener(new NetHelper.ResultListener() {

                    @Override
                    public void getResult(NetResultData result) {

                        if (result.getCode() == 0) {

                            GetCommentData();

                            ToastUtils.showToast(TalkDetailActivity.this, "评论发布成功",
                                    Toast.LENGTH_SHORT);
                            answer.setText("");
                        }
                    }

                    @Override
                    public void getError() {
                        ToastUtils.showToast(TalkDetailActivity.this, "网络错误", Toast.LENGTH_SHORT);
                    }
                });

                netHelper.doPost();
            }
        });
    }
}
