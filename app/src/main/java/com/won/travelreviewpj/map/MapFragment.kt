package com.won.travelreviewpj.map

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.won.travelreviewpj.R
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.databinding.FragmentMapBinding
import com.won.travelreviewpj.record.diary.RecordDiary
import kotlinx.coroutines.launch


class MapFragment : ViewBindingBaseFragment<FragmentMapBinding>(FragmentMapBinding::inflate) {

    private val mapViewModel: MapViewModel by viewModels()
    private lateinit var adapter: MapAdapter
    private lateinit var naverMap: NaverMap
    private var marker: Marker? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapSetting()
        fieldSetup()
        notifyDiary()
        observeDiaries()

    }
    private fun mapSetting() {
        val mapFragment = childFragmentManager.findFragmentById(binding.mapFragment.id) as MapFragment?
        mapFragment?.getMapAsync { naverMap ->
            this.naverMap = naverMap
        }
    }

    private fun fieldSetup() {
        lifecycleScope.launch {
            mapViewModel.getAllRecordDiary()
        }
    }

    private fun notifyDiary(diaries: List<RecordDiary> = emptyList()) {
        adapter = MapAdapter(R.layout.item_travel) { latLng ->
            naverMap.moveCamera(CameraUpdate.scrollTo(latLng))

            marker?.map = null

            marker = Marker().apply {
                position = latLng
                map = naverMap
            }
        }
        val manager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )
        with(binding) {
            rvMap.layoutManager = manager
            adapter.notifyMapDiaryList(diaries)
            rvMap.adapter = adapter
        }
    }

    private fun observeDiaries() {
        mapViewModel.diaries.observe(viewLifecycleOwner) { diaries ->
            adapter.notifyMapDiaryList(diaries)
        }
    }

}