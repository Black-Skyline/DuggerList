package com.example.todolist.activities

import android.os.Bundle
import com.example.todolist.R
import com.example.todolist.fragments.entrance.LoginFragment

/**
 * @author: workDog
 * @description: 这是用于展示Login Page和Register Page的入口Activity
 */
class EntranceActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrance)
        if (savedInstanceState == null)
            supportFragmentManager
                .beginTransaction()
                .add(R.id.entrance_fragment_place, LoginFragment(), "登录fragment实例")
                .commit()
    }

    // 这种写法被废弃了
//    override fun onBackPressed() {
//        if (supportFragmentManager.backStackEntryCount > 0) {
//            // 出栈下一个 Fragment，并显示出来
//            supportFragmentManager.popBackStackImmediate()
//        } else {
//            super.onBackPressed()
//        }
//    }
}