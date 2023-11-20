package com.won.travelreviewpj.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.won.travelreviewpj.databinding.ItemTravelBinding
import com.won.travelreviewpj.map.MapFragmentDirections

class ScheduleAdapter(private var arrayList: MutableList<Schedule>) :
    RecyclerView.Adapter<ScheduleAdapter.ItemHolder>() {

    inner class ItemHolder(val binding: ItemTravelBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding = ItemTravelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        var scheduleData = arrayList[position]
        with(holder.binding) {
            ivItemTravelWishlist.setImageResource(scheduleData.recordImage)
            tvItemTravelWishlistTitle.text = scheduleData.recordTitle
            tvItemTravelWishlistCompanion.text = scheduleData.recordCompanion
            root.setOnClickListener {
                val action =
                    ScheduleFragmentDirections.actionScheduleFragmentToScheduleDetailFragment()
                it.findNavController().navigate(action)
            }
        }

    }

    override fun getItemCount(): Int = arrayList.size


}