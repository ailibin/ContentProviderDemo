package com.aiitec.contentproviderdemo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import com.aiitec.contentproviderdemo.adapter.ContactAdapter
import com.aiitec.contentproviderdemo.model.Contact
import com.aiitec.contentproviderdemo.model.Item
import com.aiitec.contentproviderdemo.utils.ContactsUtil
import com.aiitec.contentproviderdemo.utils.ScreenUtils
import com.aiitec.contentproviderdemo.utils.StatusBarUtil
import com.aiitec.contentproviderdemo.widget.CharacterParser
import com.aiitec.contentproviderdemo.widget.SideBar
import kotlinx.android.synthetic.main.activity_contact_list.*
import java.util.*


/**
 * @Author: ailibin
 * @Time: 2019/03/21
 * @Description: 联系人列表
 * @Email: ailibin@qq.com
 */
class ContactListActivity : AppCompatActivity() {

    /**
     * 联系人数据
     */
    private var datas = ArrayList<Contact>()
    private var pinyinComparator: PinyinComparator? = null
    private var contactAdapter: ContactAdapter? = null
    private var contactsUtils: ContactsUtil? = null

    private var itemDatas = ArrayList<Item>()
    private var itemImgs = arrayListOf(
        R.drawable.friend_icon_man,
        R.drawable.friend_icon_lady,
        R.drawable.friend_icon_add,
        R.drawable.friend_icon_team,
        R.drawable.friend_icon_wechat
    )
    private var itemStrs = arrayListOf(
        "关注男生",
        "关注女生",
        "我的关注",
        "我的粉丝",
        "邀请微信好友"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_contact_list)
        StatusBarUtil.addStatusBarView(ll_title_bar, R.color.transparent)
        datas = intent.getParcelableArrayListExtra<Contact>("contacts")
        initConfiguration()
        setListener()

    }

    /**
     * 初始化配置信息
     */
    private fun initConfiguration() {

        contactsUtils = ContactsUtil(this)
        //比较器初始化
        pinyinComparator = PinyinComparator()
        //联系人数据初始化
        contactAdapter = ContactAdapter(this, datas)
        //这里还是要设置布局资源id
        contactAdapter?.setItemLayoutId(R.layout.item_contact)
        side_bar.setTextView(tv_contact_select_letter)
        recycler_contact.layoutManager = LinearLayoutManager(this)
        recycler_contact.adapter = contactAdapter
        //解决ScorllView和recycleView的滑动冲突
        recycler_contact.isNestedScrollingEnabled = false

        //swipeRefreshLayout刷新条的颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark)

        filledData(datas!!)
        // 根据a-z进行排序源数据
        Collections.sort(datas, pinyinComparator)
        contactAdapter?.update()

    }

    private fun setListener() {

        //设置右侧字母符号导航触摸监听
        side_bar.setOnTouchingLetterChangedListener { s ->
            //该字母首次出现的位置
            val position = contactAdapter?.getPositionForSection(s[0].toInt())
            val searchHeight = ScreenUtils.getViewWidthAndHeight(ll_search)[1]
            //搜索框的上下边距
            val dp26 = ScreenUtils.dip2px(this, 26f)
            //五个功能高度
            val totalHeight = searchHeight + dp26
            var childRawY = 0
            if (position != null) {
                //recycler_contact的子view的位置
                val childView = recycler_contact.getChildAt(position)
                childRawY = childView.y.toInt()
            }
            if (position != -1) {
                scrollView.smoothScrollTo(0, childRawY + totalHeight)
            }
        }

        ll_search.setOnClickListener {
            //点击搜索框,跳转到另外一个搜索框页面
            val intent = Intent(this, SearchActivity::class.java)
            intent.putParcelableArrayListExtra("contacts", datas)
            startActivity(intent)
        }

        //解决swipeRefresh和scrollview滑动冲突问题
        scrollView.viewTreeObserver.addOnScrollChangedListener {
            swipeRefreshLayout.isEnabled = scrollView.scrollY == 0
        }

        contactAdapter?.setOnRecyclerViewItemClickListener { v, position ->
            //点击联系人列表,跳转到用户详情
            if (datas.size > 0) {
                val contact = datas[position]
                val identify = contact.identify
                val nickname = contact.name
                val bundle = Bundle()
                bundle.putString("identify", identify)
                bundle.putString("nickname", nickname)
//                switchToActivity(UserProfileActivity::class.java, bundle)
            }
        }

        //这里为了测试,长按好友列表弹出对话框进行删除好友
        contactAdapter?.setOnRecyclerViewItemLongClickListener { v, position ->
            //to do something
        }

        swipeRefreshLayout.setOnRefreshListener {
            //下拉刷新
            update()
            Handler().postDelayed({
                //这里不管数据有没有请求成功,三秒后都消失掉
                swipeRefreshLayout.isRefreshing = false
            }, 2000)
        }

    }

    private fun update() {

        App.cachedThreadPool.execute {
            val contacts = contactsUtils?.getContacts(true)
            runOnUiThread {
                if (contacts != null && contacts.size > 0) {
                    swipeRefreshLayout.isRefreshing = false
                    datas?.clear()
                    datas?.addAll(contacts)
                    filledData(datas)
                    // 根据a-z进行排序源数据
                    Collections.sort(datas, pinyinComparator)
                    contactAdapter?.update()
                }
            }
        }

//        val contacts = contactsUtils?.getContacts(true)
//        if (contacts != null && contacts.size > 0) {
//            datas?.clear()
//            datas?.addAll(contacts)
//            filledData(datas)
//            // 根据a-z进行排序源数据
//            Collections.sort(datas, pinyinComparator)
//            contactAdapter?.update()
//        }

    }

    /**
     * 为ListView填充数据
     *
     * @param data
     * @return
     */
    private fun filledData(data: List<Contact>) {
        SideBar.b.clear()
        for (i in data.indices) {
            val contact = data[i]
            val characterParser = CharacterParser()
            //汉字转换成拼音
            var pinyin = if (!TextUtils.isEmpty(contact.alias)) {
                //备注不为空,就按照备注来
                characterParser.getSelling(contact.alias)
//                Pinyin.toPinyin( contact.alias.toCharArray()[0])
            } else {
                characterParser.getSelling(contact.name)
//                Pinyin.toPinyin(contact.name.toCharArray()[0])
            }
            val sortString = pinyin.substring(0, 1).toUpperCase()

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]".toRegex())) {
                contact.sortLetters = sortString.toUpperCase()
            } else {
                contact.sortLetters = "#"
            }
            if (!SideBar.b.contains(contact.sortLetters)) {
                SideBar.b.add(contact.sortLetters)
            }
        }
        Collections.sort(SideBar.b, PinyinComparator2())
        side_bar.postInvalidate()
    }

    internal inner class PinyinComparator : Comparator<Contact> {

        override fun compare(o1: Contact, o2: Contact): Int {
            //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
            return when {
                "#" == o2.sortLetters -> -1
                "#" == o1.sortLetters -> 1
                else -> o1.sortLetters.compareTo(o2.sortLetters)
            }
        }
    }

    internal inner class PinyinComparator2 : Comparator<String> {

        override fun compare(o1: String, o2: String): Int {
            //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
            return when {
                "#" == o2 -> -1
                "#" == o1 -> 1
                else -> o1.compareTo(o2)
            }
        }
    }


}