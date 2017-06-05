package com.mp.music.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.mp.music.R;
import com.mp.music.constants.ChallengeItem;

import java.util.ArrayList;

/**
 * Created by 张伟 on 2016-04-06.
 */
public class QuestionListAdapter extends  RecyclerView.Adapter<QuestionListAdapter.ViewHolder>{

    /**
     * ItemClick的回调接口
     * @author zhy
     *
     */
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private LayoutInflater mInflater;
    private Context context;
    private ArrayList<ChallengeItem> mDatas;

    public QuestionListAdapter(Context context, ArrayList<ChallengeItem> datats)
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

            mAvatar = (NetworkImageView) view.findViewById(R.id.avatar);
            mChallengeName=(TextView) view.findViewById(R.id.challenge_name);
            mUpName=(TextView) view.findViewById(R.id.upper_name);
            mTime=(TextView) view.findViewById(R.id.upper_time);
        }

        NetworkImageView mAvatar;
        TextView mChallengeName;
        TextView mUpName;
        TextView mTime;
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
        View view = mInflater.inflate(R.layout.item_challenge,
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
        //NetworkImageView networkImageView = (NetworkImageView) findViewById(R.id.network_image_view);

//        viewHolder.mImg.setImageUrl(Constants.SERVER_URL + mDatas.get(i).getCover(), ImageHelper.getLoader());
//        viewHolder.mName.setText(mDatas.get(i).getName());
//        viewHolder.mSinger.setText(mDatas.get(i).getName());
//        viewHolder.mType.setText(mDatas.get(i).getType());
//


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
