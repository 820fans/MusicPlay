package com.mp.music.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.mp.music.R;
import com.mp.music.constants.TalkItem;
import com.mp.music.entity.Constants;
import com.mp.music.helper.ImageHelper;
import com.mp.music.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by 张伟 on 2016-04-07.
 */
public class TalkListAdapter extends  RecyclerView.Adapter<TalkListAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private Context context;
    private ArrayList<TalkItem> mDatas;

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public TalkListAdapter(Context context, ArrayList<TalkItem> datats)
    {
        mInflater = LayoutInflater.from(context);
        this.context=context;
        mDatas = datats;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View view)
        {
            super(view);

            avatar=(NetworkImageView)view.findViewById(R.id.avatar);
            account=(TextView)view.findViewById(R.id.account);
            time=(TextView)view.findViewById(R.id.time);
            comment_num=(TextView)view.findViewById(R.id.comment_num);
            title=(TextView)view.findViewById(R.id.title);
            content=(TextView)view.findViewById(R.id.content);
        }

        NetworkImageView avatar;
        TextView account;
        TextView time;
        TextView comment_num;
        TextView title;
        TextView content;
    }

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = mInflater.inflate(R.layout.item_talk,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i)
    {
        viewHolder.avatar.setImageUrl(Constants.SERVER_URL + mDatas.get(i).getAvatar(), ImageHelper.getLoader());
        viewHolder.account.setText(mDatas.get(i).getAccount());
        viewHolder.time.setText(mDatas.get(i).getTime());
        viewHolder.comment_num.setText(mDatas.get(i).getComment_num() + "");
        viewHolder.title.setText(mDatas.get(i).getTitle());

        // 特殊文字处理,将表情等转换一下
        viewHolder.content.setText(StringUtils.getWeiboContent(
                context, viewHolder.content, mDatas.get(i).getContent()));

        //如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, i);
                }
            });
        }
    }

}
