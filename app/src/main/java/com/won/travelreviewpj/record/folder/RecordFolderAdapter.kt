package com.won.travelreviewpj.record.folder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.won.travelreviewpj.databinding.ItemFolderBinding
import com.won.travelreviewpj.databinding.ItemNotepadBinding
import com.won.travelreviewpj.databinding.ItemTravelWishlistBinding
import com.won.travelreviewpj.travel.wishlist.TravelWishlistFragmentDirections

class RecordFolderAdapter(private var arrayList: MutableList<RecordFolder>) :
    RecyclerView.Adapter<RecordFolderAdapter.ItemHolder>() {

    inner class ItemHolder(val binding: ItemNotepadBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding =
            ItemNotepadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        var recordFolderData = arrayList[position]
        with(holder.binding) {
            ivNotepadRecord.setImageResource(recordFolderData.recordImage)
            tvNotepadRecord.text = recordFolderData.recordTitle
            tvNotepadCompanion.text = recordFolderData.recordCompanion
            root.setOnClickListener {
                val action =
                    RecordFolderFragmentDirections.actionRecordFolderFragmentToRecordDetailFragment()
                it.findNavController().navigate(action)
            }
        }
    }

    override fun getItemCount(): Int = arrayList.size


}