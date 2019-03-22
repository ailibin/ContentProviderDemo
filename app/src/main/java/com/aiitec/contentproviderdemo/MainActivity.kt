package com.aiitec.contentproviderdemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.aiitec.contentproviderdemo.model.Contact
import com.aiitec.contentproviderdemo.utils.ContactsUtil
import com.aiitec.contentproviderdemo.utils.PermissionsUtils
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        var TAG = "ailibin"
    }

    private var contactsUtils: ContactsUtil? = null

    private var permissionsUtils: PermissionsUtils? = null
    private var REQUEST_EXTERNAL = 0x130
    private var contactListDatas = ArrayList<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        initPermission()
        contactsUtils = ContactsUtil(this)
        btn_jump_user.setOnClickListener {
            //跳转到联系人用户列表页面,先授权
            permissionsUtils?.requestPermissions(
                REQUEST_EXTERNAL,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_CONTACTS
            )
        }

    }


    private fun initPermission() {

        permissionsUtils = PermissionsUtils(this)
        permissionsUtils?.setOnPermissionsListener(object : PermissionsUtils.OnPermissionsListener {
            override fun onPermissionsSuccess(requestCode: Int) {
                if (requestCode == REQUEST_EXTERNAL) {
                    //写内部存储,开个线程访问
                    App.cachedThreadPool.execute {
                        contactListDatas.clear()
                        contactListDatas.addAll(contactsUtils!!.getContacts(true))
                        runOnUiThread {
                            //跳转到联系人列表
                            val intent = Intent(this@MainActivity, ContactListActivity::class.java)
                            intent.putParcelableArrayListExtra("contacts", contactListDatas)
                            startActivity(intent)
                        }
                    }
                }
            }

            override fun onPermissionsFailure(requestCode: Int) {

            }

        })
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsUtils?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}
