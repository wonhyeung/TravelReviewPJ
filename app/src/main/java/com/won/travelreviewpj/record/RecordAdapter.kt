package com.won.travelreviewpj.record

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.won.travelreviewpj.databinding.ItemFolderBinding
import kotlinx.coroutines.launch

class RecordAdapter(
    private val recordItemLayout: Int,
    private val recordViewModel: RecordViewModel,
    private val lifecycleOwner: LifecycleOwner,
) :
    RecyclerView.Adapter<RecordAdapter.ItemHolder>() {
    private lateinit var arrayList: List<Record>

    inner class ItemHolder(val binding: ItemFolderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding =
            ItemFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val folderData = arrayList[position]
        with(holder.binding) {
            ivFolderImage.setImageResource(folderData.image)
            tvFolderName.text = folderData.name
            root.setOnClickListener {
                val action =
                    RecordFragmentDirections.actionRecordFragmentToRecordDiaryFragment(folderData.id)
                it.findNavController().navigate(action)
                Log.e("folderDataId", folderData.id)
            }
            root.setOnLongClickListener { view ->
                MaterialAlertDialogBuilder(view.context)
                    .setMessage("정말로 이 폴더를 삭제하시겠습니까?")
                    .setNegativeButton("아니오") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("네") { dialog, _ ->
                        lifecycleOwner.lifecycleScope.launch {
                            recordViewModel.deleteRecord(folderData)
                            dialog.dismiss()
                        }
                    }
                    .show()
                true
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyRecordList(list: List<Record>) {
        arrayList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = arrayList.size
}