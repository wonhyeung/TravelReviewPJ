package com.won.travelreviewpj.record.diary

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.view.clicks
import com.won.travelreviewpj.R
import com.won.travelreviewpj.common.MODE_ADD
import com.won.travelreviewpj.common.RECORD_ID
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.databinding.FragmentRecordDiaryBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch

/**
 * Record diary fragment
 *
 * 방문한 여행지 확인
 */
class RecordDiaryFragment : ViewBindingBaseFragment<FragmentRecordDiaryBinding>(FragmentRecordDiaryBinding::inflate) {

    private var diaryGridLayoutManager: GridLayoutManager? = null
    private val recordViewModel: RecordDiaryViewModel by viewModels()
    private lateinit var diariesAdapter: RecordDiaryAdapter
    private val compositeDisposable = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fieldSetup()
    }
    private fun fieldSetup() {
        notifyRecordDiaries()
        with(binding) {
            val recordId = arguments?.getString(RECORD_ID)
            recordId?.let {
                lifecycleScope.launch {
                    recordViewModel.findRecords(it)
                }
                tbRecordFolder.setNavigationOnClickListener {
                    val action =
                        RecordDiaryFragmentDirections.actionFragmentRecordDiaryToFragmentRecord()
                    findNavController().navigate(action)
                }
                compositeDisposable.add(
                tbRecordFolder.menu.findItem(R.id.btn_add).clicks()
                    .subscribe{
                        val action =
                            RecordDiaryFragmentDirections.actionFragmentRecordDiaryToRecordDiaryUpdateFragment(
                                recordId, MODE_ADD
                            )
                        findNavController().navigate(action)
                    }

                )
                getFireStoreDiary(recordId)
                getField(recordId)
            }
            notifyRecordDiaries()
            observeRecordDiaries()
        }
    }

    private fun getField(recordId: String) {
        recordId.let {
            recordViewModel.records.observe(viewLifecycleOwner) { record ->
                binding.tbRecordFolder.title = record?.name
            }
        }
    }

    private fun getFireStoreDiary(recordId: String) {
        lifecycleScope.launch {
            recordViewModel.getRecordDiaries(recordId)
        }
    }

    private fun notifyRecordDiaries(recordDiary : List<RecordDiary> = emptyList()) {
        val recordId = arguments?.getString(RECORD_ID)
        diariesAdapter = RecordDiaryAdapter(
            R.layout.item_notepad,
            recordId.toString(), recordViewModel, this
        )
        with(binding) {
            diaryGridLayoutManager =
                GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
            rvRecordSelect.apply {
                this.layoutManager = diaryGridLayoutManager
                this.setHasFixedSize(true)
                this.adapter = diariesAdapter
            }
            diariesAdapter.replaceRecordDiaryInfo(recordDiary)
        }
    }

    private fun observeRecordDiaries() {
        recordViewModel.diaries.observe(viewLifecycleOwner) { diaries ->
            notifyRecordDiaries(diaries)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }
}