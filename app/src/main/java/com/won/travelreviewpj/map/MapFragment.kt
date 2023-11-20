package com.won.travelreviewpj.map

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.naver.maps.map.NaverMapSdk
import com.won.travelreviewpj.R
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.databinding.FragmentMapBinding


class MapFragment : ViewBindingBaseFragment<FragmentMapBinding>(FragmentMapBinding::inflate) {

    private fun item() = mutableListOf<Map>().apply {
        add(Map(R.drawable.travel_sample, "111", "혼자"))
        add(Map(R.drawable.travel_sample, "112", "혼자"))
        add(Map(R.drawable.travel_sample, "113", "혼자"))
        add(Map(R.drawable.travel_sample, "114", "혼자"))
        add(Map(R.drawable.travel_sample, "115", "혼자"))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NaverMapSdk.getInstance(requireContext()).client =
            NaverMapSdk.NaverCloudPlatformClient("r5qlsm0iph")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val manager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )
        with(binding) {
            rvMap.layoutManager = manager
            rvMap.adapter = MapAdapter(item())
        }
    }


}