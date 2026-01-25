package com.example.sherpalink.repository


import android.net.Uri
import com.example.sherpalink.model.GuideModel


interface GuideRepo {


    fun addGuide(
        guide: GuideModel,
        imageUri: Uri?,
        callback: (Boolean, String) -> Unit
    )


    fun getAllGuides(callback: (List<GuideModel>) -> Unit)


    fun deleteGuide(guideId: String, callback: (Boolean, String) -> Unit)
}