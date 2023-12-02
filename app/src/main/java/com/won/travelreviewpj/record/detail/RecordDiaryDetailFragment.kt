package com.won.travelreviewpj.record.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.won.travelreviewpj.R
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.databinding.FragmentRecordDiaryDetailBinding
import kotlinx.coroutines.launch


class RecordDiaryDetailFragment : ViewBindingBaseFragment<FragmentRecordDiaryDetailBinding>(
    FragmentRecordDiaryDetailBinding::inflate
) {
    private val viewModel: RecordDiaryDetailViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fieldSetUp()
    }

    private fun fieldSetUp() {
        val id = arguments?.getString("folderId")
        val recordId = arguments?.getString("recordId")
        with(binding) {
            id?.let {
                viewModel.findRecordDiary(id).observe(viewLifecycleOwner) { recordDiary ->
                    Glide.with(requireContext())
                        .load(recordDiary?.image)
                        .centerCrop()
                        .into(ivRecordDiaryDetailImage)
                    tbRecordDiaryDetail.title = recordDiary?.name
                    tvRecordDiaryDetailAddressInfo.text = recordDiary?.address
                    tvRecordDiaryDetailCompanionInfo.text = recordDiary?.companion
                    tvRecordDiaryDetailStartDate.text = recordDiary?.startDate
                    tvRecordDiaryDetailDateLine.visibility = View.VISIBLE
                    tvRecordDiaryDetailEndDate.text = recordDiary?.endDate
                    tvRecordDiaryDetailDiaryInfo.text = recordDiary?.diary

                    tbRecordDiaryDetail.setOnMenuItemClickListener { item ->
                        if (item.itemId == R.id.btn_build) {
                            val action =
                                RecordDiaryDetailFragmentDirections.actionRecordDetailFragmentToRecordDiaryUpdateFragment(
                                    recordId.toString(),id, "edit"
                                )
                            findNavController().navigate(action)
                        }
                        true
                    }
                }
            }
        }
    }
}