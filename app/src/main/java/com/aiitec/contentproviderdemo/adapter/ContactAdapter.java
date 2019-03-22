package com.aiitec.contentproviderdemo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import com.aiitec.contentproviderdemo.R;
import com.aiitec.contentproviderdemo.base.CommonRecyclerViewAdapter;
import com.aiitec.contentproviderdemo.base.CommonRecyclerViewHolder;
import com.aiitec.contentproviderdemo.glide.GlideRoundTransform;
import com.aiitec.contentproviderdemo.model.Contact;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ailibin
 * @version 1.0
 * createTime 2018/1/8.
 */

public class ContactAdapter extends CommonRecyclerViewAdapter<Contact> implements SectionIndexer {

    /**
     * 自定义item的布局
     */
    private int itemLayoutId;

    /**
     * 已经选择的联系人数据
     */
    private List<Contact> mSelectItems = new ArrayList<>();


    public ContactAdapter(Context context, List<Contact> data) {
        super(context, data);
    }

    public void setItemLayoutId(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
    }

    public void updateData(List<Contact> selectItems) {
        mSelectItems.clear();
        mSelectItems.addAll(selectItems);
    }

    @SuppressLint("CheckResult")
    @Override
    public void convert(final CommonRecyclerViewHolder h, final Contact entity, final int position) {

        TextView tv_item_contact_letter = h.getView(R.id.tv_item_contact_letter);
        ImageView civ_item_contact_avatar = h.getView(R.id.civ_item_contact_avatar);
        TextView tv_item_contact_name = h.getView(R.id.tv_item_contact_name);
        View line_item = h.getView(R.id.line_item);
        //根据position获取分类的首字母的char ascii值
        int section = getSectionForPosition(position);

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            tv_item_contact_letter.setVisibility(View.VISIBLE);
            tv_item_contact_letter.setText(entity.getSortLetters());
        } else {
            tv_item_contact_letter.setVisibility(View.GONE);
        }
        char thisLetter = data.get(position).getSortLetters().charAt(0);
        //最后一条不显示线
        if (position == data.size() - 1) {
            line_item.setVisibility(View.GONE);
        } else {
            char nextLetter = data.get(position + 1).getSortLetters().charAt(0);

            if (thisLetter == nextLetter) {
                //如果两条拼音的首字母一样，就显示线
                line_item.setVisibility(View.VISIBLE);
            } else {
                line_item.setVisibility(View.GONE);
            }
        }

        RequestOptions options = new RequestOptions();
        options.transform(new GlideRoundTransform(context, 5));
        options.placeholder(R.drawable.placeholder_avatar);

        Glide.with(context).load(entity.getImagePath()).apply(options).into(civ_item_contact_avatar);

        //这里如果设置了备注,就用用户的备注信息,否则就用昵称
        if (!TextUtils.isEmpty(entity.getAlias())) {
            tv_item_contact_name.setText(entity.getAlias());
        } else {
            tv_item_contact_name.setText(entity.getName());
        }

    }


    @Override
    public int getLayoutViewId(int viewType) {
        return itemLayoutId;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < data.size(); i++) {
            String sortStr = data.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return data.get(position).getSortLetters().charAt(0);
    }

}
