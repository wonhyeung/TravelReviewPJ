package com.won.travelreviewpj.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.won.travelreviewpj.databinding.ItemTravelBinding

class MapAdapter(private var arrayList: MutableList<Map>) :
    RecyclerView.Adapter<MapAdapter.ItemHolder>() {

    inner class ItemHolder(val binding: ItemTravelBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding = ItemTravelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        var mapData = arrayList[position]
        with(holder.binding) {
            ivItemTravelWishlist.setImageResource(mapData.recordImage)
            tvItemTravelWishlistTitle.text = mapData.recordTitle
            tvItemTravelWishlistCompanion.text = mapData.recordCompanion
            root.setOnClickListener {
                val action =
                    MapFragmentDirections.actionFragmentMapToFragmentRecordDetail()
                it.findNavController().navigate(action)
            }
        }

    }

    override fun getItemCount(): Int = arrayList.size


}