package com.won.travelreviewpj.record.diary

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.won.travelreviewpj.R
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.databinding.FragmentRecordDiaryBinding
import com.won.travelreviewpj.record.Record
import com.won.travelreviewpj.record.RecordAdapter
import kotlinx.coroutines.launch
import kotlin.math.log

class RecordDiaryFragment :
    ViewBindingBaseFragment<FragmentRecordDiaryBinding>(FragmentRecordDiaryBinding::inflate) {


    private var diaryGridLayoutManager: GridLayoutManager? = null
    private val recordViewModel: RecordDiaryViewModel by viewModels()
    private lateinit var adapter: RecordDiaryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with(binding) {
            val recordId = arguments?.getString("recordId")
            recordId?.let {
                lifecycleScope.launch {
                    recordViewModel.findRecords(it)
                }
                tbRecordFolder.setOnMenuItemClickListener { item ->
                    if (item.itemId == R.id.btn_add) {
                        val action =
                            RecordDiaryFragmentDirections.actionFragmentRecordDiaryToRecordDiaryUpdateFragment(
                                recordId
                            )
                        Log.e("recordIdㅈㅈ", recordId)
                        findNavController().navigate(action)
                    }
                    true
                }
                getFireStoreDiary(recordId)
                getField(recordId)
            }
            notifyRecordDiaries()
            observeRecordDiaries()
        }

    }

    private fun getField(recordId : String) {
        recordId.let {
            recordViewModel.records.observe(viewLifecycleOwner) { record ->
                binding.tbRecordFolder.title = record?.name
            }

        }
    }

    private fun getFireStoreDiary(recordId : String) {
        lifecycleScope.launch {
            recordViewModel.getRecordDiaries(recordId)
        }
    }

    private fun notifyRecordDiaries(diaries: List<RecordDiary> = emptyList()) {
        val recordId = arguments?.getString("recordId")
        adapter = RecordDiaryAdapter(R.layout.item_notepad,
            recordId.toString(),recordViewModel, this)
        with(binding) {
            diaryGridLayoutManager =
                GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
            rvRecordSelect.layoutManager = diaryGridLayoutManager
            rvRecordSelect.setHasFixedSize(true)
            adapter.notifyRecordDiaryList(diaries)

            rvRecordSelect.adapter = adapter
        }
    }

    private fun observeRecordDiaries() {
        recordViewModel.diaries.observe(viewLifecycleOwner) { diaries ->
            adapter.notifyRecordDiaryList(diaries)
        }
    }


}