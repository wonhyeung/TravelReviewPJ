package com.won.travelreviewpj.travel.wishlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.won.travelreviewpj.R
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.databinding.FragmentTravelWishlistBinding
import com.won.travelreviewpj.travel.TravelEntity
import com.won.travelreviewpj.travel.TravelRepository


class TravelWishlistFragment :
    ViewBindingBaseFragment<FragmentTravelWishlistBinding>(FragmentTravelWishlistBinding::inflate) {

    private val wishListViewModel: TravelWishlistViewModel by viewModels()
    private val adapter = TravelWishlistAdapter(mutableListOf())
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val swipeCallback = TravelWishlistSwipeCallback(adapter).apply {
            setClamp(resources.displayMetrics.widthPixels.toFloat() / 4)
        }
        ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.rvTravelWishlist)
        val manager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )
        with(binding) {
            rvTravelWishlist.layoutManager = manager
            rvTravelWishlist.adapter = adapter
            /* 화면 터치시 다른 item 닫기
            rvTravelWishlist.setOnTouchListener { _, _ ->
                swipeCallback.removePreviousClamp(binding.rvTravelWishlist)
                false
            }*/
        }
        wishListViewModel.getAllTravelWishlist()?.observe(viewLifecycleOwner) { travelWishlists ->
            adapter.setTravelWishList(travelWishlists)
        }


    }

}

