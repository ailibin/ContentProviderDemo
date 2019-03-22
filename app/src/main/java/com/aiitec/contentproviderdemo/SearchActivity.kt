package com.aiitec.contentproviderdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import com.aiitec.contentproviderdemo.adapter.ProfileSummaryAdapter
import com.aiitec.contentproviderdemo.model.Contact
import com.aiitec.contentproviderdemo.utils.ContactsUtil
import com.aiitec.contentproviderdemo.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_search_friend.*

/**
 * @author ailibin
 * @description 搜索页面
 * @time 2019/03/21
 */
class SearchActivity : AppCompatActivity() {

    companion object {
        var TAG = "ailibin"
    }

    private var searchList = ArrayList<Contact>()
    private var transferList = ArrayList<Contact>()
    private var searchContent: String? = null
    private var contactsUtils: ContactsUtil? = null
    private var adapter: ProfileSummaryAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_friend)
        transferList = intent.getParcelableArrayListExtra<Contact>("contacts")
        contactsUtils = ContactsUtil(this)
        initAdapter()
        setListener()
    }


    private fun initAdapter() {

        adapter = ProfileSummaryAdapter(this, searchList)
        recycler_search.layoutManager = LinearLayoutManager(this)
        recycler_search.adapter = adapter

    }

    private fun setListener() {

        edt_search.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND || event != null && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                searchContent = edt_search.text.toString().trim()
                if (TextUtils.isEmpty(searchContent)) {
                    ToastUtil.show(this, "请输入搜索内容")
                } else {
//                    toSearchContactListExact(searchContent)
                    toSearchContactListDim(searchContent)
                }
                true
            } else false
        }

        tv_cancel.setOnClickListener {
            finish()
        }

        edt_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val content = p0.toString()
//                toSearchContactListExact(content)
                toSearchContactListDim(content)
            }
        })

    }

    /**
     * 精确查询
     */
    private fun toSearchContactListExact(searchKey: String?) {
        searchList.clear()
        //这里还是按照联系人的名称搜索了
        if (TextUtils.isEmpty(searchKey)) {
            setView()
            return
        }
        val contacts = contactsUtils?.getContacts(searchKey, ContactsUtil.QueryType.BY_NAME)
        if (contacts != null) {
            searchList.addAll(contacts)
            setView()
        }

    }

    /**
     * 模糊查询
     */
    private fun toSearchContactListDim(searchKey: String?) {
        searchList.clear()
        if (TextUtils.isEmpty(searchKey)) {
            setView()
            return
        }
        if (transferList.size <= 0) {
            return
        }
        for (i in transferList.indices) {
            val contact = transferList[i]
            //如果用户的备注不为空,就按照备注搜,反之就按照昵称搜
            var searchContent = if (!TextUtils.isEmpty(contact.alias)) {
                contact.alias
            } else {
                contact.name
            }
            if (searchContent.indexOf(searchKey!!) >= 0) {
                searchList.add(contact)
            }
        }
        setView()
    }


    private fun setView() {

        ll_container.visibility = View.VISIBLE
        if (searchList.size > 0) {
            recycler_search.visibility = View.VISIBLE
            include_line.visibility = View.VISIBLE
            tv_title.visibility = View.VISIBLE
            adapter?.notifyDataSetChanged()
        } else {
            recycler_search.visibility = View.GONE
            include_line.visibility = View.GONE
            tv_title.visibility = View.GONE
        }

    }


}