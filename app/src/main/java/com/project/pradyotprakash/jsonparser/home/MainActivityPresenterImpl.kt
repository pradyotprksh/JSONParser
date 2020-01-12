package com.project.pradyotprakash.jsonparser.home

import android.app.Activity
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class MainActivityPresenterImpl @Inject constructor() : MainActivityPresenter {

    lateinit var mContext: Activity
    @Inject lateinit var mView: MainActivityView
    private lateinit var dataBase: FirebaseFirestore

    @Inject
    internal fun MainActivityPresenterImpl(activity: Activity) {
        mContext = activity
        dataBase = FirebaseFirestore.getInstance()
    }

    override fun start() {

    }

    override fun stop() {

    }

}