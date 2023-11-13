package com.won.travelreviewpj.travel.wishlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.won.travelreviewpj.R
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.databinding.FragmentTravelWishlistBinding


class TravelWishlistFragment :
    ViewBindingBaseFragment<FragmentTravelWishlistBinding>(FragmentTravelWishlistBinding::inflate) {

    private val viewModel: TravelWishlistViewModel by viewModels()

    private fun item() = mutableListOf<TravelWishlist>().apply {
        add(TravelWishlist(R.drawable.travel_sample, "111", "111"))
        add(TravelWishlist(R.drawable.travel_sample, "112", "112"))
        add(TravelWishlist(R.drawable.travel_sample, "113", "113"))
        add(TravelWishlist(R.drawable.travel_sample, "114", "114"))
        add(TravelWishlist(R.drawable.travel_sample, "115", "115"))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val manager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )
        with(binding) {
            rvTravelWishlist.layoutManager = manager
            rvTravelWishlist.adapter = TravelWishlistAdapter(item())
        }
    }

}

