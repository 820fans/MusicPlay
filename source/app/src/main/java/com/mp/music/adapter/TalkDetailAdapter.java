package com.mp.music.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.mp.music.R;
import com.mp.music.constants.CommentItem;
import com.mp.music.entity.Constants;
import com.mp.music.helper.ImageHelper;

import java.util.ArrayList;

/**
 * Created by 张伟 on 2016-04-15.
 */
public class TalkDetailAdapter extends  RecyclerView.Adapter<TalkDetailAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private Context context;
    private ArrayList<CommentItem> mDatas;

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public TalkDetailAdapter(Context context, ArrayList<CommentItem> datats)
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
            reply=(TextView)view.findViewById(R.id.reply);
            content=(TextView)view.findViewById(R.id.content);
        }

        NetworkImageView avatar;
        TextView account;
        TextView time;
        TextView reply;
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
        View view = mInflater.inflate(R.layout.item_comment,
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

        viewHolder.reply.setText(mDatas.get(i).getReply());

        viewHolder.content.setText(mDatas.get(i).getContent());

        //如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, i);
                }
            });
        }
    }

}
