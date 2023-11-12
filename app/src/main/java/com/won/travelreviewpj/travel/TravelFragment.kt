package com.won.travelreviewpj.travel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.databinding.FragmentTravelBinding


class TravelFragment : ViewBindingBaseFragment<FragmentTravelBinding>(FragmentTravelBinding::inflate) {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTravelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            ibTravelWishlist.setOnClickListener {
                val action =
                    TravelFragmentDirections.actionTravelFragmentToTravelWishlistFragment()
                findNavController().navigate(action)
            }
        }
    }


}