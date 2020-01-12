/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.project.pradyotprakash.jsonparser

import android.accounts.AccountManager
import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.project.pradyotprakash.jsonparser.dagger.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

class ApplicationManager: DaggerApplication(), Application.ActivityLifecycleCallbacks {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    private lateinit var applicationManager: ApplicationManager
    private var mAccountManager: AccountManager? = null

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)
        return appComponent
    }

    override fun onActivityPaused(activity: Activity?) {

    }

    override fun onActivityResumed(activity: Activity?) {

    }

    override fun onActivityStarted(activity: Activity?) {

    }

    override fun onActivityDestroyed(activity: Activity?) {

    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

    }

    override fun onActivityStopped(activity: Activity?) {

    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {

    }

    fun getInstance(): ApplicationManager {
        return applicationManager
    }

    override fun onCreate() {
        super.onCreate()
        applicationManager = this
        registerActivityLifecycleCallbacks(this)
        mAccountManager = AccountManager.get(applicationContext)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        System.gc()
    }

}