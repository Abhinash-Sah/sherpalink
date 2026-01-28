package com.example.sherpalink.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sherpalink.model.GuideModel
import com.example.sherpalink.repository.GuideRepo

class GuideViewModel(private val repo: GuideRepo) : ViewModel() {

    private val _guides = MutableLiveData<List<GuideModel>>()
    val guides: LiveData<List<GuideModel>> = _guides

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status

    fun addGuide(guide: GuideModel, imageUri: Uri?) {
        repo.addGuide(guide, imageUri) { success, message ->
            _status.postValue(message)
        }
    }

    fun loadGuides() {
        repo.getAllGuides {
            _guides.postValue(it)
        }
    }

    fun deleteGuide(guideId: String) {
        repo.deleteGuide(guideId) { _, message ->
            _status.postValue(message)
        }
    }
}
