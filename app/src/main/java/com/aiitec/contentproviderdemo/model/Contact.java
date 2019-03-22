package com.aiitec.contentproviderdemo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ailibin
 * @version 1.0
 * @createTime 2019/3/21.
 */

public class Contact implements Parcelable {

    /**
     * 显示数据拼音的首字母
     */
    private String sortLetters;
    private String imagePath;
    private String identify;
    private String name;
    private Long userId;
    private String contactId;
    private boolean isSelected;
    private String mobile;
    /**
     * 地址
     */
    private List<String> addresses = new ArrayList<>();
    /**
     * 电话号码
     */
    private List<String> phones = new ArrayList<>();
    /**
     * 邮箱
     */
    private List<String> emails = new ArrayList<>();
    /**
     * 公司名称或者组织名称
     */
    private List<String> companys = new ArrayList<>();
    /**
     * 用户备注
     */
    private String alias;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }


    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public List<String> getCompanys() {
        return companys;
    }

    public void setCompanys(List<String> companys) {
        this.companys = companys;
    }

    public Contact() {

    }

    public Contact(String name, String mobile) {
        this.name = name;
        this.mobile = mobile;
    }


    public Contact(String name, String mobile, String contactId) {
        this.name = name;
        this.mobile = mobile;
        this.contactId = contactId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "sortLetters='" + sortLetters + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", name='" + name + '\'' +
                ", contactId='" + contactId + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sortLetters);
        dest.writeString(this.imagePath);
        dest.writeString(this.identify);
        dest.writeString(this.name);
        dest.writeValue(this.userId);
        dest.writeString(this.contactId);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeString(this.mobile);
        dest.writeStringList(this.addresses);
        dest.writeStringList(this.phones);
        dest.writeStringList(this.emails);
        dest.writeStringList(this.companys);
        dest.writeString(this.alias);
    }

    protected Contact(Parcel in) {
        this.sortLetters = in.readString();
        this.imagePath = in.readString();
        this.identify = in.readString();
        this.name = in.readString();
        this.userId = (Long) in.readValue(Long.class.getClassLoader());
        this.contactId = in.readString();
        this.isSelected = in.readByte() != 0;
        this.mobile = in.readString();
        this.addresses = in.createStringArrayList();
        this.phones = in.createStringArrayList();
        this.emails = in.createStringArrayList();
        this.companys = in.createStringArrayList();
        this.alias = in.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel source) {
            return new Contact(source);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}
