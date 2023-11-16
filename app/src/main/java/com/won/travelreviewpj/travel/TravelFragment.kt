package com.won.travelreviewpj.travel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.won.travelreviewpj.common.SERVICE_KEY
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.databinding.FragmentTravelBinding
import com.won.travelreviewpj.databinding.FragmentTravelBottomsheetBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TravelFragment :
    ViewBindingBaseFragment<FragmentTravelBinding>(FragmentTravelBinding::inflate) {

    private val viewModel: TravelViewModel by viewModels()

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
            binding.apply {
                tvTravelDestination.text = travelEntity?.travelTitle
                tvTravelLocation.text = travelEntity?.travelAddress
                Glide.with(requireContext())
                    .load(travelEntity?.travelImage)
                    .fitCenter()
                    .into(ivTravel)
                bsTravel.tvBottomTravelDestination.text = travelEntity?.travelTitle
                bsTravel.tvBottomTravelLocation.text = travelEntity?.travelAddress
                bsTravel.tvBottomTravelOverview.text = travelEntity?.travelOverview
                Glide.with(requireContext())
                    .load(travelEntity?.travelImage)
                    .centerCrop()
                    .into(bsTravel.ivBottomTravel)

            }

        }

        with(binding) {
            ibTravelWishlist.setOnClickListener {
                val action =
                    TravelFragmentDirections.actionFragmentTravelToFragmentTravelWishlist()
                findNavController().navigate(action)
            }
        }


    }




}