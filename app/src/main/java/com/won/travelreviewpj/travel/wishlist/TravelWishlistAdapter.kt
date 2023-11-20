package com.won.travelreviewpj.travel.wishlist

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.won.travelreviewpj.databinding.ItemTravelWishlistBinding
import com.won.travelreviewpj.travel.TravelEntity

class TravelWishlistAdapter(
    var arrayList: MutableList<TravelWishlist>

) :
    RecyclerView.Adapter<TravelWishlistAdapter.ItemHolder>() {
    inner class ItemHolder(val binding: ItemTravelWishlistBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding =
            ItemTravelWishlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        Log.e("onCreateViewHOlder", "onCreateViewHOlder")
        return ItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        Log.e("onBIndViewHOlder", "onBIndViewHOlder")
        var item = arrayList[position]
        with(holder.binding) {
            tvItemTravelWishlistTitle.text = item.wishlistTitle
            tvItemTravelWishlistLocation.text = item.wishlistAddress
            Glide.with(holder.itemView.context)
                .load(item.wishlistImage)
                .centerCrop()
                .into(ivItemTravelWishlist)
            root.setOnClickListener {
                val action =
                    TravelWishlistFragmentDirections.actionFragmentTravelWishlistToFragmentTravelPlanUpdate()
                it.findNavController().navigate(action)
            }
        }
    }

    override fun getItemCount(): Int = arrayList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setTravelWishList(travelWishLists: List<TravelWishlist>) {
        arrayList = travelWishLists.toMutableList()
        notifyDataSetChanged()
    }
}


