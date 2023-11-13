package com.won.travelreviewpj.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.won.travelreviewpj.databinding.ItemFolderBinding

class RecordAdapter(private var arrayList: MutableList<Record>) :
    RecyclerView.Adapter<RecordAdapter.ItemHolder>() {
    inner class ItemHolder(val binding: ItemFolderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding =
            ItemFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        var folderData = arrayList[position]
        with(holder.binding) {
            ivFolderImage.setImageResource(folderData.folderImage)
            tvFolderName.text = folderData.folderName
            root.setOnClickListener {
                val action =
                    RecordFragmentDirections.actionRecordFragmentToRecordFolderFragment()
                it.findNavController().navigate(action)
            }
        }
    }

    override fun getItemCount(): Int = arrayList.size
}