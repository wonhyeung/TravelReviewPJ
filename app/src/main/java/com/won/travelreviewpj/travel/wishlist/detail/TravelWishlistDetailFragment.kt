package com.won.travelreviewpj.travel.wishlist.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.databinding.FragmentTravelWishlistDetailBinding

class TravelWishlistDetailFragment :
    ViewBindingBaseFragment<FragmentTravelWishlistDetailBinding>(FragmentTravelWishlistDetailBinding::inflate) {

    private val viewModel: TravelWishlistDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTravelWishlistDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fieldSetUp()
    }

    private fun fieldSetUp() {
        val id = arguments?.getLong("travelWishlistId")
        id?.let { viewModel.findWishlist(it) }
        Log.e("id", "$id")
        viewModel.getSearchResults().observe(viewLifecycleOwner) { travelWishlist ->
            with(binding) {
                tvTravelWishlistDetailTitle.text = travelWishlist?.wishlistTitle
                Log.e("travelWishlist", "${travelWishlist?.wishlistTitle}")

                tvTravelWishlistDetailAddress.text = travelWishlist?.wishlistAddress
                tvTravelWishlistDetailExplanation.text = travelWishlist?.wishlistOverview
                Glide.with(requireContext())
                    .load(travelWishlist?.wishlistImage)
                    .centerCrop()
                    .into(ivTravelWishlistDetail)
            }

        }

    }


}