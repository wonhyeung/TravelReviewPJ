package com.won.travelreviewpj.map

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.naver.maps.geometry.LatLng
import com.won.travelreviewpj.databinding.ItemTravelBinding
import com.won.travelreviewpj.record.diary.RecordDiary

class MapAdapter(
    private val recordItemLayout: Int,
    private val itemClicked: (LatLng) -> Unit
) :
    RecyclerView.Adapter<MapAdapter.ItemHolder>() {

    private lateinit var arrayList: List<RecordDiary>

    inner class ItemHolder(val binding: ItemTravelBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding = ItemTravelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        var mapData = arrayList[position]
        with(holder.binding) {
            Glide.with(root.context)
                .load(mapData.image)
                .centerCrop()
                .into(ivItemTravelWishlist)
            tvItemTravelWishlistTitle.text = mapData.name
            tvItemTravelWishlistAddress.text = mapData.address
            root.setOnClickListener {
                val latLng = LatLng(mapData.mapy.toDouble() / 1e6 / 10, mapData.mapx.toDouble() / 1e6 / 10)

                itemClicked(latLng)
            }
        }
    }



override fun getItemCount(): Int = arrayList.size

@SuppressLint("NotifyDataSetChanged")
fun notifyMapDiaryList(list: List<RecordDiary>) {
    arrayList = list
    notifyDataSetChanged()
}
}