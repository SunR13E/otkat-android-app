package com.otkat_android_app.ui.domain

import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import com.otkat_android_app.ui.utils.rotateBitmap

class OnImageCapturedCallback(
    private val onPhotoCaptured: (Bitmap) -> Unit
) : ImageCapture.OnImageCapturedCallback() {

    override fun onCaptureSuccess(image: ImageProxy) {
        val correctedBitmap = image
            .toBitmap()
            .rotateBitmap(image.imageInfo.rotationDegrees)

        onPhotoCaptured(correctedBitmap)
        image.close()
    }

    override fun onError(exception: ImageCaptureException) {
        Log.e(TAG, ERROR_MESSAGE, exception)
    }

    private companion object {
        const val TAG = "OnImageCapturedCallback"
        const val ERROR_MESSAGE = "Произошла ошибка при получении изображения"
    }
}