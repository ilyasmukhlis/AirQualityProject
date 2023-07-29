package com.example.airquality.ui.usefulInfo

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.airquality.R
import com.example.airquality.domain.`object`.InfoCardData


/**
 * UsefulInfoViewModel class responsible for all of the logic in [UsefulInfoFragment], [UsefulInfoDetailedDialog]
 */

@SuppressLint("StaticFieldLeak")
class UsefulInfoViewModel(
    application: Application
) : AndroidViewModel(application) {

    val context = getApplication<Application>()

    private val _data = MutableLiveData<List<InfoCardData>>()
    val data: LiveData<List<InfoCardData>> = _data

    /**
     *  data delegate, which returns the list of useful info cards
     */

    fun getData() {
        val cards = mutableListOf<InfoCardData>().apply {
            add(
                InfoCardData(
                    title = "What is AQI?",
                    description = context.getString(R.string.lorem_ipsum_text)
                )
            )
            add(
                InfoCardData(
                    title = "AQI Standard in Our App",
                    description = context.getString(R.string.lorem_ipsum_text)
                )
            )
            add(
                InfoCardData(
                    title = "Particles Definition",
                    description = context.getString(R.string.lorem_ipsum_text)
                )
            )
            add(
                InfoCardData(
                    title = "Contacts",
                    description = context.getString(R.string.lorem_ipsum_text)
                )
            )
        }
        _data.value = cards
    }


}