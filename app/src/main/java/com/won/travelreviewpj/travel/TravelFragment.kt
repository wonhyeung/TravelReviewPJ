package com.won.travelreviewpj.travel

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.databinding.FragmentTravelBinding
import com.won.travelreviewpj.travel.wishlist.TravelWishlist
import com.won.travelreviewpj.travel.wishlist.TravelWishlistViewModel


class TravelFragment :
    ViewBindingBaseFragment<FragmentTravelBinding>(FragmentTravelBinding::inflate) {

    private val viewModel: TravelViewModel by viewModels()
    private val wishListViewModel: TravelWishlistViewModel by viewModels()
    private var currentTravelEntity: TravelEntity? = null // 전역변수 선언

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTravelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTravelData()

        viewModel.travelData.observe(viewLifecycleOwner) { travelEntity ->
            currentTravelEntity = travelEntity
            binding.apply {
                tvTravelDestination.text = travelEntity?.travelTitle
                tvTravelLocation.text = travelEntity?.travelAddress
                Glide.with(requireContext())
                    .load(travelEntity?.travelImage)
                    .centerCrop()
                    .into(ivTravel)
                with(bsTravel) {
                    tvBottomTravelDestination.text = travelEntity?.travelTitle
                    tvBottomTravelLocation.text = travelEntity?.travelAddress
                    tvBottomTravelOverview.text = travelEntity?.travelOverview
                    Glide.with(requireContext())
                        .load(travelEntity?.travelImage)
                        .centerCrop()
                        .into(ivBottomTravel)
                }
            }
        }

        with(binding) {
            ibTravelWishlist.setOnClickListener {
                val action =
                    TravelFragmentDirections.actionFragmentTravelToFragmentTravelWishlist()
                findNavController().navigate(action)
            }
            ibTravelWish.setOnClickListener {
                dialogShow()
            }
            bsTravel.ibBottomTravelWish.setOnClickListener {
                dialogShow()
            }
            mbTravelChange.setOnClickListener {
                viewModel.getResetData()
            }
        }
    }

    private fun dialogShow() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("위시리스트에 저장하시겠습니까?")
            .setNegativeButton("no") { dialog, which ->
                Log.e("button Click", "negative")
            }
            .setPositiveButton("yes") { dialog, which ->
                viewModel.insertTravelWishlist(
                    TravelWishlist(
                        wishlistTitle = currentTravelEntity?.travelTitle ?: "",
                        wishlistAddress = currentTravelEntity?.travelAddress ?: "",
                        wishlistImage = currentTravelEntity?.travelImage ?: "",
                        wishlistOverview = currentTravelEntity?.travelOverview ?: ""
                    )
                )
            }.show()
        Log.e("wishButton", "Clicked")
    }
}



