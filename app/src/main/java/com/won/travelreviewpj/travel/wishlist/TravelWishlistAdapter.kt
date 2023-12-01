package com.won.travelreviewpj.travel.wishlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.won.travelreviewpj.databinding.ItemTravelWishlistBinding

class TravelWishlistAdapter(
    private var arrayList: MutableList<TravelWishlist>,
    private val viewModel: TravelWishlistViewModel
) :
    RecyclerView.Adapter<TravelWishlistAdapter.ItemHolder>() {
    inner class ItemHolder(val binding: ItemTravelWishlistBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding =
            ItemTravelWishlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(binding)
    }


    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = arrayList[position]
        with(holder.binding) {
            tvItemTravelWishlistTitle.text = item.wishlistTitle
            tvItemTravelWishlistLocation.text = item.wishlistAddress
            Glide.with(holder.itemView.context)
                .load(item.wishlistImage)
                .centerCrop()
                .into(ivItemTravelWishlist)
            root.setOnClickListener {
                val action =
                    TravelWishlistFragmentDirections.actionFragmentTravelWishlistToFragmentTravelWishlistDetail(
                        item.id
                    )
                it.findNavController().navigate(action)
            }
            ibItemTravelDelete.setOnClickListener {
                viewModel.deleteTravelWishlist(item.id)
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


