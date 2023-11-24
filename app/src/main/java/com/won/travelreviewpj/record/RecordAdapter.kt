package com.won.travelreviewpj.record

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.won.travelreviewpj.databinding.ItemFolderBinding

class RecordAdapter(
    private val recordItemLayout: Int
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
                    RecordFragmentDirections.actionRecordFragmentToRecordFolderFragment()
                it.findNavController().navigate(action)
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