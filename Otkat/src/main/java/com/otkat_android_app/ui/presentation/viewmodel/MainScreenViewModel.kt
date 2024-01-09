package com.otkat_android_app.ui.presentation.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otkat_android_app.ui.models.ImageState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel: ViewModel() {

    private val _imageState = MutableStateFlow(ImageState())
    val imageState: StateFlow<ImageState>
        get() = _imageState

    fun storePhotoInGallery(bitmap: Bitmap) {
        viewModelScope.launch {
            updateCapturedPhotoState(bitmap)
        }
    }

    private fun updateCapturedPhotoState(updatedPhoto: Bitmap?) {
        _imageState.value.capturedImage?.recycle()
        _imageState.value = _imageState.value.copy(capturedImage = updatedPhoto)
    }

    override fun onCleared() {
        _imageState.value.capturedImage?.recycle()
        super.onCleared()
    }
}