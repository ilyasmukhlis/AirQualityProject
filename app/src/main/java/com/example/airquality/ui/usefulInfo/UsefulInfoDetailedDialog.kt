package com.example.airquality.ui.usefulInfo

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.bottomsheetdialogfragment.viewBinding
import androidx.core.os.bundleOf
import com.example.airquality.databinding.DialogDetailedUsefulInfoBinding
import com.example.airquality.domain.`object`.InfoCardData
import com.example.airquality.utils.lazyNone
import com.example.airquality.utils.showFullScreen
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 *  Dialog used for showing useful information about air quality
 */

class UsefulInfoDetailedDialog : BottomSheetDialogFragment() {

    private val binding: DialogDetailedUsefulInfoBinding by viewBinding()

    private val cardInfo: InfoCardData by lazyNone {
        requireArguments().getParcelable(CARD_INFO)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    /**
     * it is required to override onCreateDialog to show full screen dialog
     */

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return showFullScreen(super.onCreateDialog(savedInstanceState))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.titleTextView.text = cardInfo.title
        binding.shortDescriptionTextView.text = cardInfo.description
    }


    /**
     * companion object/static method to call dialog from other fragments/activities
     */

    companion object {
        private const val CARD_INFO = "card_info"

        fun newInstance(cardInfo: InfoCardData) =
            UsefulInfoDetailedDialog().apply {
                arguments =
                    bundleOf(Pair(CARD_INFO, cardInfo))
            }
    }
}