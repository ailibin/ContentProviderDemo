package com.aiitec.contentproviderdemo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.aiitec.contentproviderdemo.R;
import com.aiitec.contentproviderdemo.base.CommonRecyclerViewAdapter;
import com.aiitec.contentproviderdemo.base.CommonRecyclerViewHolder;
import com.aiitec.contentproviderdemo.glide.GlideRoundTransform;
import com.aiitec.contentproviderdemo.model.Contact;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * @author ailibin
 * @version 1.0
 * createTime 2018/1/8.
 */
public class ProfileSummaryAdapter extends CommonRecyclerViewAdapter<Contact> {

    public ProfileSummaryAdapter(Context context, List<Contact> data) {
        super(context, data);
    }

    public ProfileSummaryAdapter(Context context) {
        super(context);
    }

    @SuppressLint("CheckResult")
    @Override
    public void convert(CommonRecyclerViewHolder h, Contact item, int position) {

        ImageView ivAvatar = h.getView(R.id.avatar);
        TextView tvName = h.getView(R.id.name);
        TextView tvDes = h.getView(R.id.description);
        View viewLine = h.getView(R.id.view_line);

        RequestOptions options = new RequestOptions();
        options.transform(new GlideRoundTransform(context, 5));
        options.placeholder(R.drawable.placeholder_avatar);
        Glide.with(context).load(item.getImagePath()).apply(options).into(ivAvatar);

        tvName.setText(item.getName());

    }

    @Override
    public int getLayoutViewId(int viewType) {
        return R.layout.item_profile_summary;
    }
}
