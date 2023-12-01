package com.won.travelreviewpj.record

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.won.travelreviewpj.R
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.databinding.FragmentRecordBinding
import kotlinx.coroutines.launch

class RecordFragment :
    ViewBindingBaseFragment<FragmentRecordBinding>(FragmentRecordBinding::inflate) {

    private var recordGridLayoutManager: GridLayoutManager? = null
    private lateinit var recordAdapter: RecordAdapter
    private val recordViewModel: RecordViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notifyRecord()
        bindingSet()
        getFireStoreRecord()
        observeRecords()

    }

    private fun bindingSet() {
        with(binding) {
            tbRecord.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.btn_add) {
                    dialogShow()
                }
                true
            }
        }
    }

    private fun dialogShow() {
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.item_folder_create, null)
        val editText = dialogView.findViewById<EditText>(R.id.et_item_folder_create)
        val button = dialogView.findViewById<Button>(R.id.mb_item_folder_create)
        editText.requestFocus()
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
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

        imageSelector(dialogView)

        button.setOnClickListener {
            val record = Record("", editText.text.toString(), selectedImageResource)
            insertFireStore(record)
            dialog.dismiss()
        }

    }

    private var selectedImageResource: Int = R.drawable.empty_folder_red
    private fun imageSelector(dialog: View) {

        val imageViews = listOf(
            Pair(
                dialog.findViewById(R.id.iv_item_folder_red),
                R.drawable.empty_folder_red
            ),
            Pair(
                dialog.findViewById(R.id.iv_item_folder_orange),
                R.drawable.empty_folder_orange
            ),
            Pair(
                dialog.findViewById(R.id.iv_item_folder_yellow),
                R.drawable.empty_folder_yellow
            ),
            Pair(
                dialog.findViewById(R.id.iv_item_folder_green),
                R.drawable.empty_folder_green
            ),
            Pair(
                dialog.findViewById(R.id.iv_item_folder_blue),
                R.drawable.empty_folder_blue
            ),
            Pair(
                dialog.findViewById(R.id.iv_item_folder_purple),
                R.drawable.empty_folder_purple
            ),
            Pair(
                dialog.findViewById<ImageView>(R.id.iv_item_folder_pink),
                R.drawable.empty_folder_pink
            ),
        )
        for ((imageView, imageResource) in imageViews) {
            imageView.setOnClickListener {
                for ((iv, _) in imageViews) {
                    Log.e("image", "$iv")
                    iv.isSelected = false
                    iv.setBackgroundResource(0)
                }
                Log.e("image", "")
                it.isSelected = true
                it.setBackgroundResource(R.drawable.baseline_check_24)

                selectedImageResource = imageResource // 선택한 이미지 리소스 ID 저장

            }
        }

    }

    private fun insertFireStore(record: Record) {
        lifecycleScope.launch {
            recordViewModel.insertRecord(record)
            recordViewModel.getRecords()
            Toast.makeText(context, "파일 생성", Toast.LENGTH_SHORT).show()
            Log.e("insertRecord", "${record.id}, ${record.image}, ${record.name}")

        }
    }


    private fun getFireStoreRecord() {
        lifecycleScope.launch {
            recordViewModel.getRecords()
        }
    }

    private fun notifyRecord(records: List<Record> = emptyList()) {
        recordAdapter = RecordAdapter(R.layout.item_folder, recordViewModel, this)
        with(binding) {
            recordGridLayoutManager =
                GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
            rvRecordSelect.layoutManager = recordGridLayoutManager
            rvRecordSelect.setHasFixedSize(true)
            recordAdapter.notifyRecordList(records)

            rvRecordSelect.adapter = recordAdapter
        }
    }

    private fun observeRecords() {
        recordViewModel.records.observe(viewLifecycleOwner) { records ->
            recordAdapter.notifyRecordList(records)
        }
    }

}


