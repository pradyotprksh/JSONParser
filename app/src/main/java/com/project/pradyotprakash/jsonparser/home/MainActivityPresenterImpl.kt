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

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import com.google.firebase.firestore.FirebaseFirestore
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsOptions
import com.project.pradyotprakash.jsonparser.R
import com.project.pradyotprakash.jsonparser.util.logd
import com.skydoves.whatif.whatIfNotNull
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.nio.charset.Charset
import javax.inject.Inject


/**
 * Main home presenter implementation
 * where all the functionality are
 * done
 */
class MainActivityPresenterImpl @Inject constructor() : MainActivityPresenter {

    lateinit var mContext: Activity
    @Inject lateinit var mView: MainActivityView
    private lateinit var mFirestore: FirebaseFirestore

    @Inject
    internal fun MainActivityPresenterImpl(activity: Activity) {
        mContext = activity
        mFirestore = FirebaseFirestore.getInstance()
    }

    override fun checkForPermissions() {
        val quickPermissionsOption = QuickPermissionsOptions(
            handleRationale = true,
            rationaleMessage = mContext.getString(R.string.permission_title),
            permanentlyDeniedMessage = mContext.getString(R.string.permission_statement),
            handlePermanentlyDenied = true
        )

        mContext.runWithPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE, options = quickPermissionsOption) {
            mView.showFileChooser()
        }
    }

    @SuppressLint("Recycle")
    override fun getFilePath(mContext: Context, data: Intent?): String {
        data.whatIfNotNull {
            val uri: Uri = data!!.data!!
            val uriString = uri.toString()
            val myFile = File(uriString)
            val path: String = data.dataString!!
            var displayName: String? = null

            if (uriString.startsWith("content://")) {
                var cursor: Cursor? = null
                try {
                    cursor = mContext.contentResolver.query(
                        uri,
                        null,
                        null,
                        null,
                        null)
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName =
                            cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                } finally {
                    cursor!!.close()
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.name
            }
            return ("$path/$displayName")
        }
        return ""
    }

    override fun readFileData(path: String) {
        try {
            val selectedFile = File(
                Environment.getExternalStorageDirectory(),
                path
            )
            val stream = FileInputStream(selectedFile)
            var jsonStr: String? = null
            try {
                val fc: FileChannel = stream.channel
                val bb: MappedByteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size())
                jsonStr = Charset.defaultCharset().decode(bb).toString()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                stream.close()
            }
            jsonStr.whatIfNotNull {
                val jsonObj = JSONObject(jsonStr!!)
                mContext.logd("JSONObject$jsonObj")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun start() {
        checkForPermissions()
    }

    override fun stop() {

    }

}