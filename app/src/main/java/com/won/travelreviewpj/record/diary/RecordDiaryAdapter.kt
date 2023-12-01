package com.won.travelreviewpj.record.diary

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.won.travelreviewpj.databinding.ItemNotepadBinding
import kotlinx.coroutines.launch

class RecordDiaryAdapter(
    private val recordItemLayout: Int,
    private val recordId: String,
    private val recordViewModel: RecordDiaryViewModel,
    private val lifecycleOwner: LifecycleOwner
) :
    RecyclerView.Adapter<RecordDiaryAdapter.ItemHolder>() {
    private lateinit var arrayList: List<RecordDiary>

    inner class ItemHolder(val binding: ItemNotepadBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding =
            ItemNotepadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val recordDiaryData = arrayList[position]
        with(holder.binding) {
            Glide.with(root.context)
                .load(recordDiaryData.image)
                .into(holder.binding.ivNotepadRecord)
            tvNotepadRecord.text = "#" + recordDiaryData.name
            tvNotepadAddress.text = recordDiaryData.address
            root.setOnClickListener {
                val action =
                    RecordDiaryFragmentDirections.actionFragmentRecordDiaryToRecordDetailFragment(
                        recordDiaryData.id
                    )
                it.findNavController().navigate(action)
            }
            root.setOnLongClickListener { view ->
                Log.e("recordId", recordDiaryData.id)

                MaterialAlertDialogBuilder(view.context)
                    .setMessage("정말로 이 폴더를 삭제하시겠습니까?")
                    .setNegativeButton("아니오") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("네") { dialog, _ ->
                        lifecycleOwner.lifecycleScope.launch {
                            recordViewModel.deleteRecordDiary(recordId, recordDiaryData.id)
                            dialog.dismiss()

                        }
                    }
                    .show()
                true
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyRecordDiaryList(list: List<RecordDiary>) {
        arrayList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = arrayList.size


}