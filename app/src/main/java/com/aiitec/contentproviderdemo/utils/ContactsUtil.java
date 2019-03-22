package com.aiitec.contentproviderdemo.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import com.aiitec.contentproviderdemo.model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ailibin
 * @Time: 2019/03/21
 * @Description: 获取联系人工具类
 * @Email: ailibin@qq.com
 */
public class ContactsUtil {

    public final static String TAG = "ailibin";
    /**
     * 号码
     */
    public final static String NUM = ContactsContract.CommonDataKinds.Phone.NUMBER;
    /**
     * 联系人姓名
     */
    public final static String NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;

    /**
     * 联系人ID
     */
    public final static String CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;


    /**
     * 按联系人的姓名搜索
     */
    public final static String SELECTION_NAME = ContactsContract.Data.DISPLAY_NAME;

    /**
     * 按联系人的ID搜索
     */
    public final static String SELECTION_CONTACT_ID = ContactsContract.Data.CONTACT_ID;


    /**
     * 上下文对象
     */
    private Context context;
    /**
     * 联系人提供者的uri
     */
    private Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

    public ContactsUtil(Context context) {
        this.context = context;
    }

    /**
     * 查询类型
     */
    public enum QueryType {

        /**
         * 根据联系人姓名
         */
        BY_NAME,
        /**
         * 根据联系人id
         */
        BY_CONTACT_ID

    }

    /**
     * 获取所有联系人的名字和电话号码
     *
     * @param isNeedContactId 是否需要联系人id
     * @return
     */
    public List<Contact> getContacts(boolean isNeedContactId) {
        List<Contact> contacts = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor;
        if (isNeedContactId) {
            cursor = cr.query(phoneUri, new String[]{NUM, NAME, CONTACT_ID}, null, null, null);
        } else {
            cursor = cr.query(phoneUri, new String[]{NUM, NAME}, null, null, null);
        }
        while (cursor.moveToNext()) {
            Contact contact;
            if (isNeedContactId) {
                contact = new Contact(
                        cursor.getString(cursor.getColumnIndex(NAME)),
                        cursor.getString(cursor.getColumnIndex(NUM)),
                        cursor.getString(cursor.getColumnIndex(CONTACT_ID)));
            } else {
                contact = new Contact(
                        cursor.getString(cursor.getColumnIndex(NAME)),
                        cursor.getString(cursor.getColumnIndex(NUM)));
            }
            contacts.add(contact);
        }
        Log.e(TAG, "contacts: " + contacts.toString());
        return contacts;
    }

    /**
     * 获取所有联系人的名字和电话号码
     *
     * @param queryArgs 根据内容查找,这个是精确查询,内容不能有一点差错,不然搜索结果就为空
     * @return
     */
    public List<Contact> getContacts(String queryArgs, QueryType type) {
        String selection;
        if (type == QueryType.BY_NAME) {
            selection = NAME + "=?";
//            selection = SELECTION_NAME + "=?";
        } else {
            selection = CONTACT_ID + "=?";
//            selection = SELECTION_CONTACT_ID + "=?";
        }
        String[] selectionArgs = {queryArgs};
        List<Contact> contacts = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor;
        cursor = cr.query(phoneUri, new String[]{NUM, NAME, CONTACT_ID}, selection, selectionArgs, null);
        while (cursor.moveToNext()) {
            Contact contact = new Contact(
                    cursor.getString(cursor.getColumnIndex(NAME)),
                    cursor.getString(cursor.getColumnIndex(NUM)),
                    cursor.getString(cursor.getColumnIndex(CONTACT_ID)));
            contacts.add(contact);
        }
        Log.e(TAG, "contacts: " + contacts.toString());
        return contacts;
    }


}
