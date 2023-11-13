package com.won.travelreviewpj.travel.wishlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.won.travelreviewpj.databinding.ItemTravelWishlistBinding

class TravelWishlistAdapter(private var arrayList: MutableList<TravelWishlist>) :
    RecyclerView.Adapter<TravelWishlistAdapter.ItemHolder>() {
    inner class ItemHolder(val binding: ItemTravelWishlistBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding =
            ItemTravelWishlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        var wishlistData = arrayList[position]
        with(holder.binding) {
            ivItemTravelWishlist.setImageResource(wishlistData.travelImage)
            tvItemTravelWishlistTitle.text = wishlistData.travelTitle
            tvItemTravelWishlistLocation.text = wishlistData.travelLocation
            root.setOnClickListener {
                val action =
                    TravelWishlistFragmentDirections.actionFragmentTravelWishlistToFragmentTravelPlanUpdate()
                it.findNavController().navigate(action)
            }
        }
    }

    override fun getItemCount(): Int = arrayList.size


}


