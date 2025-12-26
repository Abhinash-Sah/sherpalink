package com.example.sherpalink.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ImageViewModel : ViewModel() {

    // Holds the currently selected image resource
    private val _selectedImage = MutableStateFlow<Int?>(null)
    val selectedImage: StateFlow<Int?> get() = _selectedImage

    fun setImage(imageRes: Int) {
        _selectedImage.value = imageRes
    }

    fun clearImage() {
        _selectedImage.value = null
    }
}
