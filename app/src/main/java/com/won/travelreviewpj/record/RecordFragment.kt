package com.won.travelreviewpj.record

import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.view.clicks
import com.won.travelreviewpj.R
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.databinding.FragmentRecordBinding
import com.won.travelreviewpj.databinding.ItemFolderCreateBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch

/**
 * Record fragment
 *
 * 방문한 여행지 저장할 폴더 생성
 */
class RecordFragment : ViewBindingBaseFragment<FragmentRecordBinding>(FragmentRecordBinding::inflate) {

    private var recordGridLayoutManager: GridLayoutManager? = null
    private lateinit var recordAdapter: RecordAdapter
    private val recordViewModel: RecordViewModel by viewModels()
    private val compositeDisposable = CompositeDisposable()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fieldSetup()
        getFireStoreRecord()
        observeRecords()
    }

    private fun fieldSetup() {
        notifyRecords()
        with(binding) {
            compositeDisposable.add(
                tbRecord.menu.findItem(R.id.btn_add).clicks()
                    .subscribe {
                        dialogShow()
                    }
            )
        }
    }

    private fun dialogShow() {
        val dialogBinding = ItemFolderCreateBinding.inflate(LayoutInflater.from(context))
        dialogBinding.etItemFolderCreate.requestFocus()
        val dialog = AlertDialog.Builder(context)
            .setView(dialogBinding.root)
            .create()

        dialog.window?.apply {
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            setGravity(Gravity.BOTTOM)
        }
        dialog.show()
        imageSelector(dialogBinding)

        compositeDisposable.add(
            dialogBinding.mbItemFolderCreate.clicks()
                .subscribe {
                    val record = Record(
                        "",
                        dialogBinding.etItemFolderCreate.text.toString(),
                        selectedImageResource
                    )
                    insertFireStore(record)
                    dialog.dismiss()
                }
        )
    }

    /**
     * Selected image resource
     * 폴더 생성
     */
    private var selectedImageResource: Int = R.drawable.ic_folder_red
    private fun imageSelector(dialog: ItemFolderCreateBinding) {

        val imageViews = listOf(
            Pair(
                dialog.ivItemFolderRed,
                R.drawable.ic_folder_red
            ),
            Pair(
                dialog.ivItemFolderOrange,
                R.drawable.ic_folder_orange
            ),
            Pair(
                dialog.ivItemFolderYellow,
                R.drawable.ic_folder_yellow
            ),
            Pair(
                dialog.ivItemFolderGreen,
                R.drawable.ic_folder_green
            ),
            Pair(
                dialog.ivItemFolderBlue,
                R.drawable.ic_folder_blue
            ),
            Pair(
                dialog.ivItemFolderPurple,
                R.drawable.ic_folder_purple
            ),
            Pair(
                dialog.ivItemFolderPink,
                R.drawable.ic_folder_pink
            ),
        )
        for ((imageView, imageResource) in imageViews) {
            compositeDisposable.add(
                imageView.clicks()
                    .subscribe {
                        for ((iv, _) in imageViews) {
                            iv.isSelected = false
                            iv.setBackgroundResource(0)
                        }
                        imageView.isSelected = true
                        imageView.setBackgroundResource(R.color.colorButton)
                        selectedImageResource = imageResource
                    }
            )
        }
    }

    private fun insertFireStore(record: Record) {
        lifecycleScope.launch {
            recordViewModel.insertRecord(record)
            recordViewModel.getRecords()
        }
    }


    private fun getFireStoreRecord() {
        lifecycleScope.launch {
            recordViewModel.getRecords()
        }
    }

    private fun notifyRecords(records: List<Record> = emptyList()) {
        recordAdapter = RecordAdapter(R.layout.item_folder, recordViewModel, this)
        with(binding) {
            recordGridLayoutManager =
                GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)

            rvRecordSelect.apply {
                this.layoutManager = recordGridLayoutManager
                this.setHasFixedSize(true)
                this.adapter = recordAdapter
            }
            recordAdapter.replaceRecordInfo(records)

        }
    }

    private fun observeRecords() {
        recordViewModel.records.observe(viewLifecycleOwner) { records ->
            notifyRecords(records)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

}


