package com.example.airquality.ui.usefulInfo

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.example.airquality.R
import com.example.airquality.databinding.FragmentUsefulInfoBinding
import com.example.airquality.utils.observe
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *  UsefulInfoFragment class used for showing useful info cards
 */

class UsefulInfoFragment : Fragment(R.layout.fragment_useful_info) {

    private val binding: FragmentUsefulInfoBinding by viewBinding()
    private val usefulViewModel: UsefulInfoViewModel by viewModel()
    private val multiTypeAdapter = MultiTypeAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        multiTypeAdapter.apply {
            register(
                InfoCardViewBinder {
                    UsefulInfoDetailedDialog.newInstance(it)
                        .show(childFragmentManager, this.javaClass.name)
                }
            )
        }
        binding.recyclerView.adapter = multiTypeAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        usefulViewModel.getData()
        observeViewModel()
    }

    private fun observeViewModel() = with(usefulViewModel) {
        with(binding) {
            observe(data, viewLifecycleOwner){
                multiTypeAdapter.items = it
                multiTypeAdapter.notifyDataSetChanged()
            }
        }
    }



}