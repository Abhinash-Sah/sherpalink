package com.example.sherpalink.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sherpalink.model.GuideModel
import com.example.sherpalink.repository.GuideRepo

class GuideViewModelFactory(private val repo: GuideRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GuideViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GuideViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class GuideViewModel(val repo: GuideRepo) : ViewModel() {

    private val _guides = MutableLiveData<List<GuideModel>>(emptyList())
    val guides: LiveData<List<GuideModel>> = _guides

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status

    // Load guides from Repository
    fun loadGuides() {
        repo.getAllGuides { list ->
            _guides.postValue(list)
        }
    }

    // Add guide and then refresh the list
    fun addGuide(guide: GuideModel, imageUri: Uri?) {
        repo.addGuide(guide, imageUri) { success, message ->
            _status.postValue(message)
            if (success) {
                loadGuides() // Refresh list after adding
            }
        }
    }

    // Delete guide and then refresh the list
    fun deleteGuide(guideId: String) {
        repo.deleteGuide(guideId) { success, message ->
            _status.postValue(message)
            if (success) {
                loadGuides() // Refresh list after deleting
            }
        }
    }
}