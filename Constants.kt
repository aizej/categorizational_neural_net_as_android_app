package com.example.workingcamera

import android.Manifest

object Constants {
    const val TAG = "cameraX"
    const val FILE_NAME_FORMAT = "yy-MM-dd-HH-mm-ss-SSS"
    const val REQUEST_CODE_PERMISION = 123
    val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
}