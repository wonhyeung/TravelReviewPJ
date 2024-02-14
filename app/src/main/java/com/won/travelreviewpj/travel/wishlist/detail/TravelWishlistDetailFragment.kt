package com.won.travelreviewpj.travel.wishlist.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding4.view.clicks
import com.won.travelreviewpj.R
import com.won.travelreviewpj.common.TRAVEL_WISHLIST_ID
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.databinding.FragmentTravelWishlistDetailBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Travel wishlist detail fragment
 *
 * 여행지 추천
 */
class TravelWishlistDetailFragment : ViewBindingBaseFragment<FragmentTravelWishlistDetailBinding>(FragmentTravelWishlistDetailBinding::inflate) {

    private val viewModel: TravelWishlistDetailViewModel by viewModels()
    private val compositeDisposable = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fieldSetUp()
    }

    private fun fieldSetUp() {
        val id = arguments?.getLong(TRAVEL_WISHLIST_ID)
        id?.let { viewModel.findWishlist(it) }
        viewModel.getSearchResults().observe(viewLifecycleOwner) { travelWishlist ->
            with(binding) {
                compositeDisposable.add(
                    ibTravelWishlistDetail.clicks()
                        .subscribe {
                            findNavController().popBackStack()
                        }
                )
                tvTravelWishlistDetailTitle.text = travelWishlist?.wishlistTitle

                tvTravelWishlistDetailAddress.text = travelWishlist?.wishlistAddress
                tvTravelWishlistDetailExplanation.text = travelWishlist?.wishlistOverview

                Glide.with(requireContext())
                    .load(travelWishlist?.wishlistImage)
                    .centerCrop()
                    .placeholder(R.drawable.image_basic)
                    .into(ivTravelWishlistDetail)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

}