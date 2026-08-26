package com.smartexpense.android

import android.app.Application
import com.smartexpense.android.data.remote.RetrofitClient

class SmartExpenseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        RetrofitClient.init(this)
    }
}
