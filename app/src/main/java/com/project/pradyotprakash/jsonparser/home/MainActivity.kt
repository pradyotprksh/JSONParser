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


package com.project.pradyotprakash.jsonparser.home

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.project.pradyotprakash.jsonparser.R
import com.project.pradyotprakash.jsonparser.util.AppConstants.Companion.FILE_SELECT_CODE
import com.project.pradyotprakash.jsonparser.util.logd
import com.skydoves.whatif.whatIfNotNull
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


/**
 * Main home page view
 */
class MainActivity : DaggerAppCompatActivity(), MainActivityView {

    @Inject
    lateinit var presenter: MainActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make full screen
        // set theme for notch if sdk is P
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        } else {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        setContentView(R.layout.activity_main)

        logd(getString(R.string.create))

        intViews()
    }

    private fun intViews() {
        presenter.start()

        onClickListener()
    }

    private fun onClickListener() {
        fb_chose_file.setOnClickListener {
            presenter.checkForPermissions()
        }
    }

    override fun showFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/json"
        try {
            startActivityForResult(
                Intent.createChooser(intent, getString(R.string.selector_title)),
                FILE_SELECT_CODE
            )
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                this, getString(R.string.no_file_manager),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun stopAct() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            FILE_SELECT_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    data.whatIfNotNull {
                        val path: String = presenter.getFilePath(this, data)
                        Toast.makeText(
                            this, path,
                            Toast.LENGTH_SHORT
                        ).show()
                        presenter.readFileData(path)
                    }
                } else {
                    Toast.makeText(
                        this, getString(R.string.oops_something_wrong),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

}
